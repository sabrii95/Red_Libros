package com.example.redlibros.DataBase

import com.example.redlibros.Model.VolumeInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class QueryFirestore {
    val db = FirebaseFirestore.getInstance()

     fun addUserBook(libro: VolumeInfo, email: String, array: String) {
        if (libro.title.toString() != "" && email!= null) {
            this.validarUser(email, array).addOnSuccessListener {document ->
                if (document.documents.size == 0){
                    this.seearchBookDataBase(libro).addOnCompleteListener { Elementolibro->
                        if(Elementolibro.isSuccessful) {
                            db.collection("Libros").document(libro.title.toString())
                                .update (
                                    array , FieldValue.arrayUnion( email  )
                                )

                        }
                        else {
                            saveBookDataBase(libro, email, array )
                        }
                    }
                }
                else{
                    this.removeUser(libro, email, array)

                }
            }

        }

    }
    fun validarUser(email: String, array: String): Task<QuerySnapshot> {
        return db.collection("Libros")
            .whereArrayContains(array,email )
            .get()

    }
    fun seearchBookDataBase(libro: VolumeInfo): Task<QuerySnapshot>{
        return db.collection("Libros")
            .whereEqualTo("title", libro.title)
            .limit(1)
            .get()

    }
    fun removeUser(libro: VolumeInfo, email: String, array: String){
        db.collection("Libros").document(libro.title.toString())
            .update (
                array , FieldValue.arrayRemove( email  )
            )
    }


    fun saveBookDataBase(libro: VolumeInfo, user: String, array: String) {
        db.collection("Libros").document(libro.title.toString() ).set(
            hashMapOf(
                "title" to libro.title,
                "authors" to libro.authors,
                "description" to libro.description,
                "publisher" to libro.publisher,
                "publishedDate" to libro.publishedDate ,
                array to listOf(user)

            )
        )

    }

}