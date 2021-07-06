package com.example.redlibros.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.example.redlibros.MainActivity
import com.example.redlibros.R
import com.example.redlibros.User
import com.example.redlibros.databinding.FragmentDetailFragmentBinding
import com.example.redlibros.opciones_login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyAccountFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    lateinit var userdata: User
    private var _binding: FragmentDetailFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_my_account, container, false)
        _binding = FragmentDetailFragmentBinding.inflate(inflater, container, false)

        val switch_edit: Switch? = activity?.findViewById(R.id.switch_edit)
        val switch_close: Switch? = activity?.findViewById(R.id.switch_close)
        val txt_name: TextView? = activity?.findViewById(R.id.txt_name)
        val txt_username: TextView? = activity?.findViewById(R.id.txt_username)
        val txt_email: TextView? = activity?.findViewById(R.id.txt_email)
        val txt_password: TextView? = activity?.findViewById(R.id.txt_password)
        val btn_close_account: Switch? = activity?.findViewById(R.id.btn_close_account)
        val btn_save: Button? = activity?.findViewById(R.id.btn_save)
        val btn_cancel: Button? = activity?.findViewById(R.id.btn_cancel)

        /*
        val switch_edit = getActivity().findViewById<Switch>(R.id.switch_edit)
        val switch_close = getView().findViewById<Switch>(R.id.switch_close)
        val txt_name = getView().findViewById<TextView>(R.id.txt_name)
        val txt_username = getView().findViewById<TextView>(R.id.txt_username)
        val txt_email = getView().findViewById<TextView>(R.id.txt_email)
        val txt_password = getView().findViewById<TextView>(R.id.txt_password)
        val btn_close_account = getView().findViewById<Button>(R.id.btn_close_account)
        val btn_save = getView().findViewById<Button>(R.id.btn_save)
        val btn_cancel = getView().findViewById<Button>(R.id.btn_cancel)
*/
        // Cargo los datos guardados
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
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

        return inflater.inflate(R.layout.fragment_my_account, container, false)

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
                Toast.makeText(context,"Datos Guardados!", Toast.LENGTH_SHORT).show()
                //finishAffinity()
            }
            .addOnFailureListener {
                Toast.makeText(context,"Error", Toast.LENGTH_SHORT).show()
            }
    }

    fun closeAccount(user: User){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        var userRef = db.collection("User").document(user.email).set(user)
            .addOnSuccessListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, opciones_login::class.java)
                //finish()
                //finishAffinity()
            }
            .addOnFailureListener {
                Toast.makeText(context,"Error", Toast.LENGTH_SHORT).show()
            }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyAccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyAccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}