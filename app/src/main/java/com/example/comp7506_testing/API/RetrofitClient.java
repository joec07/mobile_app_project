package com.example.comp7506_testing.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// for API
public class RetrofitClient {
    public static Retrofit retrofit;
    public static String baseURL = "https://4794-202-189-105-94.ngrok-free.app";

    // singleton structure
    public static Retrofit getRetrofitInstance(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
