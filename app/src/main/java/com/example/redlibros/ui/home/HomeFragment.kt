package com.example.redlibros.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.redlibros.Model.BooksResponse
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.redlibros.Recycler.Item
import com.example.redlibros.Recycler.ItemAdapter
import com.example.redlibros.Servicio.ApiBooks
import com.example.redlibros.Servicio.CallApiBook
import com.example.redlibros.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback


class HomeFragment : Fragment(), ItemAdapter.ItemClickListener {

    private var book: BooksResponse ? = null
    private var _binding: FragmentHomeBinding? = null
    private lateinit  var recycler : RecyclerView
    private lateinit  var lista : MutableList<Item>



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recycler = binding.mRecyclerView

//        recycler.layoutManager = LinearLayoutManager(context)
        recycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)




        binding.btnBuscar.setOnClickListener {
            searchBook(binding.edtBuscar.text.toString())
        }
        return root
    }

    //////////////////////////////////////////
    private fun llenarLista(books: BooksResponse) {

        lista = mutableListOf()
        if (books.totalItems?.toInt() != null &&  books.totalItems?.toInt() >0) {
            for(book in books.items!!) {
           // for(book in 1..10){
             //   books.items!![book].volumeInfo?.title.toString()
                lista.add(
                Item(
                    book.volumeInfo?.title.toString(),
                    book.volumeInfo?.authors.toString(),
                    book.volumeInfo?.description.toString(),
                    book.volumeInfo?.imageLinks?.thumbnail.toString())
                )

            }

        }
        /*else{
            for(book in books.items!!) {
                Item(


                        books.items!![book].volumeInfo?.title.toString(),
                    books.items!![book].volumeInfo?.authors.toString(),
                    books.items!![book].volumeInfo?.description.toString(),
                    books.items!![book].volumeInfo?.imageLinks?.thumbnail.toString()
                )


            }

        }*/
        val adapter = ItemAdapter(this.lista, this)
        recycler.adapter = adapter
        Toast.makeText(
            context,
            "No se encontro libro"+ books.totalItems,
            Toast.LENGTH_SHORT
        ).show()

    }

    override fun onItemClick(element: Item) {
        val arg = Bundle().apply {
            putString("name",element.name)
            putString("author",element.author)
            putString("des",element.description)
            putString("url",element.url)
        }

        findNavController().navigate(com.example.redlibros.R.id.action_nav_home_to_detail_fragment,arg)

    }

    fun searchBook(libro: String){
        val service = CallApiBook().getRetrofit().create(ApiBooks::class.java)
        val call = service.getBooksByName(libro).enqueue(object : Callback<BooksResponse> {
            override fun onResponse(
                call: Call<BooksResponse>,
                response: retrofit2.Response<BooksResponse>
            ) {
                llenarLista(response?.body()!!)
            }

            override fun onFailure(call: Call<BooksResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "No se encontro libro",
                    Toast.LENGTH_SHORT
                ).show()
            }





        })

    }
    /*fun drawBook ( response: retrofit2.Response<BookResponse>){
        book  = response?.body()
        val title: String = book?.volumeInfo?.title.toString()
        val autor: String = book?.volumeInfo?.authors.toString()
        val autor_sin_caracteres_eseciales = autor.replace("\\[\\]<>", "")
        val description: String = book?.volumeInfo?.description.toString()
        val description_sin_caractere_especiales = description.replace("\\[\\]<>", "")
        val feha: String = book?.volumeInfo?.publishedDate.toString()
        val image: String = book?.volumeInfo?.imageLinks?.smallThumbnail.toString()
        val publisher: String = book?.volumeInfo?.publisher.toString()*/











}
