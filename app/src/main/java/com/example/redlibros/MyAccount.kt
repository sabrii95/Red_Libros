package com.example.redlibros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.example.redlibros.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log


class MyAccount : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    lateinit var userdata: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        val switch_edit = findViewById<Switch>(R.id.switch_edit)
        val switch_close = findViewById<Switch>(R.id.switch_close)
        val txt_name = findViewById<TextView>(R.id.txt_name)
        val txt_username = findViewById<TextView>(R.id.txt_username)
        val txt_email = findViewById<TextView>(R.id.txt_email)
        val txt_password = findViewById<TextView>(R.id.txt_password)
        val btn_close_account = findViewById<Button>(R.id.btn_close_account)
        val btn_save = findViewById<Button>(R.id.btn_save)
        val btn_cancel = findViewById<Button>(R.id.btn_cancel)

        // Cargo los datos guardados
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        txt_name.setText(prefs.getString("name", ""))
        txt_email.setText(prefs.getString("email", ""))
        txt_username.setText(prefs.getString("username", ""))
        txt_password.setText(prefs.getString("pass", ""))
        // Email no puede ser editado.
        txt_email.isEnabled = false

        // Cambio entre modo edición y no edición de datos
        switch_edit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // El switch esta activo para edición
                txt_name.isEnabled = true
                txt_username.isEnabled = true
                txt_password.isEnabled = true
                btn_save.isEnabled = true
                btn_cancel.isEnabled = true
            } else {
                txt_name.isEnabled = false
                txt_username.isEnabled = false
                txt_password.isEnabled = false
                btn_save.isEnabled = false
                btn_cancel.isEnabled = false
            }
        }
        // Cambio entre modo edición y no edición de close account
        switch_close.setOnCheckedChangeListener { buttonView, isChecked ->
            btn_close_account.isEnabled = if(switch_close.isChecked)true else false
        }
            // Botón Cancel
        btn_cancel.setOnClickListener{
            switch_edit.isChecked = if(switch_edit.isChecked)false else true
        }

        // Botón Save
        btn_save.setOnClickListener{
            userdata = User(
                email=txt_email.text.toString(),
                enable=true,
                image="",
                userName=txt_username.text.toString(),
                pass=txt_password.text.toString(),
                fullname=txt_name.text.toString(),
            )
            this.editInfo(userdata)
            switch_edit.isChecked = if(switch_edit.isChecked)false else true
        }

        // Botón borrar cuenta
        btn_close_account.setOnClickListener{
            userdata = User(
                email=txt_email.text.toString(),
                enable=false,
                image="",
                userName=txt_username.text.toString(),
                pass=txt_password.text.toString(),
                fullname=txt_name.text.toString(),
            )
            this.closeAccount(userdata)
        }
    }

    fun editInfo(user: User){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        var userRef = db.collection("User").document(user.email).set(user)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java).apply {
                    var datosusuario = prefs.edit()
                    datosusuario.putString("username", user.userName)
                    datosusuario.putString("image", user.image)
                    datosusuario.putString("pass", user.pass)
                    datosusuario.putString("fullname", user.fullname)
                    datosusuario.apply()
                }
                startActivity(intent)
                finishAffinity()
            }
            .addOnFailureListener {
                Toast.makeText(baseContext,"Error",Toast.LENGTH_SHORT).show()
            }
    }

    fun closeAccount(user: User){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        var userRef = db.collection("User").document(user.email).set(user)
            .addOnSuccessListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, opciones_login::class.java)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(baseContext,"Error",Toast.LENGTH_SHORT).show()
            }
    }
}