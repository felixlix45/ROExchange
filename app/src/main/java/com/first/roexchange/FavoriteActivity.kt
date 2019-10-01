package com.first.roexchange

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper

import com.first.roexchange.adapter.ItemsAdapter
import com.first.roexchange.model.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.util.ArrayList

class FavoriteActivity : AppCompatActivity() {

    lateinit var savedList: ArrayList<Item>
    private lateinit var rvItem: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var itemsAdapter: ItemsAdapter
    lateinit var toolbar: Toolbar

    fun loadData() {
        val sharedPreferences = getSharedPreferences("FavoriteItem", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("task list", null)
        val type = object : TypeToken<ArrayList<Item>>() {

        }.type
        savedList = if (gson.fromJson<ArrayList<Item>>(json, type) == null) {
            ArrayList()
        } else {
            gson.fromJson<ArrayList<Item>>(json, type)
        }

        if (savedList.size == 0) {
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

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                savedList.removeAt(p0.adapterPosition)
                saveData()
                loadData()
                buildRecycleView()
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
