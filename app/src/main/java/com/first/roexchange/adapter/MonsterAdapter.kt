package com.first.roexchange.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.first.roexchange.R
import com.first.roexchange.model.Monster
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
        val view = LayoutInflater.from(context).inflate(R.layout.list_view_monster, p0, false)

        return ViewHolder(view)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return monsterList.size
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(monsterList[p1])
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    inner class ViewHolder(monsterView : View) : RecyclerView.ViewHolder(monsterView){

        internal lateinit var monsterName : TextView
        internal lateinit var monsterRace : TextView
        internal lateinit var monsterElement: TextView
        internal lateinit var monsterSize : TextView

        init {

            monsterElement = monsterView.findViewById(R.id.tvMonsterElement)
            monsterName = monsterView.findViewById(R.id.tvMonsterName)
            monsterRace = monsterView.findViewById(R.id.tvMonsterRace)
            monsterSize = monsterView.findViewById(R.id.tvMonsterSize)
        }
        fun bind(monster : Monster){

            monsterName.text = monster.name
            monsterElement.text = monster.element
            monsterSize.text = monster.size
            monsterRace.text =monster.race

        }
    }
}