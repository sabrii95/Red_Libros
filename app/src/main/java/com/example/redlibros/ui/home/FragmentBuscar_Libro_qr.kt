package com.example.redlibros.ui.home


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.redlibros.Model.BookResponse
import com.example.redlibros.Servicio.CallApiBook
import com.example.redlibros.Servicio.ApiBooks
import com.example.redlibros.databinding.FragmentBuscarLibroQrBinding
import com.google.zxing.integration.android.IntentIntegrator
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

        initScanner()

        binding.btnSearchBook.setOnClickListener {
            initScanner()
        }
        return root
    }

    private fun initScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Mensaje al usuario")
        integrator.setTorchEnabled(true)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null){
            if(result.contents != null ){
                this.searchBook(result.contents)


            }
            else{
                binding.txtTituloLibro.setText("Cancelado")
                Toast.makeText(context, "Cancelado", Toast.LENGTH_LONG).show()

            }

        }
        else{

            Toast.makeText(context, "el resultado fue nulo", Toast.LENGTH_LONG).show()
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
    fun drawBook ( response: retrofit2.Response<BookResponse>, contexto : Context){
        val book = response?.body()
        val title: String = book?.volumeInfo?.title.toString()
        val autor: String = book?.volumeInfo?.authors.toString()
        val autor_sin_caracteres_eseciales = autor.replace("\\[\\]<>", "")
        val description: String = book?.volumeInfo?.description.toString()
        val description_sin_caractere_especiales = description.replace("\\[\\]<>", "")
        val feha: String = book?.volumeInfo?.publishedDate.toString()
        val image: String = book?.volumeInfo?.imageLinks?.smallThumbnail.toString()
        val publisher: String = book?.volumeInfo?.publisher.toString()


        binding.txtTituloLibro.setText(title.toUpperCase())
        binding.txtDescripcionLibro.setText((description_sin_caractere_especiales.toString()))
        binding.txtAutorLibro.setText(autor_sin_caracteres_eseciales.toString())
        binding.txtAOEdicion.setText(feha.toString())
        binding.txtEditorialLibro.setText(publisher)
        Glide.with(contexto).load(image).into(binding.imageTapaLibro)
        binding.constrainInfoLibro.setVisibility(View.VISIBLE)

    }
    fun searchBook(libro: String){
        val service = CallApiBook().getRetrofit().create(ApiBooks::class.java)
        val call = service.getBook(libro).enqueue(object : Callback<BookResponse> {
            override fun onResponse(
                call: Call<BookResponse>,
                response: retrofit2.Response<BookResponse>
            ) {

                drawBook(response,context!!)

            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "No se encontro libro",
                    Toast.LENGTH_SHORT
                ).show()
            }


        })

    }

}





