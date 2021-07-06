package com.example.redlibros

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.example.redlibros.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var fusedLocationClient: FusedLocationProviderClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


        drawerLayout = binding.drawerLayout

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.appBarMain.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)


        val navView: NavigationView = binding.navView
        navView.setNavigationItemSelectedListener {
            Toast.makeText(this@MainActivity, "clicked", Toast.LENGTH_SHORT).show()
            false
        }
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(

                R.id.nav_home,R.id.nav_fragmentCount, R.id.nav_BuscarLibroQR, R.id.nav_LibrosDeseos

            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
        val mail = binding.navView.getHeaderView(0).findViewById<MaterialTextView>(R.id.txt_mail)
        var username = binding.navView.getHeaderView(0).findViewById<MaterialTextView>(R.id.txt_user_name)
        var image = binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.image_user)


        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        mail.text = prefs.getString("email","")
        username.text = prefs.getString("username","")
        Glide.with(this).load(prefs.getString("image","").toString()).into(image)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checLocationpermission()



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu )
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun  cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, opciones_login::class.java)

        startActivity(intent)
        finishAffinity()


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.nav_BuscarLibroQR->{
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_BuscarLibroQR)
            }
            R.id.nav_home->{
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_home)
            }
            R.id.nav_cerrarSesion -> {
                this.cerrarSesion()

            }
            R.id.nav_LibrosDeseos->{
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_LibrosDeseos)
            }
            R.id.nav_fragmentMisLibros->{
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_fragmentMisLibros)
            }
            R.id.nav_fragmentCount -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_fragmentCount)
                /*val intent = Intent(this, MyAccount::class.java)
                startActivity(intent)
                finishAffinity()
                true*/
            }
            else -> super.onOptionsItemSelected(item)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun checLocationpermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            //El permiso no está aceptado.
            requestLocationPermission()
        }
    }
    fun requestLocationPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            //El usuario ya ha rechazado el permiso anteriormente, debemos informarle que vaya a ajustes.
            Toast.makeText(this, "El usuario ya ha rechazado lo permisos", Toast.LENGTH_SHORT).show()
        } else {
            //El usuario nunca ha aceptado ni rechazado, así que le pedimos que acepte el permiso.
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==0){
            if(grantResults.isNotEmpty() &&  grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                }


            }

                    Toast.makeText(this, "Permiso cencedido", Toast.LENGTH_SHORT).show()

        }
        else{
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }

    }





}

