package com.first.roexchange

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast


import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.first.roexchange.model.Item
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


import java.text.DecimalFormat
import java.util.ArrayList

class DetailActivity : AppCompatActivity() {

    lateinit var tvName: TextView
    lateinit var tvTypes: TextView
    lateinit var tvPrice: TextView
    lateinit var tvUpdated: TextView
    lateinit var tvLastPrice: TextView
    lateinit var toolbar: Toolbar
    internal var savedList: ArrayList<Item>? = ArrayList()
    private var mChart: LineChart? = null
    private var disableMenuOption = false

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    fun saveData() {
        val sharedPreferences = getSharedPreferences("FavoriteItem", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        val json = gson.toJson(savedList)
        editor.putString("task list", json)
        editor.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        if (intent.getStringExtra("act").contains("MainActivity")) {
            inflater.inflate(R.menu.detail_menu_item, menu)
            if(disableMenuOption){
                menu.findItem(R.id.itemAddToFav).setEnabled(false)
            }else{
                menu.findItem(R.id.itemAddToFav).setEnabled(true)
            }
        } else if (intent.getStringExtra("act").contains("FavoriteActivity")) {
            inflater.inflate(R.menu.detail_menu_item_delete, menu)
            if(disableMenuOption){
                menu.findItem(R.id.item_delete).setEnabled(false)
            }else{
                menu.findItem(R.id.item_delete).setEnabled(true)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sharedPreferences = getSharedPreferences("FavoriteItem", MODE_PRIVATE)
        val gson = Gson()

        val json = sharedPreferences.getString("task list",
                null)
        val type = object : TypeToken<ArrayList<Item>>() {

        }.type
        savedList = gson.fromJson<ArrayList<Item>>(json, type)
        if (savedList == null) {
            savedList = ArrayList()
        }
        val obj = Item()

        when (item.itemId) {

            R.id.itemAddToFav -> {
                if (savedList != null) {
                    for (i in savedList!!.indices) {
                        if (savedList!![i].name == tvName.text.toString().replace("Name : ".toRegex(), "")) {
                            Toast.makeText(this, "Already added to favorite", Toast.LENGTH_SHORT).show()
                            return true
                        }
                    }
                }
                obj.name = tvName.text.toString().replace("Name : ".toRegex(), "")
                obj.types = tvTypes.text.toString().replace("Types : ".toRegex(), "")
                savedList!!.add(obj)
                saveData()
                Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.item_delete -> {
                val i = intent.getIntExtra("pos", -1)
                if (i != -1) {
                    savedList!!.removeAt(i)
                    saveData()
                    Toast.makeText(this, "Deleted from favorite", Toast.LENGTH_SHORT).show()
                    finish()
                    //                    Toast.makeText(this, savedList.size(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "-1", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        shimmerFrameLayout = findViewById(R.id.shimmer_container_detail)

        //        Toast.makeText(this, savedList.size(), Toast.LENGTH_SHORT).show();
        tvName = findViewById(R.id.tvDetailName)
        tvTypes = findViewById(R.id.tvDetailTypes)
        tvPrice = findViewById(R.id.tvDetailPrice)
        tvUpdated = findViewById(R.id.tvDetailUpdated)
        tvLastPrice = findViewById(R.id.tvDetailBeforeUpdatePrice)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        mChart = findViewById(R.id.LineChart)

        val desc = Description()
        desc.text = ""
        mChart!!.isDragEnabled = true
        mChart!!.setScaleEnabled(false)
        mChart!!.setPinchZoom(true)
        mChart!!.setNoDataText("CLICK ME MASTER!")
        mChart!!.description = desc
        mChart!!.setDrawBorders(true)


        if (intent.hasExtra("URL") && intent.hasExtra("Types")) {
            val act = intent.getStringExtra("act").replace("@.+".toRegex(), "")
            val yValue = ArrayList<Entry>()
            val priceArray = IntArray(7)
            val URL = "https://www.romexchange.com/api?item=" + intent.getStringExtra("URL")
            shimmerFrameLayout.visibility = View.VISIBLE
            shimmerFrameLayout.startShimmer()
            disableMenuOption = true;
            val request = JsonArrayRequest(
                    Request.Method.GET,
                    URL, null,
                    Response.Listener { response ->
                        shimmerFrameLayout.visibility = View.GONE
                        shimmerFrameLayout.stopShimmer()
                        disableMenuOption = false
                        invalidateOptionsMenu()
                        try {
                            val formatter = DecimalFormat("#,###,###")
                            for (i in 0 until response.length()) {

                                tvName.text = "Name : " + response.getJSONObject(i).get("name").toString()
                                tvTypes.text = "Types : " + intent.getStringExtra("Types")
                                tvPrice.text = "Current Price : " + formatter.format(response.getJSONObject(i).getJSONObject("sea").get("latest")) + " zeny"

                                val jsonArray = response.getJSONObject(i).getJSONObject("sea").getJSONObject("week").getJSONArray("data")
                                for (j in 0 until jsonArray.length()) {
                                    priceArray[j] = jsonArray.getJSONObject(j).getInt("price")
                                    yValue.add(Entry(j.toFloat() + 1, priceArray[j].toFloat()))
                                }

                                tvLastPrice.text = "Previous Price : " + formatter.format(jsonArray.getJSONObject(5).getInt("price").toLong()) + " zeny"
                                val date = response.getJSONObject(i).getJSONObject("sea").get("latest_time").toString().replace("T".toRegex(), " ").replace("Z".toRegex(), " GMT/UTC Time")
                                if (date != "") {
                                    tvUpdated.text = "Last Updated : $date"
                                } else {
                                    tvUpdated.text = "Last Updated : " + response.getJSONObject(i).getJSONObject("sea").get("latest_time").toString()
                                }
                                val set1 = LineDataSet(yValue, "SEA Server")

                                set1.fillAlpha = 200
                                set1.color = Color.BLUE
                                set1.lineWidth = 2.5f
                                set1.setCircleColor(Color.BLUE)
                                set1.circleHoleColor = Color.BLUE
                                set1.circleRadius = 5f
                                set1.notifyDataSetChanged()

                                val dataSets = ArrayList<ILineDataSet>()
                                dataSets.add(set1)

                                val data = LineData(dataSets)
                                mChart!!.data = data
                                supportActionBar!!.setTitle(tvName.text.toString().replace("Name : ".toRegex(), ""))
                            }

                        } catch (e: Exception) {

                        }
                    },
                    Response.ErrorListener {
                        shimmerFrameLayout.visibility = View.GONE
                        shimmerFrameLayout.stopShimmer()
                        Toast.makeText(this@DetailActivity, "Error while fetching data", Toast.LENGTH_SHORT).show()
                    }
            )
            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(request)
        }

    }

}
