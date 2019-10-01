package com.first.roexchange

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    private lateinit var bottomNav: BottomNavigationView

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        var selectedFragment: Fragment? = null
        when (menuItem.itemId) {
            R.id.nav_home -> {
                selectedFragment = HomeFragment()
                toolbar.title = "RO M Guides and Exchange"
                if (!isNetworkAvailable) {
                    Toast.makeText(applicationContext, "Something is wrong with your internet connection", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.nav_et -> {
                selectedFragment = EtFragment()
                toolbar.title = "ET Guides"
                if (!isNetworkAvailable) {
                    Toast.makeText(applicationContext, "Something is wrong with your internet connection", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.nav_val -> {
                selectedFragment = ValFragment()
                toolbar.title = "Valhalla Guides"
                if (!isNetworkAvailable) {
                    Toast.makeText(applicationContext, "Something is wrong with your internet connection", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.nav_info -> {
                selectedFragment = InfoFragment()
                toolbar.title = "General Guides"
                if (!isNetworkAvailable) {
                    Toast.makeText(applicationContext, "Something is wrong with your internet connection", Toast.LENGTH_SHORT).show()
                }
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()

        true
    }

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemFav -> {
                val intent = Intent(applicationContext, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.itemInfo -> {
                Toast.makeText(this, "Outdated image/data will be deleted or changed with 'updated soon' image!", Toast.LENGTH_LONG).show()
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if(doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        bottomNav = findViewById(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
        setSupportActionBar(toolbar)

        if (!isNetworkAvailable) {
            Toast.makeText(applicationContext, "Something is wrong with your internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
    }
}
