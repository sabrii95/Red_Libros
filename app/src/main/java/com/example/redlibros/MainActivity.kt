package com.example.redlibros

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.example.redlibros.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
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
                R.id.nav_home, R.id.nav_cerrarSesion, R.id.nav_miCuenta
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val mail = binding.navView.getHeaderView(0).findViewById<MaterialTextView>(R.id.txt_mail)
        var username = binding.navView.getHeaderView(0).findViewById<MaterialTextView>(R.id.txt_user_name)
        var image = binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.image_user)




        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        mail.text = prefs.getString("email","")
        username.text = prefs.getString("username","")
        Glide.with(this).load(prefs.getString("image","").toString()).into(image)






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
    fun  cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, opciones_login::class.java)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.nav_cerrarSesion -> {
                Toast.makeText(
                    baseContext,
                    "Me presionaste",
                    Toast.LENGTH_LONG
                ).show()
                this.cerrarSesion()
                true
            }
            R.id.nav_miCuenta -> {
                val intent = Intent(this, MyAccount::class.java)
                startActivity(intent)
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

