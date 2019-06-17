package com.first.roexchange

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper

import com.first.roexchange.adapter.ItemsAdapter
import com.first.roexchange.model.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.util.ArrayList
import java.util.LinkedHashSet

class FavoriteActivity : AppCompatActivity() {

    lateinit var savedList: ArrayList<Item>
    internal var hashSet: LinkedHashSet<Item>? = null
    lateinit var rvItem: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var itemsAdapter: ItemsAdapter
    lateinit var toolbar: Toolbar

    fun loadData() {
        val sharedPreferences = getSharedPreferences("FavoriteItem", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("task list", null)
        val type = object : TypeToken<ArrayList<Item>>() {

        }.type
        if(gson.fromJson<ArrayList<Item>>(json, type) == null){
            savedList = ArrayList()
        }else{
            savedList = gson.fromJson<ArrayList<Item>>(json, type)
        }

        if (savedList == null || savedList!!.size == 0) {
            //            Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show();
            savedList = ArrayList()
        }

    }

    fun buildRecycleView() {
        rvItem = findViewById(R.id.rvItem)
        rvItem.layoutManager = LinearLayoutManager(this)
        itemsAdapter = ItemsAdapter(this, savedList)
        rvItem.adapter = itemsAdapter
        itemsAdapter.notifyDataSetChanged()
    }

    fun saveData() {
        val sharedPreferences = getSharedPreferences("FavoriteItem", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        val json = gson.toJson(savedList)
        editor.putString("task list", json)
        editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        swipeRefreshLayout = findViewById(R.id.swipeContainer)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Favorites"
        loadData()
        buildRecycleView()

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                savedList!!.removeAt(p0.adapterPosition)
                saveData();
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(rvItem)


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
