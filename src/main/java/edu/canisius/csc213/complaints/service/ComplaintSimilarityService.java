package edu.canisius.csc213.complaints.service;

import edu.canisius.csc213.complaints.model.Complaint;

import java.util.List;

public class ComplaintSimilarityService {

    private final List<Complaint> complaints;

    public ComplaintSimilarityService(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public List<Complaint> findTop3Similar(Complaint target) {
        // TODO: Return top 3 most similar complaints (excluding itself)
        // 1. Calculate cosine similarity for each complaint
        // 2. Sort by similarity score
        // 3. Return top 3
        List<ComplaintWithScore> scores = complaints.stream()
                .filter(c -> c.getComplaintId() != target.getComplaintId())
                .map(c -> new ComplaintWithScore(c, cosineSimilarity(c.getEmbedding(), target.getEmbedding())))
                .sorted((a, b) -> Double.compare(b.score, a.score))
                .limit(3)
                .toList();
        // Convert to List<Complaint>
        return scores.stream()
                .map(cs -> cs.complaint)
                .toList();
        // If no similar complaints found, return an empty list
        


    }

    private double cosineSimilarity(double[] a, double[] b) {
        // TODO: Implement cosine similarity
        if (a.length != b.length) {
            throw new IllegalArgumentException("Vectors must be of the same length");
        }
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA == 0.0 || normB == 0.0) {
            return 0.0; // Avoid division by zero
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private static class ComplaintWithScore {
        Complaint complaint;
        double score;

        ComplaintWithScore(Complaint c, double s) {
            this.complaint = c;
            this.score = s;
        }
    }
}
