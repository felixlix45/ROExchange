package com.first.roexchange

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_info.*

class InfoFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        if (v!!.id == R.id.ibGuide){
            val intent = Intent(activity, GeneralGuidesActivity::class.java)
            startActivity(intent)
        }else if(v!!.id == R.id.ibMapGinger){
            val intent = Intent(activity, DailyGingerActivity::class.java)
            startActivity(intent)
        }else if(v!!.id == R.id.ibMonsterList){
            Toast.makeText(activity, "Coming Soon", Toast.LENGTH_SHORT).show()
//            val intent = Intent(activity, MonsterList::class.java)
//            startActivity(intent)
        }
    }

    lateinit var ibGuide: ImageButton
    lateinit var ibGinger: ImageButton
    lateinit var ibMonsterList: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_info, container, false)
        ibGuide = v.findViewById(R.id.ibGuide)
        ibGinger = v.findViewById(R.id.ibMapGinger)
        ibMonsterList = v.findViewById(R.id.ibMonsterList)

        ibGuide.setOnClickListener(this)
        ibGinger.setOnClickListener(this)
        ibMonsterList.setOnClickListener(this)

        return v
    }
}
