package com.example.redlibros.Model

data class BookMatch(val title: String?,
                     val authors: List<String>?,
                     val image: String?,
                     val users: List<String>?,
                     var expanded: Boolean = false
                    )