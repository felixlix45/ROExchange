package com.first.roexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.first.roexchange.adapter.RecipeAdapter
import com.first.roexchange.model.Recipe
import org.json.JSONObject
import java.io.InputStream
import java.lang.Exception

class RecipeActivity : AppCompatActivity() {

    lateinit var recipeAdapter: RecipeAdapter
    private var recipeList = ArrayList<Recipe>()


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
                recipeAdapter.filter.filter(newText)
                return false
            }
        })
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val rvRecipe = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvRecipe)

        recipeAdapter = RecipeAdapter(baseContext, recipeList)

        rvRecipe.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(applicationContext)
        rvRecipe.adapter = recipeAdapter

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Recipe List"

        try {
            val jsonObj = JSONObject(loadJson())
            val jArray = jsonObj.getJSONArray("recipe")

            for (i in 1 until jArray.length()) {
                val recipe = Recipe()
                val jObj = jArray.getJSONObject(i)
                recipe.name = jObj.get("name").toString()
                recipe.stars = jObj.get("stars").toString()
                recipe.images = jObj.get("images").toString()
                recipe.station = jObj.get("station").toString()
                if (jArray.getJSONObject(i).has("indgredients_text")) {
                    recipe.iText = jObj.get("indgredients_text").toString()
                } else {
                    recipe.iText = ""
                    val jArray2 = jObj.getJSONArray("ingredients")
                    for (j in 0 until jArray2.length()) {
                        val jObj2 = jArray2.getJSONObject(j)

                        when (j) {
                            0 -> recipe.iImage1 = jObj2.get("ingredients_image").toString()
                            1 -> recipe.iImage2 = jObj2.get("ingredients_image").toString()
                            2 -> recipe.iImage3 = jObj2.get("ingredients_image").toString()
                            3 -> recipe.iImage4 = jObj2.get("ingredients_image").toString()
                            4 -> recipe.iImage5 = jObj2.get("ingredients_image").toString()
                        }
                    }
                    if (jArray2.length() < 2) {
                        recipe.iImage2 = ""
                        recipe.iImage3 = ""
                        recipe.iImage4 = ""
                        recipe.iImage5 = ""
                    }
                }

                recipeList.add(recipe)
            }
            recipeAdapter.notifyDataSetChanged()
//            Toast.makeText(applicationContext, "Berhasil Parse Json", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Failed to load data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadJson(): String {
        lateinit var json: String
        try {
            val istream: InputStream = applicationContext.assets.open("recipe_list.json")
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
        private const val TAG = "RecipeActivity"


    }
}
