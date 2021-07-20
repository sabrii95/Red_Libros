package com.example.redlibros

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.redlibros.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase



class login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding:ActivityLoginBinding
    lateinit var userdata: User
    val Google_SIGN_IN=100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = Firebase.auth

        val usuarioid= binding.edtUser
        val contraid= binding.edtPass

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        var mail = prefs.getString("email","")
        var pass = prefs.getString("pass","")
        var provider = prefs.getString("provider","")

        if(provider == "Basic") {
            binding.edtUser.setText(mail)
            binding.edtPass.setText(pass)
            userdata = User(mail!!, true, "", "", pass!!)

            if (mail != "" && pass != "") {
                binding.btnLoguear.setEnabled(false)
                binding.btnRegistro.setEnabled(false)
                binding.btnLoginGoogle.setEnabled(false)
                binding.edtUser.setEnabled(false)
                binding.edtPass.setEnabled(false)
                this.loguear(userdata)
            }
        }
        if(provider == "Google"){
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)

            var credencial =  prefs.getString("credential","")
            if (credencial != "") {
                binding.btnLoguear.setEnabled(true)
                binding.btnLoginGoogle.setEnabled(true)
                binding.btnRegistro.setEnabled(true)
                binding.edtUser.setEnabled(true)
                binding.edtPass.setEnabled(true)

            }

        }

        setContentView(binding.root)

        binding.btnLoguear.setOnClickListener{
            if(usuarioid.text.toString().equals("") || usuarioid.text.toString().equals(null)){
                Toast.makeText(
                    this,
                    "Debe completar los datos antes de continuar",
                    Toast.LENGTH_SHORT
                ).show()


            }
            else{
                binding.btnLoguear.setEnabled(false)
                binding.btnLoginGoogle.setEnabled(false)
                binding.btnRegistro.setEnabled(false)
                binding.edtPass.setEnabled(false)
                binding.edtUser.setEnabled(false)
                userdata = User(usuarioid.text.toString().trim(), true, "", "",contraid.text.toString())
                this.loguear(userdata)
            }



        }
        binding.btnLoginGoogle.setOnClickListener{
            binding.edtPass.setEnabled(false)
            binding.edtUser.setEnabled(false)
            binding.btnLoguear.setEnabled(false)
            binding.btnLoginGoogle.setEnabled(false)
            binding.btnRegistro.setEnabled(false)
            val gooleConfig= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleClient = GoogleSignIn.getClient(this, gooleConfig)
            startActivityForResult(googleClient.signInIntent, Google_SIGN_IN)
            googleClient.signOut()
            contraid.setVisibility(View.GONE )



        }
        binding.btnRegistro.setOnClickListener {
            if (usuarioid.text.toString().equals("") || usuarioid.text.toString().equals(null)) {
                Toast.makeText(
                    this,
                    "Debe completar los datos antes de continuar",
                    Toast.LENGTH_SHORT
                ).show()


            }
            else{
                binding.edtPass.setEnabled(false)
                binding.edtUser.setEnabled(false)
                binding.btnLoguear.setEnabled(false)
                binding.btnLoginGoogle.setEnabled(false)
                binding.btnLoguear.setEnabled(false)
                userdata = User(usuarioid.text.toString(), true, "https://image.flaticon.com/icons/png/512/681/681392.png", "",contraid.text.toString()
                )
                this.registroUser(userdata)
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
                    this.credentialGoogle(credential)

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
                .addOnCompleteListener { document ->
                    if(document.result.data?.get("enable") == false) {
                        Toast.makeText(
                            baseContext,
                            "El usuario se encuentra dado de baja ",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.edtPass.setEnabled(true)
                        binding.edtUser.setEnabled(true)
                        binding.btnLoguear.setEnabled(true)
                        binding.btnLoginGoogle.setEnabled(true)
                        binding.btnLoguear.setEnabled(true)

                    }
                    else {
                        auth.signInWithEmailAndPassword(user.email, user.pass)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this, MainActivity::class.java)

                                    val datosusuario = prefs.edit()
                                    datosusuario.putString(
                                        "username",
                                        document.result.data?.get("userName").toString()
                                    )
                                    datosusuario.putString(
                                        "image",
                                        document.result.data?.get("image").toString()
                                    )
                                    datosusuario.putString("email", user.email)
                                    datosusuario.putString("pass", user.pass)
                                    datosusuario.putString(
                                        "latitud",
                                        document.result.data?.get("latitud")?.toString()
                                    )
                                    datosusuario.putString(
                                        "longitud",
                                        document.result.data?.get("longitud")?.toString()
                                    )
                                    datosusuario.putString("provider", "Basic")
                                    datosusuario.apply()


                                    startActivity(intent)
                                    finish()


                                } else {
                                    Toast.makeText(
                                        baseContext,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.btnLoguear.setEnabled(true)
                                    binding.btnLoginGoogle.setEnabled(true)
                                    binding.btnRegistro.setEnabled(true)
                                    binding.edtPass.setEnabled(true)
                                    binding.edtUser.setEnabled(true)
                                }
                            }
                    }


                }
        }

    }
    fun registroUser(user: User){

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if(user.email!= "" || user.pass != "") {
            auth.createUserWithEmailAndPassword(user.email, user.pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        //val datosUser = auth.currentUser
                        this.ingresarUser(user)
                        val intent = Intent(this, MainActivity::class.java).apply {
                            val datosusuario = prefs.edit()
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
                            "Resgistro failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.edtPass.setEnabled(true)
                        binding.edtUser.setEnabled(true)
                        binding.btnLoguear.setEnabled(true)
                        binding.btnLoginGoogle.setEnabled(true)
                        binding.btnLoguear.setEnabled(true)


                    }
                }
        }

    }
    // insert Data Base
    fun ingresarUser(user_data: User) {

        db.collection("User").document(user_data.email).set(
            hashMapOf("email" to user_data.email,
                "enable" to user_data.enable,
                "image" to user_data.image,
                "username" to user_data.userName,
                "latitud" to user_data.latitud,
                "longitud" to user_data.longitud)

        )

    }
    fun credentialGoogle(credential : AuthCredential){
        auth.signInWithCredential(credential).addOnCompleteListener(this) { it ->
            if (it.isSuccessful){
                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                val user = FirebaseAuth.getInstance().currentUser
                var userRef = db.collection("User").document(user?.email.toString()).get()
                    .addOnCompleteListener() { document ->
                        if(document.isSuccessful && document.result.data?.get("email").toString()!= "null"){
                            if(document.result.data?.get("enable") == false){
                                Toast.makeText(
                                    baseContext,
                                    "El usuario se encuentra dado de baja ",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.edtPass.setEnabled(true)
                                binding.edtUser.setEnabled(true)
                                binding.btnLoguear.setEnabled(true)
                                binding.btnLoginGoogle.setEnabled(true)
                                binding.btnLoguear.setEnabled(true)

                            }
                            else{
                                val intent = Intent(this, MainActivity::class.java)
                                val datosusuario = prefs.edit()
                                datosusuario.putString("username", user!!.displayName)
                                datosusuario.putString("image", user.photoUrl.toString())
                                datosusuario.putString("email", user.email)
                                datosusuario.putString("latitud", document.result.data?.get("latitud").toString() )
                                datosusuario.putString("longitud",document.result.data?.get("longitud").toString() )
                                datosusuario.putString("provider", "Google" )
                                datosusuario.apply()
                                startActivity(intent)
                                finishAffinity()
                            }


                        }
                        else{
                            val dataUser = User(
                                user?.email.toString(),
                                true,
                                user?.photoUrl.toString(),
                                user!!.displayName.toString(),
                                "",
                                "",
                                "0",
                                "0")
                            ingresarUser(dataUser)
                            val intent = Intent(this, MainActivity::class.java)
                            val datosusuario = prefs.edit()
                            datosusuario.putString("username", user!!.displayName)
                            datosusuario.putString("image", user.photoUrl.toString())
                            datosusuario.putString("email", user.email)
                            datosusuario.putString("latitud", document.result.data?.get("latitud").toString() )
                            datosusuario.putString("longitud",document.result.data?.get("longitud").toString() )
                            datosusuario.putString("provider", "Google" )

                            datosusuario.apply()

                            startActivity(intent)
                            finishAffinity()

                        }

                    }

            }
            else{
                Toast.makeText(
                    baseContext,
                    "Authentication failed",
                    Toast.LENGTH_SHORT
                ).show()
                binding.edtPass.setEnabled(true)
                binding.edtUser.setEnabled(true)
                binding.btnLoguear.setEnabled(true)
                binding.btnLoginGoogle.setEnabled(true)
                binding.btnLoguear.setEnabled(true)

            }
        }

    }






}


