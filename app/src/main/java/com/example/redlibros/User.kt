package com.example.redlibros
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val email: String,
    val enable: Boolean,
    val image: String = "https://firebasestorage.googleapis.com/v0/b/red-biblioteca.appspot.com/o/profile%2Fdefault.jpg?alt=media&token=7cd210e3-3ddc-43ec-a011-7629bafbb448",
    val userName: String,
    val pass: String,
    val fullname: String = ""
): Parcelable {

}
