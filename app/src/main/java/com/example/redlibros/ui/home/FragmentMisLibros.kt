package com.example.redlibros.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redlibros.DataBase.QueryFirestore
import com.example.redlibros.R
import com.example.redlibros.Recycler.Item
import com.example.redlibros.Recycler.ItemAdapter
import com.example.redlibros.databinding.FragmentMisLibrosBinding

class FragmentMisLibros : Fragment(),ItemAdapter.ItemClickListener {
    private var _binding: FragmentMisLibrosBinding? = null
    private val binding get() = _binding!!
    private lateinit  var recycler : RecyclerView
    private lateinit  var lista : MutableList<Item>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMisLibrosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recycler = binding.mRecyclerView
        recycler.layoutManager = LinearLayoutManager(context)
        llenarLista()
        return root
    }

    private fun llenarLista() {
        lista = mutableListOf()
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val emailPref = prefs.getString("email", "")
        var book = QueryFirestore().bookforUser(emailPref.toString(), "usersPerteneciente")
        book.addOnSuccessListener { DobumentBook ->

            for (elemento in DobumentBook) {
                lista.add(
                    Item(
                        elemento.data?.get("title").toString(),
                        elemento.data?.get("authors").toString(),
                        elemento.data?.get("description").toString(),
                        elemento.data?.get("image").toString()
                    )
                )

            }
            val adapter = ItemAdapter(this.lista, this)
            recycler.adapter = adapter
        }
    }

    override fun onItemClick(element: Item) {
        val arg = Bundle().apply {
            putString("name",element.name)
            putString("author",element.author)
            putString("des",element.description)
            putString("url",element.url)
        }

        findNavController().navigate(R.id.action_fragmentMisLibros_to_detail_fragment,arg)

    }



}