package com.example.weatherapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {
    private const val baseUrl = "https://api.weatherapi.com"

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // Corrected method name
            .build()
    }


   val weatherApi: WeatherApi = getInstance().create(WeatherApi::class.java)

}
