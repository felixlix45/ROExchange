package com.example.roexchange

import android.content.SharedPreferences
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.example.roexchange.adapter.ItemAdapter
import com.example.roexchange.model.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type
import java.util.ArrayList
import java.util.LinkedHashSet
import java.util.stream.Collectors

class FavoriteActivity : AppCompatActivity() {

    lateinit var savedList: ArrayList<Item>
    internal var hashSet: LinkedHashSet<Item>? = null
    lateinit var rvItem: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var toolbar: Toolbar

    fun loadData() {
        val sharedPreferences = getSharedPreferences("FavoriteItem", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("task list", null)
        val type = object : TypeToken<ArrayList<Item>>() {

        }.type
        savedList = gson.fromJson<ArrayList<Item>>(json, type)

        if (savedList == null || savedList!!.size == 0) {
            //            Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show();
            savedList = ArrayList()
        }

    }

    fun buildRecycleView() {
        rvItem = findViewById(R.id.rvItem)
        rvItem.layoutManager = LinearLayoutManager(this)
        val itemAdapter = ItemAdapter(this, savedList)
        rvItem.adapter = itemAdapter
        itemAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        swipeRefreshLayout = findViewById(R.id.swipeContainer)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Favorites"
        loadData()
        buildRecycleView()

        swipeRefreshLayout.setOnRefreshListener {
            loadData()
            buildRecycleView()
            swipeRefreshLayout.isRefreshing = false
        }

    }

    override fun onRestart() {
        super.onRestart()
        loadData()
        buildRecycleView()
    }
}
