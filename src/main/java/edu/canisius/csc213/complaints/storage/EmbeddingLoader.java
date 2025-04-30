package edu.canisius.csc213.complaints.storage;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class EmbeddingLoader {

    /**
     * Loads complaint embeddings from a JSONL (newline-delimited JSON) file.
     * Each line must be a JSON object with:
     * {
     *   "complaintId": <long>,
     *   "embedding": [<double>, <double>, ...]
     * }
     *
     * @param jsonlStream InputStream to the JSONL file
     * @return A map from complaint ID to its embedding vector
     * @throws IOException if the file cannot be read or parsed
     */
    public static Map<Long, double[]> loadEmbeddings(InputStream jsonlStream) throws IOException {
        // TODO: Implement parsing of JSONL to extract complaintId and embedding
        // This is a placeholder implementation. You need to replace it with actual parsing logic.
        Map<Long, double[]> embeddings = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(jsonlStream))){
            String line;


            while ((line = reader.readLine()) != null) {
                Map<String, Object> jsonMap = objectMapper.readValue(line, Map.class);
            
                String idString = (String) jsonMap.get("id");
                if (idString == null) {
                    throw new IllegalArgumentException("Missing id in line: " + line);
                }
                Long compLongId = Long.parseLong(idString);
            
                List<Double> embeddingList = (List<Double>) jsonMap.get("embedding");
                if (embeddingList == null) {
                    throw new IllegalArgumentException("Missing embedding in line: " + line);
                }
            
                double[] embedding = new double[embeddingList.size()];
                for (int i = 0; i < embeddingList.size(); i++) {
                    embedding[i] = embeddingList.get(i);
                }
            
                embeddings.put(compLongId, embedding);
            }
            
            

            /* 
            while (reader.readLine() != null) {
                line = reader.readLine();
                Map<String, Object> jsonMap = objectMapper.readValue(line, Map.class);
                Long compLongId = ((Number) jsonMap.get("complaintId")).longValue();
                List<Double> embeddingList = (List<Double>) jsonMap.get("embedding");
                double[] embedding = new double[embeddingList.size()];
                for (int i = 0; i < embeddingList.size(); i++) {
                    embedding[i] = embeddingList.get(i);
                }
                embeddings.put(compLongId, embedding);
            }
            */

        }
        
        return embeddings;
    }

}
