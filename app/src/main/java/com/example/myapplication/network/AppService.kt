package com.example.myapplication.network

import com.example.myapplication.network.response.model.Country
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AppService{

    @GET("all")
    fun getAllCountry(): Call<List<Country>>

    @GET("name/{country}")
    fun getCountryDetails(@Path("country") country: String?): Call<List<Country>>

    @GET("alpha/{code}")
    fun getCountryByCode(@Path("code") code: String?): Call<Country>


}