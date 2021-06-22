package com.example.redlibros

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.io.Serializable


class login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth

        val usuarioid= findViewById<EditText>(R.id.edt_user)
        val contraid= findViewById<EditText>(R.id.edt_pass)
        val btn_loguear = findViewById<Button>(R.id.btn_loguear)
        val username = findViewById<EditText>(R.id.edt_username)
        val parametros = this.intent.extras
        if (parametros != null) {
            val datos = parametros.getString("Boton")

            if (datos == "Registro"){
                username.setVisibility(View.VISIBLE );
                btn_loguear.setOnClickListener {
                    this.registroUser(usuarioid, contraid)
                }
            }

            if (datos == "IniciarSesion") {
                username.setVisibility(View.GONE);
                btn_loguear.setOnClickListener {
                    this.loguear(usuarioid, contraid)
                }
            }

        }

    }
    fun loguear(usuarioid: EditText,contraid: EditText){
        /*var userdata = User(usuarioid.text.toString(), true, "", "sabrina")
       if(usuarioid.text.isNotEmpty() && contraid.text.isNotEmpty()){
           auth.signInWithEmailAndPassword(usuarioid.text.toString(), contraid.text.toString())
               .addOnCompleteListener(this) { task ->
                   if (task.isSuccessful) {
                       // Sign in success, update UI with the signed-in user's information
                       val user = auth.currentUser
                       val intent = Intent(this, MainActivity::class.java).apply {
                           putExtra("dataUser","valor" )
                       }
                       startActivity(intent)
                       finish();

                   } else {
                       // If sign in fails, display a message to the user.
                       Toast.makeText(baseContext, "Authentication failed."+task.isSuccessful,Toast.LENGTH_SHORT).show()
                   }*/
          var userRef = db.collection("User").document(usuarioid.text.toString()).get()
               .addOnSuccessListener {
                       document ->
                   auth.signInWithEmailAndPassword(usuarioid.text.toString(), contraid.text.toString())
                       .addOnCompleteListener(this) { task ->
                           if (task.isSuccessful) {
                               // Sign in success, update UI with the signed-in user's information
                               val user = auth.currentUser
                               var userdata = User(usuarioid.text.toString(), true, "", "sabrina")
                               val intent = Intent(this, MainActivity::class.java).apply {
                                   putExtra("dataUser",userdata)
                               }
                               startActivity(intent)
                               finish();


                           } else {
                               // If sign in fails, display a message to the user.
                               Toast.makeText(baseContext, "Authentication failed.",Toast.LENGTH_SHORT).show()
                           }
                       }


        }

    }
    fun registroUser(usuarioid: EditText, contraid:EditText){

        if(usuarioid.text.isNotEmpty() && contraid.text.isNotEmpty()) {
            var userdata = User(usuarioid.text.toString(), true, "", "sabrina")
            auth.createUserWithEmailAndPassword(usuarioid.text.toString(), contraid.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //val datosUser = auth.currentUser
                        this.ingresarUser(userdata)
                        val intent = Intent(this, MainActivity::class.java).apply {
                            putExtra("dataUser", userdata )
                        }
                        startActivity(intent)
                        finish();

                    } else {
                        Toast.makeText(
                            this,
                            "Resgistro failed." + usuarioid.text.toString()+task + " contra " + contraid.text.toString(),
                            Toast.LENGTH_SHORT
                        ).show()


                    }
                }
        }

    }




    fun ingresarUser(user_data: User) {

        db.collection("User").document(user_data.email).set(
            hashMapOf("email" to user_data.email,
                "enable" to user_data.enable,
                "image" to user_data.image,
                "username" to user_data.userName)
        )

    }


}


