package com.first.roexchange.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.first.roexchange.DetailRecipeActivity
import com.first.roexchange.R
import com.first.roexchange.model.Recipe
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_view_recipe.view.*
import java.util.zip.Inflater

class RecipeAdapter(internal var context: Context, internal var recipeList: ArrayList<Recipe>) :RecyclerView.Adapter<RecipeAdapter.ViewHolder>(), Filterable {
    internal lateinit var copyList: ArrayList<Recipe>

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                if(constraint == null || constraint.length == 0){
                    copyList = recipeList
                }else{
                    val filteredList = ArrayList<Recipe>()
                    val filterPattern: String = constraint.toString().toLowerCase().trim()
                    for (recipe:Recipe in recipeList){
                        if(recipe.name!!.toLowerCase().contains(filterPattern)){
                            filteredList.add(recipe)
                        }
                    }

                    copyList = filteredList
                }

                var results = FilterResults()
                results.values = copyList

                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                copyList.clear()
                copyList = results!!.values as ArrayList<Recipe>
//                copyList.addAll(results!!.values as ArrayList<Monster>)
                notifyDataSetChanged()
            }
        }
    }

    init {
        this.copyList = recipeList
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_view_recipe, p0, false)


        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return copyList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(copyList[p1])

        p0.layoutParent.setOnClickListener { v ->
            val intent = Intent(v.context, DetailRecipeActivity::class.java)
            intent.putExtra("id", p1)
            intent.putExtra("name", recipeList[p1].name.toString())
            v.context.startActivity(intent)
        }

    }


    inner class ViewHolder(recipeView: View) :RecyclerView.ViewHolder(recipeView) {

        internal lateinit var recipeName: TextView
        internal lateinit var recipeIText: TextView
        internal lateinit var ingre1: ImageView
        internal lateinit var ingre2: ImageView
        internal lateinit var ingre3: ImageView
        internal lateinit var ingre4: ImageView
        internal lateinit var ingre5: ImageView
        internal lateinit var recipeImage: ImageView
        internal lateinit var recipeStation: ImageView
        internal lateinit var layoutParent: CardView

        init {
            recipeName = recipeView.findViewById<TextView>(R.id.tvRecipeName)
            recipeIText = recipeView.findViewById<TextView>(R.id.tvRecipeIText)
            ingre1 = recipeView.findViewById<ImageView>(R.id.ivIngre1)
            ingre2 = recipeView.findViewById<ImageView>(R.id.ivIngre2)
            ingre3 = recipeView.findViewById<ImageView>(R.id.ivIngre3)
            ingre4 = recipeView.findViewById<ImageView>(R.id.ivIngre4)
            ingre5 = recipeView.findViewById<ImageView>(R.id.ivIngre5)
            recipeImage = recipeView.findViewById(R.id.ivRecipeImage)
            recipeStation = recipeView.findViewById(R.id.ivRecipeStation)
            layoutParent = recipeView.findViewById(R.id.layoutParentRecipe)
        }


        fun bind(recipe: Recipe) {
            recipeName.text = recipe.name
            if(!recipe.iText.equals("")){
                recipeIText.text = recipe.iText
                recipeIText.visibility = View.VISIBLE
                ingre1.visibility = View.GONE
                ingre2.visibility = View.GONE
                ingre3.visibility = View.GONE
                ingre4.visibility = View.GONE
                ingre5.visibility = View.GONE

            }else{
                recipeIText.visibility = View.GONE
                ingre1.visibility = View.VISIBLE

                Picasso.with(context).load(recipe.iImage1).into(ingre1)
                if (!ingre2.equals("")){
                    ingre2.visibility = View.VISIBLE
                    ingre3.visibility = View.VISIBLE
                    ingre4.visibility = View.VISIBLE
                    ingre5.visibility = View.VISIBLE
                    Picasso.with(context).load(recipe.iImage2).into(ingre2)
                    Picasso.with(context).load(recipe.iImage3).into(ingre3)
                    Picasso.with(context).load(recipe.iImage4).into(ingre4)
                    Picasso.with(context).load(recipe.iImage5).into(ingre5)
                }
            }
            Picasso.with(context).load(recipe.images).into(recipeImage)
            Picasso.with(context).load(recipe.station).into(recipeStation)

        }

    }
}