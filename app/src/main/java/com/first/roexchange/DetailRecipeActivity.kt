package com.first.roexchange

import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_recipe.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.InputStream
import java.lang.Exception

class DetailRecipeActivity : AppCompatActivity() {

    lateinit var tvName: TextView
    lateinit var tvEffect: TextView
    lateinit var ivFoodImage: ImageView
    lateinit var ivFoodStation: ImageView
    lateinit var ivIngre1: ImageView
    lateinit var ivIngre2: ImageView
    lateinit var ivIngre3: ImageView
    lateinit var ivIngre4: ImageView
    lateinit var ivIngre5: ImageView
    lateinit var tvIngreText: TextView
    lateinit var layoutImage: LinearLayout
    lateinit var layoutExtended: LinearLayout


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
            supportActionBar!!.setTitle(intent.getStringExtra("name"))

            var index = intent.getIntExtra("id", 0)
            index = index + 1
            Log.e(TAG, index.toString())


            try{
                var jsonObj = JSONObject(loadJson())
                var jArray = jsonObj.getJSONArray("recipe").getJSONObject(index)
                Log.e(TAG, jArray.toString())

//                Log.e(TAG, jArray2.toString())
                tvName.text = jArray.get("name").toString()
                Picasso.with(baseContext).load(jArray.get("images").toString()).into(ivFoodImage)
                Picasso.with(baseContext).load(jArray.get("station").toString()).into(ivFoodStation)
                if(jArray.has("indgredients_text")){
                    tvIngreText.text = jArray.get("indgredients_text").toString()
                    layoutImage.visibility = View.GONE
                }else{
                    var jArray2 = jArray.getJSONArray("ingredients")

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

    fun loadJson():String{
        lateinit var json:String
        try {
            var istream: InputStream = applicationContext.assets.open("recipe_list.json")
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

    companion object{
        private val TAG= "RecipeActivity"


    }
}
