package com.example.redlibros.api.model

import com.google.gson.JsonObject

data class BooksResponse(
    val totalItems: String?,
    val items: List<BookResponse>?,
)