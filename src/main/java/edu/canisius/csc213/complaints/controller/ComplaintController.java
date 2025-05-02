package edu.canisius.csc213.complaints.controller;

import edu.canisius.csc213.complaints.model.Complaint;
import edu.canisius.csc213.complaints.service.ComplaintSimilarityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ComplaintController {

    private final List<Complaint> complaints;
    private final ComplaintSimilarityService similarityService;

    public ComplaintController(List<Complaint> complaints, ComplaintSimilarityService similarityService) {
        this.complaints = complaints;
        this.similarityService = similarityService;
    }

    @GetMapping("/complaint")
    public String showComplaint(@RequestParam(name = "index", defaultValue = "0") String indexParam, Model model) {

        int index;
        int max = complaints.size();


        String error = null;

        try {
            index = Integer.parseInt(indexParam);
            if (index < 0 || index >= max) {
                error = "Please enter a number between 0 and " + (max - 1) + ".";
                index = 0; // fallback
            }
        } catch (NumberFormatException e) {
            error = "Invalid input. Please enter a numeric value.";
            index = 0; // fallback
        }

        Complaint current = complaints.get(index);
        List<Complaint> similar = similarityService.findTop3Similar(current);

        model.addAttribute("complaint", current);
        model.addAttribute("similarComplaints", similar);
        model.addAttribute("prevIndex", index > 0 ? index - 1 : 0);
        model.addAttribute("nextIndex", index < max - 1 ? index + 1 : max - 1);
        model.addAttribute("error", error); // 👈 Add the error to the model if present

        model.addAttribute("maxIndex", complaints.size() - 1);


        return "complaint";

    }

    // Endpoint to handle search by company name
    @GetMapping("/search")
    public String searchByCompany(@RequestParam(name = "company", required = false) String company, Model model) {
        if (company == null || company.trim().isEmpty()) {
            model.addAttribute("error", "Please enter a company name.");
            return "search";
        }

        // Case-insensitive filtering
        List<Complaint> matches = complaints.stream()
            .filter(c -> c.getCompany() != null &&
                        c.getCompany().toLowerCase().contains(company.toLowerCase()))
            .toList();

        if (matches.isEmpty()) {
            model.addAttribute("error", "No complaints found for that company.");
        } else {
            model.addAttribute("searchResults", matches);
        }

        model.addAttribute("companySearch", company); 
        return "search";
    }

}
