package com.first.roexchange

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.first.roexchange.adapter.ItemsAdapter
import com.first.roexchange.databinding.FragmentHomeBinding
import com.first.roexchange.model.Item
import com.first.roexchange.viewmodel.HomeViewModel
import java.util.*

class HomeFragment : androidx.fragment.app.Fragment() {

    private var listItem = ArrayList<Item>()

    private var URL = "https://www.romexchange.com/api/items.json"
    private lateinit var binding: FragmentHomeBinding
    private lateinit var itemsAdapter: ItemsAdapter
    private val viewModel = HomeViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        this.setDataBinding(inflater, container)
        this.bindViewModel()
        itemsAdapter = ItemsAdapter(requireActivity(), listItem)

        this.binding.shimmerContainer.startShimmer()

        this.binding.rvItem.layoutManager = LinearLayoutManager(activity)
        this.binding.rvItem.adapter = itemsAdapter

        this.binding.btnSearch.setOnClickListener {
            this.binding.shimmerContainer.visibility = View.VISIBLE
            this.binding.shimmerContainer.startShimmer()
            val name = this.binding.etSearchItem.text.toString().replace("\\s+".toRegex(), "%20")
            if (this.binding.etSearchItem.text.toString() != "") {
                itemsAdapter.clear()
                if (!this.binding.cbFilter.isChecked) {
                    val request = JsonArrayRequest(
                            Request.Method.GET,
                            "https://www.romexchange.com/api?exact=false&item=$name",
                            null,
                            Response.Listener { response ->
                                this.binding.shimmerContainer.visibility = View.GONE
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
                                this.binding.shimmerContainer.visibility = View.GONE
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
            this.binding.etSearchItem.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        this.binding.cbFilter.setOnCheckedChangeListener { _, _ ->
            if (this.binding.cbFilter.isChecked) {
                this.binding.spinnerFilter.visibility = View.VISIBLE
                getFilteredData()
            } else {
                this.binding.spinnerFilter.visibility = View.INVISIBLE
                getAllData()
            }
        }

        this.binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                getFilteredData()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        // Keyboard Search Click
        this.binding.etSearchItem.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                this.binding.btnSearch.performClick()
                return@OnEditorActionListener true
            }
            false
        })

        this.binding.reset.setOnClickListener {
            itemsAdapter.clear()
            this.binding.cbFilter.isChecked = false
            this.binding.etSearchItem.setText("")
            this.binding.etSearchItem.onEditorAction(EditorInfo.IME_ACTION_DONE)
            getAllData()
        }

        if (itemsAdapter.size() == 0) {
            getAllData()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        this.viewModel.getInformation()
    }

    private fun setDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        this.binding.lifecycleOwner = this
        this.binding.fragment = this
    }

    private fun bindViewModel() {
        this.viewModel.information.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (!it.isNullOrEmpty()) {
                this.binding.informationTextView.text = it
                this.binding.informationTextView.visibility = View.VISIBLE
            }
        })
    }

    fun getFilteredData() {
        itemsAdapter.clear()
        val pos = this.binding.spinnerFilter.selectedItemPosition + 1
        val URLFiltered = "https://www.romexchange.com/api/items.json"//exact=false&item=" + this.binding.etSearchItem.getText().toString().replaceAll("\\s+","%20") + "&type=" + pos;
        //        progressBar.setVisibility(View.VISIBLE);
        this.binding.shimmerContainer.visibility = View.VISIBLE
        this.binding.shimmerContainer.startShimmer()
        val request = JsonArrayRequest(
                Request.Method.GET,
                URLFiltered, null,
                Response.Listener { response ->
                    //                        progressBar.setVisibility(View.GONE);
                    this.binding.shimmerContainer.visibility = View.GONE
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
                    this.binding.shimmerContainer.visibility = View.GONE

                }
        )

        val requestQueue = Volley.newRequestQueue(activity!!)
        requestQueue.add(request)
    }

    private fun getFilteredData2() {
        itemsAdapter.clear()
        val pos = this.binding.spinnerFilter.selectedItemPosition + 1
        val URLFiltered = "https://www.romexchange.com/api/items.json"//exact=false&item=" + this.binding.etSearchItem.getText().toString().replaceAll("\\s+","%20") + "&type=" + pos;
        this.binding.shimmerContainer.visibility = View.VISIBLE
        this.binding.shimmerContainer.startShimmer()
        val request = JsonArrayRequest(
                Request.Method.GET,
                URLFiltered, null,
                Response.Listener { response ->
                    //                        progressBar.setVisibility(View.GONE);
                    this.binding.shimmerContainer.visibility = View.GONE
                    try {

                        for (i in 0 until response.length()) {
                            if (response.getJSONObject(i).getInt("type") == pos) {
                                val item = Item()
                                val name = response.getJSONObject(i).get("name").toString().toLowerCase(Locale.ROOT).trim()
                                if (name.contains(this.binding.etSearchItem.text.toString().toLowerCase(Locale.ROOT).trim(), false)) {
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
                    this.binding.shimmerContainer.visibility = View.GONE

                }
        )

        val requestQueue = Volley.newRequestQueue(activity!!)
        requestQueue.add(request)
    }

    private fun getAllData() {
        this.binding.shimmerContainer.visibility = View.VISIBLE
        this.binding.shimmerContainer.startShimmer()
        itemsAdapter.clear()
        val request = JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                Response.Listener { response ->
                    this.binding.shimmerContainer.visibility = View.GONE
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

    private fun typeConvert(type: Int): String {
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
        private const val TAG = "HomeFragment"
    }
}
