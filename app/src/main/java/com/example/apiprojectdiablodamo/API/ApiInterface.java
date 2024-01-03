package com.example.apiprojectdiablodamo.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("token")
    @FormUrlEncoded
    Call<AccessTokenResponse> obtenerTokenDeAcceso(
            @Field("grant_type") String grantType,
            @Header("Authorization") String credentials
    );


    @GET("d3/data/hero/{classSlug}")
    Call<Personaje> obtenerPersonaje(@Path("classSlug") String classSlug, @Header("Authorization") String authToken);




}
