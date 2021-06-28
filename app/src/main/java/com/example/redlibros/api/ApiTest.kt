package com.example.redlibros.api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.redlibros.R
import com.example.redlibros.api.model.BookResponse
import com.example.redlibros.api.model.BooksResponse
import com.example.redlibros.api.service.APIService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_test)
        asd()
    }

    private fun asd() {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://www.googleapis.com/books/v1/").addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(APIService::class.java)

        // Buscar por Id
        service.getBook("2zgRDXFWkm8C").enqueue(
            object : Callback<BookResponse> {
                override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                    val book = response?.body()
                    val title: String = book?.volumeInfo?.title.toString()
                    Log.i("BOOK", Gson().toJson(book))
                    Log.i("BOOK", title)
                }

                override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                    t?.printStackTrace()
                }

            }
        )

        // Buscar por query la palabra potter
        service.getBooksQuery("potter").enqueue(
            object : Callback<BooksResponse> {
                override fun onResponse(call: Call<BooksResponse>, response: Response<BooksResponse>) {
                    val books = response?.body() as BooksResponse
                    val a: List<BookResponse> = books.items as List<BookResponse>
                    Log.i("BOOKS", Gson().toJson(a))
                }

                override fun onFailure(call: Call<BooksResponse>, t: Throwable) {
                    t?.printStackTrace()
                }

            }
        )
    }

}