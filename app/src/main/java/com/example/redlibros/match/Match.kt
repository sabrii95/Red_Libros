package com.example.redlibros.match

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redlibros.Model.BookMatch
import com.example.redlibros.Model.VolumeInfo
import com.example.redlibros.R
import com.example.redlibros.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_match.*

class Match : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)
        initRecycler()
    }

    fun initRecycler() {

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        var books = mutableListOf<BookMatch>()
        db.collection("Libros").whereArrayContains("userDeseo", prefs.getString("email", "").toString()).get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    val title = document.get("title").toString()
                    val authors = document.get("authors") as List<String>
                    val image = document.get("image").toString()
                    val usersPerteneciente = document.get("usersPerteneciente") as List<String>
                    books.add(BookMatch(title, authors, image, usersPerteneciente))
                }
                rvBook.layoutManager = LinearLayoutManager(this)
                val adapter = MatchItem(books)
                rvBook.adapter = adapter
            }
    }
}