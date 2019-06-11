package com.example.roexchange

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout

import com.github.chrisbanes.photoview.PhotoView

class InfoFragment : Fragment() {
    lateinit var ivWeapon: ImageView
    lateinit var ivLevel: ImageView
    lateinit var ivElement: PhotoView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_info, container, false)
        ivElement = v.findViewById(R.id.tableElement)
        ivWeapon = v.findViewById(R.id.tableWeaponPenalty)
        ivLevel = v.findViewById(R.id.tableLevelGap)

        //        ivElement.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                if(fullScreen){
        //                    fullScreen=false;
        //                    ivElement.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //                    ivElement.setAdjustViewBounds(true);
        //                }else{
        //                    fullScreen=true;
        //                    ivElement.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        //                    ivElement.setScaleType(ImageView.ScaleType.FIT_XY);
        //                }
        //            }
        //        });
        return v
    }
}