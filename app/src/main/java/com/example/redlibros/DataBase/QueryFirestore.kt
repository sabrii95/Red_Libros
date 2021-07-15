package com.example.redlibros.DataBase

import android.content.Context
import android.widget.Toast
import com.example.redlibros.Model.VolumeInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class QueryFirestore {
    val db = FirebaseFirestore.getInstance()

    /*fun addUserBook(libro: VolumeInfo, email: String, array: String) {
        if (libro.id != "" && email!= "") {
             this.seearchBookDataBase(libro)
                 .addOnSuccessListener { Elementolibro->
                if(Elementolibro.documents.size > 0) {
                    this.bookforUser(email, array, libro.id).addOnCompleteListener { documento ->
                        if(documento.result.documents.isEmpty() ){
                            if(array != "userDeseo"){
                                Firebase.messaging.subscribeToTopic(libro.id)
                            }
                            db.collection("Libros").document(libro.id)
                                .update(array, FieldValue.arrayUnion(email))
                        }
                        else{
                            if(array != "userDeseo"){
                                Firebase.messaging.unsubscribeFromTopic(libro.id)
                            }
                            this.removeUser(libro, email, array)
                        }
                        //this.removeUser(libro, email, array)
                    }

                }
                else{

                    saveBookDataBase(libro, email, array )

                }
            }

        }


    }*/

    fun addUserBook(libro: VolumeInfo, email: String, array: String, contexto: Context) {
        if (libro.id != "" && email!= "") {
            this.seearchBookDataBase(libro)
                .addOnSuccessListener { Elementolibro->
                    if(Elementolibro.documents.size > 0) {
                        this.bookforUser(email, array, libro.id).addOnCompleteListener { documento ->
                            if(documento.result.documents.isEmpty() ){
                                if(array != "userDeseo"){
                                    Firebase.messaging.subscribeToTopic(libro.id)
                                }
                                db.collection("Libros").document(libro.id)
                                    .update(array, FieldValue.arrayUnion(email))
                                    .addOnSuccessListener { documentActualizado ->
                                        Toast.makeText(
                                            contexto,
                                            "El libro se agrego satisfactoriamente",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                            else{
                                if(array != "userDeseo"){
                                    Firebase.messaging.unsubscribeFromTopic(libro.id)

                                }
                                this.removeUser(libro.id, email, array)
                                    .addOnSuccessListener { documentActualizado ->
                                    Toast.makeText(
                                        contexto,
                                        "El libro se quito de la lista",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }
                            //this.removeUser(libro, email, array)
                        }

                    }
                    else{

                        saveBookDataBase(libro, email, array )
                            .addOnSuccessListener {
                                Toast.makeText(
                                    contexto,
                                    "El libro se agrego a la lista",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                    }
                }

        }


    }



    fun bookforUser(email: String, array: String, id: String): Task<QuerySnapshot> {
        return db.collection("Libros")
            .whereArrayContains(array,email )
            .whereEqualTo("id", id)
            .get()

    }
    fun seearchBookDataBase(libro: VolumeInfo): Task<QuerySnapshot>{
        return db.collection("Libros")
            .whereEqualTo("id", libro.id)
            .get()

    }
    fun removeUser(id:String, email: String, array: String): Task<Void> {
        return db.collection("Libros").document(id)
            .update (
                array , FieldValue.arrayRemove( email  )
            )
    }


    fun saveBookDataBase(libro: VolumeInfo, user: String, array: String): Task<Void> {
        return db.collection("Libros").document(libro.id ).set(
            hashMapOf(
                "id" to libro.id,
                "title" to libro.title,
                "authors" to libro.authors,
                "description" to libro.description,
                "publisher" to libro.publisher,
                "publishedDate" to libro.publishedDate ,
                "image" to libro.imageLinks?.thumbnail,
                array to listOf(user)

            )
        )

    }
    fun booksforUser(email: String, array: String): Task<QuerySnapshot> {
        return db.collection("Libros")
            .whereArrayContains(array,email )
            .get()

    }

    //// Actualizar ubicacion

    fun actualizarUbicacion(latitud : String, longitud: String, user:String) {

        db.collection("User").document(user)
            .update (
                "latitud" ,latitud,
                "longitud", longitud
            )
    }

}