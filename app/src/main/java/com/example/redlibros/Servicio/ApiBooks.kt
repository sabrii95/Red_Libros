package com.example.redlibros.Servicio

import com.example.redlibros.Model.BookResponse
import com.example.redlibros.Model.BooksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiBooks {
    @GET("volumes/{id}")
    fun getBook(@Path("id") id: String): Call<BookResponse>

    @GET("volumes")
    fun getBooksByName(@Query("q")query: String): Call<BooksResponse>
}
