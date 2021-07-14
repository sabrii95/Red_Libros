package com.example.redlibros.Servicio

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotificationApi {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://45.79.10.185:5555/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}