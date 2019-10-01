package com.first.roexchange.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.first.roexchange.R
import com.first.roexchange.model.Monster
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class MonsterAdapter(internal var context: Context, internal var monsterList: ArrayList<Monster>) : RecyclerView.Adapter<MonsterAdapter.ViewHolder>(), Filterable {
    internal var copyList: ArrayList<Monster>

    init {
        this.copyList = monsterList
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                copyList = if (constraint == null || constraint.isEmpty()) {
                    monsterList
                } else {
                    val filteredList = ArrayList<Monster>()
                    val filterPattern: String = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    for (monster: Monster in monsterList) {
                        if (monster.name!!.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                            filteredList.add(monster)
                        }
                    }

                    filteredList
                }

                val results = FilterResults()
                results.values = copyList

                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                copyList = results!!.values as ArrayList<Monster>
                notifyDataSetChanged()
            }
        }
    }


    fun size(): Int {
        return copyList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.list_view_monster, p0, false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return copyList.size

    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(copyList[p1])

    }


    inner class ViewHolder(monsterView: View) : RecyclerView.ViewHolder(monsterView) {

        private var monsterName: TextView = monsterView.findViewById(R.id.tvMonsterName)
        private var monsterRace: TextView = monsterView.findViewById(R.id.tvMonsterRace)
        private var monsterElement: TextView = monsterView.findViewById(R.id.tvMonsterElement)
        private var monsterSize: TextView = monsterView.findViewById(R.id.tvMonsterSize)
        private var monsterImage: ImageView = monsterView.findViewById(R.id.ivMonster)

        fun bind(monster: Monster) {
            var level: String = monster.lvl.toString()
            level = level.replace("Level : ", "")
            monsterName.text = monster.name + " Lv. " + level
            monsterElement.text = monster.element
            monsterSize.text = monster.size
            monsterRace.text = monster.race
            Picasso.with(context).load(monster.image).into(monsterImage)

        }
    }


}