package com.first.roexchange

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.first.roexchange.adapter.MonsterAdapter
import com.first.roexchange.model.Item
import com.first.roexchange.model.Monster

class MonsterList : AppCompatActivity() {

    lateinit var monsterAdapter: MonsterAdapter

    internal var monsterList = java.util.ArrayList<Monster>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_list)

        val rvMonster = findViewById(R.id.rvMonster) as RecyclerView

        monsterAdapter = MonsterAdapter(applicationContext, monsterList)

        rvMonster.layoutManager = LinearLayoutManager(applicationContext)
        rvMonster.adapter = monsterAdapter





    }
}
