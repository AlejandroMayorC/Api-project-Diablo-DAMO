package com.example.apiprojectdiablodamo.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("token")
    @FormUrlEncoded
    Call<AccessTokenResponse> obtenerTokenDeAcceso(
            @Field("grant_type") String grantType,
            @Header("Authorization") String credentials
    );
    @GET("d3/data/hero/barbarian")
    Call<Personaje> obtenerPersonaje(@Header("Authorization") String authToken);

}
