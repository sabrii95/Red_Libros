package com.example.redlibros.ui.home

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.example.redlibros.User
import com.example.redlibros.databinding.FragmentCountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


class FragmentCount : Fragment() {
    private val db = FirebaseFirestore.getInstance()

    lateinit var userdata: User

    companion object {
        val TAG = "Change Account";
        const val CAMERA = 1001;
        const val GALLERY = 1002;
    }

    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mProgressDialog: ProgressDialog
    private val REQUEST_PERMISSION = 100
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
        val txt_pass = binding.txtPass

        val btn_close_account =binding.btnCloseAccount
        val btn_save = binding.btnSave
        val btn_cancel = binding.btnCancel

        val btn_photo = binding.btnCamera
        val btn_gallery = binding.btnGallery
        var image = binding.Image



        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        txt_name.setText(prefs.getString("fullname", ""))
        txt_email.setText(prefs.getString("email", ""))
        txt_username.setText(prefs.getString("userName", ""))
        txt_pass.setText(prefs.getString("pass", ""))

        Glide.with(this).load(prefs.getString("image",""))
            .fitCenter()
            .centerCrop()
            .into(image)

        // Email no puede ser editado.
        txt_email.isEnabled = false
        btn_photo.isEnabled = false
        btn_gallery.isEnabled = false
        btn_close_account.isEnabled = false

        switch_edit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // El switch esta activo para edición
                txt_name.isEnabled = true
                txt_username.isEnabled = true
                txt_pass.isEnabled = true

                btn_photo.isEnabled = true
                btn_gallery.isEnabled = true

                btn_save.isEnabled = true
                btn_cancel.isEnabled = true
            } else {
                txt_name.isEnabled = false
                txt_username.isEnabled = false
                txt_pass.isEnabled = false

                btn_photo.isEnabled = false
                btn_gallery.isEnabled = false

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
                image=prefs.getString("image","").toString(),
                userName=txt_username.text.toString(),
                pass=txt_pass.text.toString(),
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
                image=prefs.getString("image","").toString(),
                userName=txt_username.text.toString(),
                pass=txt_pass.text.toString(),
                fullname=txt_name.text.toString(),
            )
            this.closeAccount(userdata)
        }

        // Botón Subir foto desde galeria
        btn_gallery.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY)
        }

        // Botón Subir foto desde camara
        btn_photo.setOnClickListener{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun checkCameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION)
        }
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var image = binding.Image

        if (requestCode == GALLERY && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Photo was selected")

            val imageFileName = "/profile/pic${System.currentTimeMillis()}.png"
            val filePath: Uri = data!!.getData()!!

            mProgressDialog = ProgressDialog(context)
            mProgressDialog.setMessage("Subiendo imagen...")
            mProgressDialog.show()

            val uploadTask = mStorageRef.child(imageFileName).putFile(filePath)

            uploadTask.addOnSuccessListener{
                Log.e(TAG, "La imagen se subio correctamente.")
                val downloadURLTask = mStorageRef.child(imageFileName).downloadUrl
                downloadURLTask.addOnSuccessListener{
                    Log.e(TAG, "IMAGE TAG: $it")
                    val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
                    var datosusuario = prefs.edit()
                    datosusuario.putString("image", "$it")
                    datosusuario.apply()
                    userdata = User(
                        email=binding.txtEmail.text.toString(),
                        enable=true,
                        image="$it",
                        userName=binding.txtUsername.text.toString(),
                        pass=binding.txtPass.text.toString(),
                        fullname=binding.txtName.text.toString(),
                    )
                    var userRef = db.collection("User").document(userdata.email).set(userdata)

                    Glide.with(this).load(it)
                        .fitCenter()
                        .centerCrop()
                        .into(image)
                    mProgressDialog.dismiss()
                }.addOnFailureListener{
                    mProgressDialog.dismiss()
                }
            }.addOnFailureListener{
                Log.e(TAG, "Hubo un error al subír la imagen ${it.printStackTrace()}")
            }
        }

        if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Se saco la foto")

            val imageFileName = "/profile/pic${System.currentTimeMillis()}.png"

            mProgressDialog = ProgressDialog(context)
            mProgressDialog.setMessage("Subiendo imagen...")
            mProgressDialog.show()

            val bitmap = data?.extras?.get("data") as Bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val uploadTask = mStorageRef.child(imageFileName).putBytes(data)


            uploadTask.addOnSuccessListener{
                Log.e(TAG, "La imagen se subio correctamente.")
                val downloadURLTask = mStorageRef.child(imageFileName).downloadUrl
                downloadURLTask.addOnSuccessListener{
                    Log.e(TAG, "IMAGE TAG: $it")
                    val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
                    var datosusuario = prefs.edit()
                    datosusuario.putString("image", "$it")
                    datosusuario.apply()
                    userdata = User(
                        email=binding.txtEmail.text.toString(),
                        enable=true,
                        image="$it",
                        userName=binding.txtUsername.text.toString(),
                        pass=binding.txtPass.text.toString(),
                        fullname=binding.txtName.text.toString(),
                    )

                    var userRef = db.collection("User").document(userdata.email).set(userdata)

                    Glide.with(this).load(it)
                        .fitCenter()
                        .centerCrop()
                        .into(image)
                    mProgressDialog.dismiss()
                }.addOnFailureListener{
                    mProgressDialog.dismiss()
                }
            }.addOnFailureListener{
                Log.e(TAG, "Hubo un error al subír la imagen ${it.printStackTrace()}")
            }

        }
    }


    // Cambio entre modo edición y no edición de close account
    fun editInfo(user: User){
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        var userRef = db.collection("User").document(user.email).set(user)
            .addOnSuccessListener {

                    var datosusuario = prefs.edit()
                    datosusuario.putString("userName", user.userName)
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