package com.example.redlibros

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.redlibros.databinding.ActivityOpcionesLoguinBinding

class opciones_login : AppCompatActivity() {
    private lateinit var binding: ActivityOpcionesLoguinBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityOpcionesLoguinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSesionNomal.setOnClickListener {
            val intentSesion = Intent(this, login::class.java).apply {
                putExtra("Boton","IniciarSesion" )
            }
            startActivity(intentSesion)
        }
        binding.btnRegistro.setOnClickListener {
            val intentRegistro = Intent(this, login::class.java).apply {
                putExtra("Boton","Registro" )
            }
            startActivity(intentRegistro)
        }
        binding.btnLoginGoogle.setOnClickListener {
            val intentRegistro = Intent(this, login::class.java).apply {
                putExtra("Boton","Google" )
            }
            startActivity(intentRegistro)
        }


    }
}