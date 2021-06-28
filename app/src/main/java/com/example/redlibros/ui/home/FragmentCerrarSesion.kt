package com.example.redlibros.ui.home

import android.content.Intent
import android.content.Intent.makeRestartActivityTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.IntentCompat
import androidx.fragment.app.Fragment
import com.example.redlibros.R
import com.example.redlibros.opciones_login
import com.google.firebase.auth.FirebaseAuth


class FragmentCerrarSesion : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(context, opciones_login::class.java)
        startActivity(intent)
        getActivity()?.finishAffinity()








    return inflater.inflate(R.layout.fragment_cerrar_sesion, container, false)
    }



}