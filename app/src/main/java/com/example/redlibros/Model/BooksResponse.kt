package com.example.redlibros.Model

import com.google.gson.JsonObject

data class BooksResponse(
    val totalItems: String?,
    val items: List<BookResponse>?,
)