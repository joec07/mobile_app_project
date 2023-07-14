package com.example.comp7506_testing.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// for API
public class RetrofitClient {
    public static Retrofit retrofit;
    public static String baseURL = "https://1134-223-19-143-35.ngrok-free.app";

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
