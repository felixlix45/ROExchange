package com.example.roexchange.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.example.roexchange.DetailActivity
import com.example.roexchange.R
import com.example.roexchange.model.Item

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Collections
import java.util.Date
import java.util.Locale

class ItemAdapter(internal var context: Context, internal var itemList: ArrayList<Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    internal var copyList: ArrayList<Item>


    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    fun size(): Int {
        return itemList.size
    }

    fun addAll(copyList: ArrayList<Item>) {
        itemList = copyList.clone() as ArrayList<Item>
        notifyDataSetChanged()
    }

    init {
        this.copyList = ArrayList()
    }

    fun filter(text: String) {
        var text = text
        text = text.toLowerCase(Locale.getDefault())
        itemList.clear()
        if (text.length == 0) {
            itemList = copyList.clone() as ArrayList<Item>
        } else {
            for (item in copyList) {
                if (item.name!!.toLowerCase(Locale.getDefault()).contains(text)) {
                    itemList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_view_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(itemList[i])
        //        copyList = (ArrayList<Item>)itemList.clone();
        viewHolder.layoutParent.setOnClickListener { v ->
            val intent = Intent(v.context, DetailActivity::class.java)
            val name = itemList[i].name!!.toString().replace("\\s+".toRegex(), "%20")
            intent.putExtra("URL", name)
            intent.putExtra("Types", itemList[i].types)
            intent.putExtra("act", v.context.toString())
            intent.putExtra("pos", i)
            v.context.startActivity(intent)
        }
        //        viewHolder.btnFavorite.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                Toast.makeText(v.getContext(), "Item " + itemList.get(i).getName() + " has been added to favorite" , Toast.LENGTH_SHORT).show();
        //            }
        //        });
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var tvTitle: TextView
        internal var tvTypes: TextView
        internal var btnFavorite: ImageButton? = null
        internal var layoutParent: LinearLayout


        init {

            tvTitle = itemView.findViewById(R.id.tvItemName)
            tvTypes = itemView.findViewById(R.id.tvTypes)
            layoutParent = itemView.findViewById(R.id.layoutParent)
            //            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }

        fun bind(item: Item) {
            tvTypes.text = item.types
            tvTitle.text = item.name
        }

    }
}
