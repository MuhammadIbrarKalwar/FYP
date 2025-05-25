package com.example.fyp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RecommendationResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("recommendations")
    private List<String> recommendations;

    @SerializedName("message")
    private String message;

    // Getters
    public String getStatus() {
        return status;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public String getMessage() {
        return message;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}