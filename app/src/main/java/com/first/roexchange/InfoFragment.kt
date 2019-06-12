package com.first.roexchange

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class InfoFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        if (v!!.id == R.id.ibGuide){
            val intent = Intent(activity, GeneralGuidesActivity::class.java)
            startActivity(intent)
        }else if(v!!.id == R.id.ibMapGinger){
            val intent = Intent(activity, DailyGingerActivity::class.java)
            startActivity(intent)
        }
    }

    lateinit var ibGuide: ImageButton
    lateinit var ibGinger: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_info, container, false)
        ibGuide = v.findViewById(R.id.ibGuide)
        ibGinger = v.findViewById(R.id.ibMapGinger)

        ibGuide.setOnClickListener(this)
        ibGinger.setOnClickListener(this)

        return v
    }
}
