package com.first.roexchange.adapter

import android.content.Context
import android.content.Intent
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.first.roexchange.DetailRecipeActivity
import com.first.roexchange.R
import com.first.roexchange.model.Recipe
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class RecipeAdapter(internal var context: Context, internal var recipeList: ArrayList<Recipe>) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>(), Filterable {
    internal var copyList: ArrayList<Recipe>

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                copyList = if (constraint == null || constraint.isEmpty()) {
                    recipeList
                } else {
                    val filteredList = ArrayList<Recipe>()
                    val filterPattern: String = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    for (recipe: Recipe in recipeList) {
                        if (recipe.name!!.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                            filteredList.add(recipe)
                        }
                    }

                    filteredList
                }

                val results = FilterResults()
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


    inner class ViewHolder(recipeView: View) : RecyclerView.ViewHolder(recipeView) {

        private var recipeName: TextView = recipeView.findViewById(R.id.tvRecipeName)
        private var recipeIText: TextView = recipeView.findViewById(R.id.tvRecipeIText)
        private var ingre1: ImageView = recipeView.findViewById(R.id.ivIngre1)
        private var ingre2: ImageView = recipeView.findViewById(R.id.ivIngre2)
        private var ingre3: ImageView = recipeView.findViewById(R.id.ivIngre3)
        private var ingre4: ImageView = recipeView.findViewById(R.id.ivIngre4)
        private var ingre5: ImageView = recipeView.findViewById(R.id.ivIngre5)
        private var recipeImage: ImageView = recipeView.findViewById(R.id.ivRecipeImage)
        private var recipeStation: ImageView = recipeView.findViewById(R.id.ivRecipeStation)
        internal var layoutParent: CardView = recipeView.findViewById(R.id.layoutParentRecipe)


        fun bind(recipe: Recipe) {
            recipeName.text = recipe.name
            if (!recipe.iText.equals("")) {
                recipeIText.text = recipe.iText
                recipeIText.visibility = View.VISIBLE
                ingre1.visibility = View.GONE
                ingre2.visibility = View.GONE
                ingre3.visibility = View.GONE
                ingre4.visibility = View.GONE
                ingre5.visibility = View.GONE

            } else {
                recipeIText.visibility = View.GONE
                ingre1.visibility = View.VISIBLE

                Picasso.with(context).load(recipe.iImage1).into(ingre1)
                if (!ingre2.equals("")) {
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