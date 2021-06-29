package com.example.redlibros.Servicio

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CallApiBook {

     fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}