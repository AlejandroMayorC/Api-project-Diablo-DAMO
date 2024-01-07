package com.example.apiprojectdiablodamo.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static final String BASE_URL_OAUTH = "https://oauth.battle.net/";
    private static final String BASE_URL_API = "https://us.api.blizzard.com/";

    private static Retrofit getRetrofitInstance(String baseUrl) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Double.class, new CustomDoubleDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static ApiInterface getOAuthApiInterface() {
        return getRetrofitInstance(BASE_URL_OAUTH).create(ApiInterface.class);
    }

    public static ApiInterface getApiInterface() {
        return getRetrofitInstance(BASE_URL_API).create(ApiInterface.class);
    }
}
