package com.example.redlibros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.redlibros.databinding.FragmentCountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentCount : Fragment() {
    private val db = FirebaseFirestore.getInstance()

    lateinit var userdata: User


    private var _binding: FragmentCountBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val switch_edit = binding.switchEdit
        val switch_close = binding.switchClose
        val txt_name = binding.txtName
        val txt_username = binding.txtUsername
        val txt_email = binding.txtEmail

        val btn_close_account =binding.btnCloseAccount
        val btn_save = binding.btnSave
        val btn_cancel = binding.btnCancel

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        txt_name.setText(prefs.getString("name", ""))
        txt_email.setText(prefs.getString("email", ""))
        txt_username.setText(prefs.getString("username", ""))

        // Email no puede ser editado.
        txt_email.isEnabled = false

        switch_edit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // El switch esta activo para edición
                txt_name.isEnabled = true
                txt_username.isEnabled = true

                btn_save.isEnabled = true
                btn_cancel.isEnabled = true
            } else {
                txt_name.isEnabled = false
                txt_username.isEnabled = false

                btn_save.isEnabled = false
                btn_cancel.isEnabled = false
            }
        }
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
                pass= "",
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
                pass= "",
                fullname=txt_name.text.toString(),
            )
            this.closeAccount(userdata)
        }



        return root
    }
    // Cambio entre modo edición y no edición de close account


fun editInfo(user: User){
    val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
    var userRef = db.collection("User").document(user.email).set(user)
        .addOnSuccessListener {

                var datosusuario = prefs.edit()
                datosusuario.putString("username", user.userName)
                datosusuario.putString("image", user.image)
                datosusuario.putString("pass", user.pass)
                datosusuario.putString("fullname", user.fullname)
                datosusuario.apply()

        }
        .addOnFailureListener {
            Toast.makeText(context,"Error", Toast.LENGTH_SHORT).show()
        }
}

fun closeAccount(user: User){
    val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
    var userRef = db.collection("User").document(user.email).set(user)
        .addOnSuccessListener {
            FirebaseAuth.getInstance().signOut()
            /*val intent = Intent(this, opciones_login::class.java)
            finish()*/
        }
        .addOnFailureListener {
            Toast.makeText(context,"Error", Toast.LENGTH_SHORT).show()
        }
}


}