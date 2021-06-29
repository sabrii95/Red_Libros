package com.example.redlibros.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.redlibros.Model.BookResponse
import com.example.redlibros.Model.Response
import com.example.redlibros.R
import com.example.redlibros.Servicio.CallApiBook
import com.example.redlibros.Servicio.ApiBooks
import com.example.redlibros.databinding.FragmentBuscarLibroQrBinding
import retrofit2.Call
import retrofit2.Callback



class FragmentBuscar_Libro_qr : Fragment() {


    private var _binding: FragmentBuscarLibroQrBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding = FragmentBuscarLibroQrBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val service = CallApiBook().getRetrofit().create(ApiBooks::class.java)
        val call = service.getBook("2zgRDXFWkm8C").enqueue(object : Callback<BookResponse> {
            override fun onResponse(
                call: Call<BookResponse>,
                response: retrofit2.Response<BookResponse>
            ) {
                val book = response?.body()
                val title: String = book?.volumeInfo?.title.toString()
                val autor: String = book?.volumeInfo?.authors.toString()
                val description: String = book?.volumeInfo?.description.toString()
                val feha: String = book?.volumeInfo?.publishedDate.toString()
                val image: String = book?.volumeInfo?.imageLinks?.smallThumbnail.toString()
                val publisher: String = book?.volumeInfo?.publisher.toString()
                binding.constrainInfoLibro.setVisibility(View.VISIBLE)


                binding.txtTituloLibro.setText(title.toUpperCase())
                binding.txtDescripcionLibro.setText((description.toString()))
                binding.txtAutorLibro.setText(autor.toString())
                binding.txtAOEdicion.setText(feha.toString())
                binding.txtEditorialLibro.setText(publisher)
                Glide.with(context!!).load(image).into(binding.imageTapaLibro)
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "No se encontro libro",
                    Toast.LENGTH_SHORT
                ).show()
            }


        })
        return root
    }

}





