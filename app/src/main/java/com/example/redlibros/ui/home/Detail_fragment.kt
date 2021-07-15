package com.example.redlibros.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redlibros.DataBase.QueryFirestore
import com.example.redlibros.Model.ImageLinks
import com.example.redlibros.Model.Notification
import com.example.redlibros.Model.VolumeInfo
import com.example.redlibros.R
import com.example.redlibros.Servicio.ApiNotification
import com.example.redlibros.Servicio.NotificationApi
import com.example.redlibros.databinding.FragmentDetailFragmentBinding
import com.example.redlibros.match.MatchSubItem
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Detail_fragment : Fragment() {
    private var id:String=""
    private var name:String=""
    private var url:String =""
    private  var author:String=""
    private var des:String=""
    private var _binding: FragmentDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private  var visible: Boolean = true
    private lateinit var array : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = arguments?.getString("id").toString()
        name = arguments?.getString("name").toString()
        url = arguments?.getString("url").toString()
        author = arguments?.getString("author").toString()
        des = arguments?.getString("des").toString()
        visible = arguments?.getBoolean("mostrar_contenido")!!
        array = arguments?.getString("array").toString()


    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailFragmentBinding.inflate(inflater, container, false)
        binding.nameTextView.text = name
        binding.authorTextView.text = "author: $author"
        binding.descriptionTextView.text = des
        if(url.isNotEmpty()) {
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.missingbook)
                .error(R.drawable.missingbook)
                .into(binding.vistaImageView)
        }

        var usersPerteneciente: List<String> = emptyList()
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val emailPref = prefs.getString("email","")
        QueryFirestore().booksforUser(emailPref.toString(), "userDeseo")
            .addOnSuccessListener{ document ->

            val bookDeseo = document.documents.filter { doc-> doc.get("title") == name }

            for (document in bookDeseo) {
                if(document.get("usersPerteneciente") != null) {
                    usersPerteneciente = document.get("usersPerteneciente") as List<String>
                }
            }

            if(usersPerteneciente.size > 0) {
                binding.subtitulo.visibility = View.VISIBLE
                binding.scrollView.layoutParams.height = 350
            }

            val userAdapter = MatchSubItem(usersPerteneciente)
            binding.rvUsers.layoutManager = LinearLayoutManager(context)
            binding.rvUsers.adapter = userAdapter


        }

        val vol = VolumeInfo(id, name, listOf(author), "", "", des, emptyList(), "", "", ImageLinks("",url,"","","",""), "")

      
        if(visible == false){
            binding.btnDeseo.setVisibility(View.GONE)
            binding.btnTengo.setVisibility(View.GONE)
            binding.btnQuitar.setVisibility(View.VISIBLE)
        }
        else{
            binding.btnDeseo.setVisibility(View.VISIBLE)
            binding.btnTengo.setVisibility(View.VISIBLE)
            binding.btnQuitar.setVisibility(View.GONE)
        }



        binding.btnDeseo.setOnClickListener {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val emailPref = prefs.getString("email","")
            QueryFirestore().addUserBook( vol, emailPref.toString(), "userDeseo", requireContext())
            sendNotificacion(id,name)

        }
        binding.btnTengo.setOnClickListener {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val emailPref = prefs.getString("email","")
            Firebase.messaging.subscribeToTopic(id)
            QueryFirestore().addUserBook( vol, emailPref.toString(), "usersPerteneciente",requireContext())

        }
        binding.btnQuitar.setOnClickListener {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val emailPref = prefs.getString("email","").toString()
            QueryFirestore().removeUser(id,emailPref,array)
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Se quito el libro de la lista",
                        Toast.LENGTH_SHORT
                    ).show()

                }

        }


        return binding.root
    }

    fun sendNotificacion(topic: String, title: String){
        val service = NotificationApi().getRetrofit().create(ApiNotification::class.java)
        val call = service.sendNotification(topic,title).enqueue(object : Callback<Notification> {
            override fun onResponse(call: Call<Notification>, response: Response<Notification>) {
                println("Hola soy la respuesta $response")
                Toast.makeText(
                    context,
                    "Se envio notificacion",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<Notification>, t: Throwable) {
                println(t)
                Toast.makeText(
                    context,
                    "No se pudo enviar notificacion",
                    Toast.LENGTH_SHORT
                ).show()
            }





        })

    }

}