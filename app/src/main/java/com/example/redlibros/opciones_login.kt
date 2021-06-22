package com.example.redlibros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.redlibros.databinding.ActivityMainBinding
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


    }
}