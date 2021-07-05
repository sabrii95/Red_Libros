package com.example.redlibros
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val email: String,
    val enable: Boolean,
    val image: String,
    val userName: String,
    val pass: String,
    val fullname: String = ""
): Parcelable {

}
