package com.example.redlibros.Servicio

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotificationApi {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://localhost:3030/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}