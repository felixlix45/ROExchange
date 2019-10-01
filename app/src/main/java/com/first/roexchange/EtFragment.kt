package com.first.roexchange

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.shimmer.ShimmerFrameLayout
import com.first.roexchange.viewmodel.EtValViewModel
import com.github.chrisbanes.photoview.PhotoView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_et.*


class EtFragment : androidx.fragment.app.Fragment() {

    private lateinit var ivMini: PhotoView
    private lateinit var ivBoss: PhotoView
    //    ProgressBar progressBar;
    lateinit var container_et_floor: ShimmerFrameLayout
    lateinit var container_et_mini: ShimmerFrameLayout
    lateinit var container_et_global_mvp: ShimmerFrameLayout
    lateinit var container_et_global_mini: ShimmerFrameLayout
    private val db = FirebaseFirestore.getInstance()
    private var URLMVP: String? = ""
    private var URLMini: String? = ""
    private var URLGlobalMVP: String? = ""
    private var URLGlobalMini: String? = ""
    private var URLLastUpdated: String? = ""

    private lateinit var etValViewModel: EtValViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_et, container, false)
        setHasOptionsMenu(true)

        ivBoss = v.findViewById(R.id.ivBossTower)
        ivMini = v.findViewById(R.id.ivMini)
        val ivGlobalBoss = v.findViewById<PhotoView>(R.id.ivGlobalBossTower)
        val ivGlobalMini = v.findViewById<PhotoView>(R.id.ivGlobalMini)
        container_et_floor = v.findViewById(R.id.shimmer_container_et_floor)
        container_et_mini = v.findViewById(R.id.shimmer_container_et_mini)
        container_et_global_mvp = v.findViewById(R.id.shimmer_container_et_global_mvp)
        container_et_global_mini = v.findViewById(R.id.shimmer_container_et_global_mini)

        container_et_floor.visibility = View.VISIBLE
        container_et_floor.startShimmer()
        container_et_mini.visibility = View.VISIBLE
        container_et_mini.startShimmer()
        if(URLMVP != "" || URLMini != ""){
            container_et_floor.stopShimmer()
            container_et_mini.stopShimmer()
        }


        etValViewModel = ViewModelProviders.of(requireActivity()).get(EtValViewModel::class.java)
        etValViewModel.getET().observe(this, Observer { etList ->
            if(etList!!.isNotEmpty()){
                URLMVP = etList[0]
                URLMini = etList[1]
                URLGlobalMVP = etList[2]
                URLGlobalMini = etList[3]
                URLLastUpdated = etList[4]


                tvLastUpdated.text = URLLastUpdated
                //                    Toast.makeText(getActivity(), URLMVP, Toast.LENGTH_SHORT).show();

                Picasso.with(activity).load(URLMVP).into(ivBoss, object : Callback {
                    override fun onSuccess() {
                        //                progressBar.setVisibility(View.GONE);
                        container_et_floor.visibility = View.GONE
                        container_et_floor.stopShimmer()
                    }

                    override fun onError() {
                        Toast.makeText(activity, "Something is wrong", Toast.LENGTH_SHORT).show()
                    }
                })

                Picasso.with(activity).load(URLMini).into(ivMini, object : Callback {
                    override fun onSuccess() {
                        container_et_mini.visibility = View.GONE
                        container_et_mini.stopShimmer()
                    }

                    override fun onError() {

                    }
                })

                Picasso.with(activity).load(URLGlobalMVP).into(ivGlobalBoss, object: Callback{
                    override fun onSuccess() {
                        container_et_global_mvp.visibility = View.GONE
                        container_et_global_mvp.stopShimmer()
                    }
                    override fun onError() {

                    }
                })
                Picasso.with(activity).load(URLGlobalMini).into(ivGlobalMini, object: Callback {
                    override fun onSuccess() {
                        container_et_global_mini.visibility = View.GONE
                        container_et_global_mini.stopShimmer()
                    }
                    override fun onError() {

                    }
                })
            }else{
                Log.e("ET", "Null")
            }

        })
        etValViewModel.setET()

        return v
    }

    companion object {
    }


}
