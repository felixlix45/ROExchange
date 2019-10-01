package com.first.roexchange

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class InfoFragment : androidx.fragment.app.Fragment(), View.OnClickListener {
    override fun onClick(v: View) {
        when {
            v.id == R.id.ibGuide -> {
                val intent = Intent(activity, GeneralGuidesActivity::class.java)
                startActivity(intent)
            }
            v.id == R.id.ibMapGinger -> {
                val intent = Intent(activity, DailyGingerActivity::class.java)
                startActivity(intent)
            }
            v.id == R.id.ibMonsterList -> {

                val intent = Intent(activity, MonsterList::class.java)
                startActivity(intent)
            }
            v.id == R.id.ibRecipe -> {
                val intent = Intent(activity, RecipeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private lateinit var ibGuide: ImageButton
    private lateinit var ibGinger: ImageButton
    private lateinit var ibMonsterList: ImageButton
    private lateinit var ibFoodRecipe: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_info, container, false)
        ibGuide = v.findViewById(R.id.ibGuide)
        ibGinger = v.findViewById(R.id.ibMapGinger)
        ibMonsterList = v.findViewById(R.id.ibMonsterList)
        ibFoodRecipe = v.findViewById(R.id.ibRecipe)

        ibGuide.setOnClickListener(this)
        ibGinger.setOnClickListener(this)
        ibMonsterList.setOnClickListener(this)
        ibFoodRecipe.setOnClickListener(this)

        return v
    }
}
