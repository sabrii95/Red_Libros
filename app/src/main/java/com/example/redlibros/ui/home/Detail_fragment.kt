package com.example.redlibros.ui.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redlibros.DataBase.QueryFirestore
import com.example.redlibros.Model.ImageLinks
import com.example.redlibros.Model.VolumeInfo
import com.example.redlibros.R
import com.example.redlibros.databinding.FragmentDetailFragmentBinding
import com.example.redlibros.match.MatchSubItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_match.*


class Detail_fragment : Fragment() {
    private var name:String=""
    private var url:String =""
    private  var author:String=""
    private var des:String=""
    private var _binding: FragmentDetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = arguments?.getString("name").toString()
        url = arguments?.getString("url").toString()
        author = arguments?.getString("author").toString()
        des = arguments?.getString("des").toString()

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
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.missingbook)
            .error(R.drawable.missingbook)
            .into(binding.vistaImageView)

        var usersPerteneciente: List<String> = emptyList()
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val emailPref = prefs.getString("email","")
        QueryFirestore().bookforUser(emailPref.toString(), "userDeseo").addOnSuccessListener{ document ->

            val bookDeseo = document.documents.filter { doc-> doc.get("title") == name }

            for (document in bookDeseo) {
                usersPerteneciente = document.get("usersPerteneciente") as List<String>
            }

            if(usersPerteneciente.size > 0) {
                binding.subtitulo.visibility = View.VISIBLE
                binding.scrollView.layoutParams.height = 350
            }

            val userAdapter = MatchSubItem(usersPerteneciente)
            binding.rvUsers.layoutManager = LinearLayoutManager(context)
            binding.rvUsers.adapter = userAdapter


        }

        val vol = VolumeInfo(name, listOf(author), "", "", des, emptyList(), "", "", ImageLinks("","","","","",""), "")

        binding.btnDeseo.setOnClickListener {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val emailPref = prefs.getString("email","")
            QueryFirestore().addUserBook( vol, emailPref.toString(), "userDeseo")

        }
        binding.btnTengo.setOnClickListener {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val emailPref = prefs.getString("email","")
            QueryFirestore().addUserBook( vol, emailPref.toString(), "usersPerteneciente")

        }


        return binding.root
    }


}