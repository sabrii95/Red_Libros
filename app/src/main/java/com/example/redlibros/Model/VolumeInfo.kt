package com.example.redlibros.Model

data class VolumeInfo(val title: String?,
                      val authors: List<String>?,
                      val publisher: String?,
                      val publishedDate: String?,
                      val description: String?,
                      val categories: List<String>?,
                      val averageRating: String?,
                      val ratingsCount: String?,
                      val imageLinks: ImageLinks?,
                      val language: String?,

                      )
