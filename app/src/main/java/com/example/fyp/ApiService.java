package com.example.fyp;

import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("signup")
    Call<SignupResponse> signup(@Body SignupRequest request);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("update-password")
    Call<LoginResponse> updatePassword(@Body LoginRequest request);

    @POST("save-profile")
    Call<ResponseBody> saveUserProfile(@Body ProfileData profileData);

    // Recommendation endpoint
    @POST("recommendations")
    Call<RecommendationResponse> getRecommendations(@Body Map<String, Object> userProfile);

    // Job Insights endpoint - Updated to use Adzuna API format
    //@GET("https://api.adzuna.com/v1/api/jobs/{country}/search/1")
    @GET("job-insights")
    Call<JobListResponse> getJobInsights(
            @Query("country") String Country,
            @Query("what") String What
    );

    // Alternative job search with location


    // Test endpoint
    @GET("test")
    Call<ResponseBody> testConnection();
}