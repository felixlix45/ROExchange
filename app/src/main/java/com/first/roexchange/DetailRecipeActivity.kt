package com.first.roexchange

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.io.InputStream

class DetailRecipeActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvEffect: TextView
    private lateinit var ivFoodImage: ImageView
    private lateinit var ivFoodStation: ImageView
    private lateinit var ivIngre1: ImageView
    private lateinit var ivIngre2: ImageView
    private lateinit var ivIngre3: ImageView
    private lateinit var ivIngre4: ImageView
    private lateinit var ivIngre5: ImageView
    private lateinit var tvIngreText: TextView
    private lateinit var layoutImage: LinearLayout
    private lateinit var layoutExtended: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_recipe)

        tvName = findViewById(R.id.tvFoodName)
        tvEffect = findViewById(R.id.tvFoodEffect)
        ivFoodImage = findViewById(R.id.ivFood)
        ivFoodStation = findViewById(R.id.ivFoodStation)
        ivIngre1 = findViewById(R.id.ivIngredients1)
        ivIngre2 = findViewById(R.id.ivIngredients2)
        ivIngre3 = findViewById(R.id.ivIngredients3)
        ivIngre4 = findViewById(R.id.ivIngredients4)
        ivIngre5 = findViewById(R.id.ivIngredients5)
        tvIngreText = findViewById(R.id.tvIngredients)
        layoutImage = findViewById(R.id.layoutIngredietnsImage)
        layoutExtended = findViewById(R.id.layoutIngredientExtend)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if(intent.hasExtra("name")){
            supportActionBar!!.title = intent.getStringExtra("name")

            var index = intent.getIntExtra("id", 0)
            index += 1
            Log.e(TAG, index.toString())


            try{
                val jsonObj = JSONObject(loadJson())
                val jArray = jsonObj.getJSONArray("recipe").getJSONObject(index)
                Log.e(TAG, jArray.toString())

                tvName.text = jArray.get("name").toString()
                Picasso.with(baseContext).load(jArray.get("images").toString()).into(ivFoodImage)
                Picasso.with(baseContext).load(jArray.get("station").toString()).into(ivFoodStation)
                if(jArray.has("indgredients_text")){
                    tvIngreText.text = jArray.get("indgredients_text").toString()
                    layoutImage.visibility = View.GONE
                }else{
                    val jArray2 = jArray.getJSONArray("ingredients")

                    tvIngreText.visibility = View.GONE
                    layoutExtended.visibility = View.GONE
                    for(j in 0 until jArray2.length()){
                        val jObj = jArray2.getJSONObject(j)
                        if (j == 0){
                            Picasso.with(baseContext).load(jObj.get("ingredients_image").toString()).into(ivIngre1)
                        }
                        if (j == 1){
                            Picasso.with(baseContext).load(jObj.get("ingredients_image").toString()).into(ivIngre2)
                        }
                        if (j == 2){
                            Picasso.with(baseContext).load(jObj.get("ingredients_image").toString()).into(ivIngre3)
                        }
                        if (j == 3){
                            layoutExtended.visibility = View.VISIBLE
                            Picasso.with(baseContext).load(jObj.get("ingredients_image").toString()).into(ivIngre4)
                        }
                        if (j == 4){
                            Picasso.with(baseContext).load(jObj.get("ingredients_image").toString()).into(ivIngre5)
                        }

                    }
                }

                val effects = jArray.get("effects").toString().replace(", ","\n")
                tvEffect.text = effects



            }catch (e:Exception){
                Log.e(TAG, e.toString())
                Toast.makeText(applicationContext, "Failed to load data", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(baseContext, "Error while loading data, please try again", Toast.LENGTH_SHORT).show()
        }

    }

    private fun loadJson():String{
        lateinit var json:String
        try {
            val istream: InputStream = applicationContext.assets.open("recipe_list.json")
            val size: Int = istream.available()
            val buffer = ByteArray(size)
            istream.read(buffer)
            istream.close()
            val charset = Charsets.UTF_8
            json = String(buffer, charset)
        }catch (e: Exception){
            Log.d(TAG, "Error when try to read json")
        }
        return json
    }

    companion object{
        private const val TAG= "RecipeActivity"


    }
}
