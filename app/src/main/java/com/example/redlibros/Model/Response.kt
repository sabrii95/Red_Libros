package com.example.redlibros.Model

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("items") var resultado_Item: List<BookResponse>,
    @SerializedName("totalItems") var totalItems: String?
)
