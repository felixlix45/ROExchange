package com.example.roexchange;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roexchange.adapter.ItemAdapter;
import com.example.roexchange.model.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    ArrayList<Item> savedList;
    RecyclerView rvItem;


    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("FavoriteItem", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Item>>() {}.getType();
        savedList = gson.fromJson(json, type);

        if(savedList == null){
            Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show();
            savedList = new ArrayList<>();
        }

    }

    public void buildRecycleView(){
        rvItem = findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new LinearLayoutManager(this));
        ItemAdapter itemAdapter = new ItemAdapter(this, savedList);
        rvItem.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
    }

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Favorites");
        loadData();
        buildRecycleView();

    }
}
