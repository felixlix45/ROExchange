package com.example.roexchange;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Toolbar toolbar;
    BottomNavigationView bottomNav;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemFav:
                Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                startActivity(intent);
//                Toast.makeText(this, "Favorite Coming Soon", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.itemInfo:
                Toast.makeText(this, "Thanks ROMExchange.com for the API :)", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    toolbar.setTitle("ROExchange");
                    if(!isNetworkAvailable()){
                        Toast.makeText(getApplicationContext(), "Something is wrong with your internet connection", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.nav_et:
                    selectedFragment = new EtFragment();
                    toolbar.setTitle("ET Guides");
                    break;
                case R.id.nav_val:
                    selectedFragment = new ValFragment();
                    toolbar.setTitle("Valhalla Guides");
                    if(!isNetworkAvailable()){
                        Toast.makeText(getApplicationContext(), "Something is wrong with your internet connection", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.nav_info:
                    selectedFragment = new InfoFragment();
                    toolbar.setTitle("General Guides");
                    if(!isNetworkAvailable()){
                        Toast.makeText(getApplicationContext(), "Something is wrong with your internet connection", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        toolbar = findViewById(R.id.toolbar);
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        setSupportActionBar(toolbar);
        if(!isNetworkAvailable()){
            Toast.makeText(getApplicationContext(), "Something is wrong with your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }   
}
