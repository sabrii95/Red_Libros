package com.example.redlibros.Servicio

import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.FormUrlEncoded;
import com.example.redlibros.Model.Notification


interface ApiNotification {
    @POST("topic")
    @FormUrlEncoded
    fun sendNotification(@Field("topic") topic:String,@Field("title") title:String):Call<Notification>
}
