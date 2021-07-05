package com.example.redlibros.api.service

import com.example.redlibros.api.model.BookResponse
import com.example.redlibros.api.model.BooksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("volumes/{id}")
    fun getBook(@Path("id") id: String): Call<BookResponse>

    @GET("volumes")
    fun getBooksQuery(@Query("q") query: String): Call<BooksResponse>
}