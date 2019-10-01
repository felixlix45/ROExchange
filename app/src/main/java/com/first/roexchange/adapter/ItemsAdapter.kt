package com.first.roexchange.adapter

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

import com.first.roexchange.DetailActivity
import com.first.roexchange.R
import com.first.roexchange.model.Item

import java.util.ArrayList
import java.util.Locale

class ItemsAdapter(internal var context: Context, private var itemList: ArrayList<Item>) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    private var copyList: ArrayList<Item> = ArrayList()

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    fun size(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_view_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(itemList[i])

        viewHolder.layoutParent.setOnClickListener { v ->
            val intent = Intent(v.context, DetailActivity::class.java)
            val name = itemList[i].name!!.toString().replace("\\s+".toRegex(), "%20")
            intent.putExtra("URL", name)
            intent.putExtra("Types", itemList[i].types)
            intent.putExtra("act", v.context.toString())
            intent.putExtra("pos", i)
            v.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvTitle: TextView = itemView.findViewById(R.id.tvItemName)
        private var tvTypes: TextView = itemView.findViewById(R.id.tvTypes)
        internal var layoutParent: LinearLayout = itemView.findViewById(R.id.layoutParent)


        fun bind(item: Item) {
            tvTypes.text = item.types
            tvTitle.text = item.name
        }

    }
}
