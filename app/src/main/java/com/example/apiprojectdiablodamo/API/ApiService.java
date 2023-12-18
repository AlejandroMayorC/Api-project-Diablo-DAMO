package com.example.apiprojectdiablodamo.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static final String BASE_URL_OAUTH = "https://oauth.battle.net/";
    private static final String BASE_URL_API = "https://us.api.blizzard.com/";

    private static Retrofit getRetrofitInstance(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiInterface getOAuthApiInterface() {
        return getRetrofitInstance(BASE_URL_OAUTH).create(ApiInterface.class);
    }

    public static ApiInterface getApiInterface() {
        return getRetrofitInstance(BASE_URL_API).create(ApiInterface.class);
    }
}

