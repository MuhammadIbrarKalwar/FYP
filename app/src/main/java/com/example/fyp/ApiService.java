package com.example.fyp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("signup") // Change path based on your API route
    Call<SignupResponse> signup(@Body SignupRequest request);

    @POST("login") // Change path based on your API route
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("update-password") // Change path based on your API route
    Call<LoginResponse> updatePassword(@Body LoginRequest request);


}

