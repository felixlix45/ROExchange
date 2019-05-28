package com.example.roexchange;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.roexchange.adapter.ItemAdapter;
import com.example.roexchange.model.Item;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


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
                Toast.makeText(this, "Favorite Coming Soon", Toast.LENGTH_SHORT).show();
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
                case R.id.nav_info:
                    selectedFragment = new InfoFragment();
                    toolbar.setTitle("Guides");
                    if(!isNetworkAvailable()){
                        Toast.makeText(getApplicationContext(), "Something is wrong with your internet connection", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.nav_more:
                    selectedFragment = new MoreFragment();
                    toolbar.setTitle("Valhalla Guides");
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
