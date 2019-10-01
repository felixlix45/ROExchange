package com.first.roexchange

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.first.roexchange.adapter.MonsterAdapter
import com.first.roexchange.model.Monster
import org.json.JSONObject
import java.io.InputStream

class MonsterList : AppCompatActivity() {

    lateinit var monsterAdapter: MonsterAdapter

    private var monsterList = ArrayList<Monster>()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.filter_menu, menu)

        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

        val rvMonster : RecyclerView = findViewById(R.id.rvMonster)
        val toolbar : Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Monster List"

        monsterAdapter = MonsterAdapter(applicationContext, monsterList)

        rvMonster.layoutManager = LinearLayoutManager(applicationContext)
        rvMonster.adapter = monsterAdapter

        try {
            val jsonObj = JSONObject(loadJson())
            val jArray = jsonObj.getJSONArray("monster_list")
            for (i in 0 until jArray.length()) {
                val monster = Monster()
                monster.name = jArray.getJSONObject(i).get("name").toString()
                monster.size = "Size : " + jArray.getJSONObject(i).get("size").toString()
                monster.lvl = "Level : " + jArray.getJSONObject(i).get("level").toString()
                monster.element = "Element : " + jArray.getJSONObject(i).get("Element").toString()
                if (jArray.getJSONObject(i).has("race")) {
                    monster.race = "Race : " + jArray.getJSONObject(i).get("race").toString()
                } else {
                    monster.race = "Race : No data available"
                }
                monster.image = jArray.getJSONObject(i).get("image").toString()
                monsterList.add(monster)
            }
            monsterAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Failed to load data", Toast.LENGTH_SHORT).show()
        }


    }

    private fun loadJson(): String {
        lateinit var json: String
        try {
            val istream: InputStream = applicationContext.assets.open("monster_list.json")
            val size: Int = istream.available()
            val buffer = ByteArray(size)
            istream.read(buffer)
            istream.close()
            val charset = Charsets.UTF_8
            json = String(buffer, charset)
        } catch (e: Exception) {
            Log.d(TAG, "Error when try to read json")
        }
        return json
    }

    companion object {

        private const val TAG = "MonsterList.Activity"
    }


}

