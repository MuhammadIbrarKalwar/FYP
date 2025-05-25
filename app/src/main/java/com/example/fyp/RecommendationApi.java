package com.example.fyp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class RecommendationApi {
    @POST("recommendations")
    Call<RecommendationResponse> getRecommendations(@Body UserSkills skills) {
        return null;
    }
}
