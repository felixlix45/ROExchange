package com.first.roexchange

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.first.roexchange.adapter.MonsterAdapter
import com.first.roexchange.model.Item
import com.first.roexchange.model.Monster
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.lang.Exception

class MonsterList : AppCompatActivity() {

    lateinit var monsterAdapter: MonsterAdapter

    internal var monsterList = ArrayList<Monster>()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater:MenuInflater = menuInflater
        inflater.inflate(R.menu.filter_menu, menu)

        val searchItem:MenuItem = menu!!.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                monsterAdapter.filter.filter(newText)
                return false
            }
        })
        return true
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_list)

        val rvMonster = findViewById(R.id.rvMonster) as RecyclerView
        val toolbar = findViewById(R.id.toolbar) as Toolbar

        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Monster List")

        monsterAdapter = MonsterAdapter(applicationContext, monsterList)

        rvMonster.layoutManager = LinearLayoutManager(applicationContext)
        rvMonster.adapter = monsterAdapter

//        monsterAdapter.clear()
//        val monster = Monster()
//        monster.name = "Dicky"
//        monster.size = "Large"
//        monster.race = "Human"
//        monster.element = "Fire"
//        val monster2 = Monster()
//        monster2.name = "Felix"
//        monster2.size = "Smol"
//        monster2.race = "Human"
//        monster2.element = "Water"
//        monsterList.add(monster)
//        monsterList.add(monster2)
//
//        monsterAdapter.notifyDataSetChanged()

        try{
            var jsonObj = JSONObject(loadJson())
            var jArray = jsonObj.getJSONArray("monster_list")
//            Log.e(TAG, jArray.length().toString())
            for (i in 0 until jArray.length()){
                val monster = Monster()
                monster.name = jArray.getJSONObject(i).get("name").toString()
//                Log.e(TAG, monster.name)
                monster.size = "Size : " + jArray.getJSONObject(i).get("size").toString()
                monster.lvl = "Level : " + jArray.getJSONObject(i).get("level").toString()
                monster.element = "Element : " + jArray.getJSONObject(i).get("Element").toString()
                if (jArray.getJSONObject(i).has("race")){
                    monster.race = "Race : " + jArray.getJSONObject(i).get("race").toString()
                }else{
                    monster.race = "Race : No data available"
                }
                monster.image = jArray.getJSONObject(i).get("image").toString()
                monsterList.add(monster)
            }
            monsterAdapter.notifyDataSetChanged()
//            Toast.makeText(applicationContext, "Berhasil Parse Json", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            Toast.makeText(applicationContext, "Failed to load data", Toast.LENGTH_SHORT).show()
        }


    }

    fun loadJson():String{
        lateinit var json:String
        try {
            var istream:InputStream = applicationContext.assets.open("monster_list.json")
            var size: Int = istream.available()
            var buffer = ByteArray(size)
            istream.read(buffer)
            istream.close()
            val charset = Charsets.UTF_8
            json = String(buffer, charset)
        }catch (e: Exception){
            Log.d(TAG, "Error when try to read json")
        }
        return json
    }

    companion object {

        private val TAG = "MonsterList.Activity"
    }


}

