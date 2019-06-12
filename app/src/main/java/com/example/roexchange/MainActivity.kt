package com.example.roexchange

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var bottomNav: BottomNavigationView

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        var selectedFragment: Fragment? = null
        when (menuItem.itemId) {
            R.id.nav_home -> {
                selectedFragment = HomeFragment()
                toolbar.title = "ROExchange"
                if (!isNetworkAvailable) {
                    Toast.makeText(applicationContext, "Something is wrong with your internet connection", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.nav_et -> {
                selectedFragment = EtFragment()
                toolbar.title = "ET Guides"
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
        when (item.itemId) {
            R.id.itemFav -> {
                val intent = Intent(applicationContext, FavoriteActivity::class.java)
                startActivity(intent)
                //                Toast.makeText(this, "Favorite Coming Soon", Toast.LENGTH_SHORT).show();
                return true
            }
            R.id.itemInfo -> {
                Toast.makeText(this, "Thanks ROMExchange.com for the API :)", Toast.LENGTH_SHORT).show()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }

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
        private val TAG = "MainActivity"
    }
}
