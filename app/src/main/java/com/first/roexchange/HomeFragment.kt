package com.first.roexchange

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.first.roexchange.adapter.ItemsAdapter
import com.first.roexchange.model.Item
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.fragment_home.*

import java.util.ArrayList

class HomeFragment : Fragment() {

    internal var listItem = ArrayList<Item>()

    internal var URL = "https://www.romexchange.com/api/items.json"

    lateinit var itemsAdapter: ItemsAdapter
    lateinit var spinnerFilter : Spinner
    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_home, container, false)

        itemsAdapter = ItemsAdapter(requireActivity(), listItem)

        shimmerFrameLayout = v.findViewById<ShimmerFrameLayout>(R.id.shimmer_container)
        shimmerFrameLayout.startShimmer()

        var btnSearch = v.findViewById(R.id.btnSearch) as Button
        var btnReset = v.findViewById(R.id.reset)as Button
        var etSearch = v.findViewById(R.id.etSearchItem)as EditText

        var rvItem = v.findViewById(R.id.rvItem) as RecyclerView
        rvItem.layoutManager = LinearLayoutManager(activity)
        rvItem.adapter = itemsAdapter

        var cbFilter = v.findViewById(R.id.cbFilter) as CheckBox
        spinnerFilter = v.findViewById(R.id.spinnerFilter) as Spinner

        btnSearch.setOnClickListener {
            shimmerFrameLayout.visibility = View.VISIBLE
            shimmerFrameLayout.startShimmer()
            val name = etSearch.text.toString().replace("\\s+".toRegex(), "%20")
            if (etSearch.text.toString() != "") {
                itemsAdapter.clear()
                if (!cbFilter.isChecked) {
                    val request = JsonArrayRequest(
                            Request.Method.GET,
                            "https://www.romexchange.com/api?exact=false&item=$name",
                            null,
                            Response.Listener { response ->
                                shimmerFrameLayout.visibility = View.GONE
                                try {
                                    for (i in 0 until response.length()) {
                                        val item = Item()
                                        item.name = response.getJSONObject(i).get("name").toString()
                                        item.types = typeConvert(response.getJSONObject(i).getInt("type"))
                                        listItem.add(item)
                                    }
                                    itemsAdapter.notifyDataSetChanged()
                                } catch (e: Exception) {
                                    Log.d(TAG, e.toString())
                                }
                            },
                            Response.ErrorListener {
                                shimmerFrameLayout.visibility = View.GONE
                                Toast.makeText(activity, "Error occured", Toast.LENGTH_SHORT).show()
                            }
                    )
                    val requestQueue = Volley.newRequestQueue(activity!!)
                    requestQueue.add(request)
                } else {
                    getFilteredData2()
                }

            } else {
                Toast.makeText(activity, "Search can't be empty, retrieving all data", Toast.LENGTH_SHORT).show()
                getAllData()
            }
            etSearch.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        cbFilter.setOnCheckedChangeListener { buttonView, isChecked ->
            if (cbFilter.isChecked) {
                spinnerFilter.visibility = View.VISIBLE
                getFilteredData()
            } else {
                spinnerFilter.visibility = View.INVISIBLE
                getAllData()
            }
        }

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                getFilteredData()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        // Keyboard Search Click
        etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                btnSearch.performClick()
                return@OnEditorActionListener true
            }
            false
        })

        btnReset.setOnClickListener {
            itemsAdapter.clear()
            cbFilter.isChecked = false
            etSearch.setText("")
            etSearch.onEditorAction(EditorInfo.IME_ACTION_DONE)
            getAllData()
        }

        if (itemsAdapter.size() == 0) {
            getAllData()
        }
        return v
    }

    fun getFilteredData() {
        itemsAdapter.clear()
        val pos = spinnerFilter.selectedItemPosition + 1
        val URLFiltered = "https://www.romexchange.com/api/items.json"//exact=false&item=" + etSearch.getText().toString().replaceAll("\\s+","%20") + "&type=" + pos;
        //        progressBar.setVisibility(View.VISIBLE);
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmer()
        val request = JsonArrayRequest(
                Request.Method.GET,
                URLFiltered, null,
                Response.Listener { response ->
                    //                        progressBar.setVisibility(View.GONE);
                    shimmerFrameLayout.visibility = View.GONE
                    try {

                        for (i in 0 until response.length()) {
                            if (response.getJSONObject(i).getInt("type") == pos) {
                                val item = Item()
                                item.name = response.getJSONObject(i).get("name").toString()
                                item.types = typeConvert(response.getJSONObject(i).getInt("type"))
                                listItem.add(item)
                            }

                        }
                        itemsAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {


                    }
                },
                Response.ErrorListener {
                    //                        progressBar.setVisibility(View.GONE);
                    shimmerFrameLayout.visibility = View.GONE

                }
        )

        val requestQueue = Volley.newRequestQueue(activity!!)
        requestQueue.add(request)
    }

    fun getFilteredData2() {
        itemsAdapter.clear()
        val pos = spinnerFilter.selectedItemPosition + 1
        val URLFiltered = "https://www.romexchange.com/api/items.json"//exact=false&item=" + etSearch.getText().toString().replaceAll("\\s+","%20") + "&type=" + pos;
        //        progressBar.setVisibility(View.VISIBLE);
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmer()
        val request = JsonArrayRequest(
                Request.Method.GET,
                URLFiltered, null,
                Response.Listener { response ->
                    //                        progressBar.setVisibility(View.GONE);
                    shimmerFrameLayout.visibility = View.GONE
                    try {

                        for (i in 0 until response.length()) {
                            if (response.getJSONObject(i).getInt("type") == pos) {
                                val item = Item()
                                val name = response.getJSONObject(i).get("name").toString().toLowerCase().trim()
                                if(name.contains(etSearchItem.text.toString().toLowerCase().trim(), false)){
                                    item.name = response.getJSONObject(i).get("name").toString()
                                    item.types = typeConvert(response.getJSONObject(i).getInt("type"))
                                    listItem.add(item)
                                }
                            }

                        }
                        itemsAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {


                    }
                },
                Response.ErrorListener {
                    //                        progressBar.setVisibility(View.GONE);
                    shimmerFrameLayout.visibility = View.GONE

                }
        )

        val requestQueue = Volley.newRequestQueue(activity!!)
        requestQueue.add(request)
    }

    fun getAllData() {
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmer()
        itemsAdapter.clear()
        val request = JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                Response.Listener { response ->
                    shimmerFrameLayout.visibility = View.GONE
                    try {
                        for (i in 0 until response.length()) {
                            val item = Item()
                            item.name = response.getJSONObject(i).get("name").toString()
                            item.types = typeConvert(response.getJSONObject(i).getInt("type"))
                            listItem.add(item)
                        }
                        itemsAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {

                    }
                },
                Response.ErrorListener {

                }
        )

        val requestQueue = Volley.newRequestQueue(activity!!)
        requestQueue.add(request)
    }

    fun typeConvert(type: Int): String {
        when (type) {
            1 -> return "Weapon"
            2 -> return "Off-hand"
            3 -> return "Armors"
            4 -> return "Garments"
            5 -> return "Footgears"
            6 -> return "Accessory"
            7 -> return "Blueprint"
            8 -> return "Potion / Effect"
            9 -> return "Refine"
            10 -> return "Scroll / Album"
            11 -> return "Material"
            12 -> return "Holiday Material"
            13 -> return "Pet Material"
            14 -> return "Premium"
            15 -> return "Costume"
            16 -> return "Head"
            17 -> return "Face"
            18 -> return "Back"
            19 -> return "Mouth"
            20 -> return "Tail"
            21 -> return "Weapon Card"
            22 -> return "Off-Hand Card"
            23 -> return "Armor Card"
            24 -> return "Garments Card"
            25 -> return "Shoe Card"
            26 -> return "Accessory Card"
            27 -> return "Headwear Card"
        }
        return "NULL"
    }

    companion object {

        private val TAG = "HomeFragment"
    }
}
