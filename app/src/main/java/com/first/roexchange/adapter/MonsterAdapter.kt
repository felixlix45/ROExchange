package com.first.roexchange.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.first.roexchange.R
import com.first.roexchange.model.Monster
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class MonsterAdapter(internal var context:Context, internal var monsterList: ArrayList<Monster>) : RecyclerView.Adapter<MonsterAdapter.ViewHolder>() {

    fun clear(){
        monsterList.clear()
        notifyDataSetChanged()
    }

    fun size(): Int{
        return monsterList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.list_view_monster, p0, false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return monsterList.size

    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(monsterList[p1])

    }


    inner class ViewHolder(monsterView : View) : RecyclerView.ViewHolder(monsterView){

        internal lateinit var monsterName : TextView
        internal lateinit var monsterRace : TextView
        internal lateinit var monsterElement: TextView
        internal lateinit var monsterSize : TextView
        internal lateinit var layoutMonster: LinearLayout
        internal lateinit var monsterImage: ImageView

        init {

            monsterElement = monsterView.findViewById(R.id.tvMonsterElement)
            monsterName = monsterView.findViewById(R.id.tvMonsterName)
            monsterRace = monsterView.findViewById(R.id.tvMonsterRace)
            monsterSize = monsterView.findViewById(R.id.tvMonsterSize)
            layoutMonster = monsterView.findViewById(R.id.layoutParentMonster)
            monsterImage = monsterView.findViewById(R.id.ivMonster)
        }
        fun bind(monster : Monster){

            monsterName.text = monster.name
            monsterElement.text = monster.element
            monsterSize.text = monster.size
            monsterRace.text =monster.race
            Picasso.with(context).load(monster.image).into(monsterImage)

        }
    }
}