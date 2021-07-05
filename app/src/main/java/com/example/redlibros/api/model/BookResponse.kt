package com.example.redlibros.api.model

data class BookResponse(
    val id: String?,
    val selfLink: String?,
    val volumeInfo: VolumeInfo?,
)