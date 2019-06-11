package com.example.roexchange

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.roexchange.R
import com.example.roexchange.adapter.ItemAdapter
import com.example.roexchange.model.Item
import com.facebook.shimmer.ShimmerFrameLayout

import org.json.JSONArray

import java.util.ArrayList

class HomeFragment : Fragment() {

//    internal var btnSearch: Button?;
//    internal var btnReset: Button? = null
//    internal var etSearch: EditText? = null
//    internal var rvItem: RecyclerView = RecyclerView();
    internal var listItem = ArrayList<Item>()

//    internal var cbFilter: CheckBox? = null
//    internal var spinnerFilter: Spinner? = null
    internal var URL = "https://www.romexchange.com/api/items.json"

//    private var itemAdapter = ItemAdapter(requireActivity(), listItem);
    lateinit var itemAdapter: ItemAdapter;
    lateinit var spinnerFilter : Spinner
    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_home, container, false)
        itemAdapter = ItemAdapter(requireActivity(),listItem);
        shimmerFrameLayout = v.findViewById<ShimmerFrameLayout>(R.id.shimmer_container)
        shimmerFrameLayout.startShimmer()
        var btnSearch = v.findViewById(R.id.btnSearch) as Button
        var btnReset = v.findViewById(R.id.reset)as Button
        var etSearch = v.findViewById(R.id.etSearchItem)as EditText

        var rvItem = v.findViewById(R.id.rvItem) as RecyclerView
        rvItem.layoutManager = LinearLayoutManager(activity)
        rvItem.adapter = itemAdapter

        var cbFilter = v.findViewById(R.id.cbFilter) as CheckBox
        spinnerFilter = v.findViewById(R.id.spinnerFilter) as Spinner

        btnSearch.setOnClickListener {
            shimmerFrameLayout.visibility = View.VISIBLE
            shimmerFrameLayout.startShimmer()
            val name = etSearch.text.toString().replace("\\s+".toRegex(), "%20")
            if (etSearch.text.toString() != "") {
                itemAdapter.clear()
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
                                    itemAdapter.notifyDataSetChanged()
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
                    getFilteredData()
                }

            } else {

                Toast.makeText(activity, "Null", Toast.LENGTH_SHORT).show()
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
            itemAdapter.clear()
            cbFilter.isChecked = false
            etSearch.setText("")
            etSearch.onEditorAction(EditorInfo.IME_ACTION_DONE)
            getAllData()
        }

        if (itemAdapter.size() == 0) {
            getAllData()
        }
        return v
    }

    fun getFilteredData() {
        itemAdapter.clear()
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
                        itemAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {


                    }
                },
                Response.ErrorListener {
                    //                        progressBar.setVisibility(View.GONE);
                    shimmerFrameLayout.visibility = View.GONE
                    Toast.makeText(activity, "Check your internet connection and try again", Toast.LENGTH_SHORT).show()
                }
        )

        val requestQueue = Volley.newRequestQueue(activity!!)
        requestQueue.add(request)
    }

    fun getAllData() {
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmer()
        itemAdapter.clear()
        val request = JsonArrayRequest(
                Request.Method.GET,
                URL, null,
                Response.Listener { response ->
                    shimmerFrameLayout.visibility = View.GONE
                    try {
                        for (i in 0 until response.length()) {
                            val item = Item()
                            item.name = response.getJSONObject(i).get("name").toString()
                            item.types = typeConvert(response.getJSONObject(i).getInt("type"))
                            listItem.add(item)
                        }
                        itemAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {

                    }
                },
                Response.ErrorListener { Toast.makeText(activity, "Check your internet connection and try again", Toast.LENGTH_SHORT).show() }
        )

        val requestQueue = Volley.newRequestQueue(activity!!)
        requestQueue.add(request)
    }

    fun typeConvert(type: Int): String {
        when (type) {
            1 -> return "Weapon"
            2 -> return "Off-hand"
            3 -> return "Armors"
            4 -> return "Garmantes"
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
