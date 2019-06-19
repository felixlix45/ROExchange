package com.first.roexchange

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.github.chrisbanes.photoview.PhotoView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var bottomNav: BottomNavigationView

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


        val currFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

//        if(selectedFragment == HomeFragment()){
//            if (HomeFragment().isHidden){
//                supportFragmentManager.beginTransaction().show(HomeFragment()).commit()
//            }else{
//                supportFragmentManager.beginTransaction().add(R.id.fragment_container, selectedFragment).add(R.id.fragment_container, currFragment!!).hide(currFragment).commit()
//
//            }
//        }else if (selectedFragment == EtFragment()){
//            if (EtFragment().isHidden){
//                supportFragmentManager.beginTransaction().show(EtFragment()).commit()
//            }else{
//                supportFragmentManager.beginTransaction().add(R.id.fragment_container, selectedFragment).add(R.id.fragment_container, currFragment!!).hide(currFragment).commit()
//
//            }
//        }else if(selectedFragment == ValFragment()){
//            if (ValFragment().isHidden){
//                supportFragmentManager.beginTransaction().show(ValFragment()).commit()
//            }else{
//                supportFragmentManager.beginTransaction().add(R.id.fragment_container, selectedFragment).add(R.id.fragment_container, currFragment!!).hide(currFragment).commit()
//
//            }
//        }else if(selectedFragment == InfoFragment()){
//            if (InfoFragment().isHidden){
//                supportFragmentManager.beginTransaction().show(InfoFragment()).commit()
//            }else{
//                supportFragmentManager.beginTransaction().add(R.id.fragment_container, selectedFragment!!).hide(currFragment!!).commit()
//
//            }
//        }

        if(selectedFragment!!.isHidden){
            supportFragmentManager.beginTransaction().show(selectedFragment).commit()
        }else{
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, selectedFragment).hide(currFragment!!).commit()
        }
//        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()
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
                Toast.makeText(this, "Outdated image/data will be deleted or changed with 'updated soon' image!", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }
//            R.id.serverChange ->{
////                Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show()
//                if(item.isChecked){
//                    item.setChecked(false)
//                }else{
//                    item.setChecked(true)
//                }
//                return super.onOptionsItemSelected(item)
//            }
            else -> return super.onOptionsItemSelected(item)
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

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        bottomNav = findViewById(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, HomeFragment()).commit()
        setSupportActionBar(toolbar)

        if (!isNetworkAvailable) {
            Toast.makeText(applicationContext, "Something is wrong with your internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private val TAG = "MainActivity"
    }
}
