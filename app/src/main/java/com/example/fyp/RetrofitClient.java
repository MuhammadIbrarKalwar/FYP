package com.example.fyp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
                                   // 192.168.173.220
public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.173.220:8000/api/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
