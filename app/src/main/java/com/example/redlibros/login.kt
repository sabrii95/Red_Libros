package com.example.redlibros

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.preference.PreferenceManager
import com.example.redlibros.databinding.ActivityLoginBinding
import com.example.redlibros.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.io.Serializable


class login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    private lateinit var binding:ActivityLoginBinding
    lateinit var userdata: User
    val Google_SIGN_IN=100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val usuarioid= binding.edtUser
        val contraid= binding.edtPass
        val btn_loguear = binding.btnLoguear
        val usernameid = binding.edtUsername



        val parametros = this.intent.extras
        if (parametros != null) {
            val datos = parametros.getString("Boton")

            if (datos == "Registro"){
                usernameid.setVisibility(View.VISIBLE );

                btn_loguear.setOnClickListener {
                    userdata = User(usuarioid.text.toString(), true, "https://image.flaticon.com/icons/png/512/681/681392.png", usernameid.text.toString(),contraid.text.toString())
                    this.registroUser(userdata)
                }
            }

            if (datos == "IniciarSesion") {
                usernameid.setVisibility(View.GONE);
                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                val email = prefs.getString("email","")
                val pass = prefs.getString("pass","")
                val username = prefs.getString("username", "")
                if(email!= "" && pass!= "") {
                    usuarioid.setText(email)
                    contraid.setText(pass)

                }
                btn_loguear.setOnClickListener {

                    userdata = User(usuarioid.text.toString(), true, "", "",contraid.text.toString())
                    this.loguear(userdata)
                }
            }
            if (datos == "Google") {
                val gooleConfig= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val googleClient = GoogleSignIn.getClient(this, gooleConfig)
                startActivityForResult(googleClient.signInIntent, Google_SIGN_IN)
                googleClient.signOut()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Google_SIGN_IN ) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d("activity result", "firebaseAuthWithGoogle:" + account.id)
                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                    auth.signInWithCredential(credential).addOnCompleteListener(this) { it ->
                        if (it.isSuccessful){
                            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                            val user = FirebaseAuth.getInstance().currentUser
                            val intent = Intent(this, MainActivity::class.java)
                                var datosusuario = prefs.edit()
                                datosusuario.putString("username", user!!.displayName)
                                datosusuario.putString("image", user.photoUrl.toString() )
                                datosusuario.putString("email", user.email)
                                datosusuario.apply()

                            startActivity(intent)
                            finishAffinity()

                        }
                        else{
                            Toast.makeText(
                                baseContext,
                                "Authentication failed."+task.isSuccessful,
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                }

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("no se ejecuto activity result", "Google sign in failed", e)
            }
        }
    }
    fun loguear(user: User){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if(user.email!= "" || user.pass != "") {
            var userRef = db.collection("User").document(user.email).get()
                .addOnSuccessListener { document ->
                    auth.signInWithEmailAndPassword(user.email, user.pass)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {


                                val intent = Intent(this, MainActivity::class.java).apply {

                                    var datosusuario = prefs.edit()
                                    datosusuario.putString("username", document.data?.get("username").toString())
                                    datosusuario.putString("image", document.data?.get("image").toString() )
                                    datosusuario.putString("email", user.email)
                                    datosusuario.putString("pass", user.pass)
                                    datosusuario.apply()

                                }
                                startActivity(intent)
                                finishAffinity()


                            } else {
                                Toast.makeText(
                                    baseContext,
                                    "Authentication failed."+task.isSuccessful,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }


                }
        }

    }
    fun registroUser(user: User){
        Toast.makeText(
            baseContext,
            "usuario"+user.email+"contr"+user.pass,
            Toast.LENGTH_SHORT
        ).show()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if(user.email!= "" || user.pass != "") {
            auth.createUserWithEmailAndPassword(user.email, user.pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //val datosUser = auth.currentUser
                        this.ingresarUser(user)
                        val intent = Intent(this, MainActivity::class.java).apply {
                            var datosusuario = prefs.edit()
                            datosusuario.putString("username", user.userName)
                            datosusuario.putString("email", user.email)
                            datosusuario.putString("pass", user.pass)
                            datosusuario.putString("image", user.image)
                            datosusuario.apply()
                        }
                        startActivity(intent)
                        finish();

                    } else {
                        Toast.makeText(
                            this,
                            "Resgistro failed." + user.email+""+ " contra " + user.pass,
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


