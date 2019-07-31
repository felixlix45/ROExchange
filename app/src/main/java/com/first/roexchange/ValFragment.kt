package com.first.roexchange

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.facebook.shimmer.ShimmerFrameLayout
import com.first.roexchange.viewmodel.EtValViewModel
import com.github.chrisbanes.photoview.PhotoView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ValFragment : Fragment() {
    lateinit var ivValhala40: ImageView
    lateinit var ivValhala60: ImageView
    lateinit var ivValhala80: ImageView
    lateinit var ivValhala100: ImageView
    lateinit var valItem: PhotoView
    private val db = FirebaseFirestore.getInstance()
    private val noteRef = db.collection("URL").document("VAL")
    internal var ValURL: String? = ""
    internal var valGlobalURL: String? = ""
    internal var lastupdated: String? = ""
    //    ProgressBar progressBar;
    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    private lateinit var etValViewModel: EtValViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_val, container, false)

        shimmerFrameLayout = v.findViewById(R.id.shimmer_container_valhalla)
        //        progressBar = v.findViewById(R.id.progressBar);
        //        progressBar.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer()
        shimmerFrameLayout.visibility = View.VISIBLE

        val valGlobal = v.findViewById<PhotoView>(R.id.ValGlobal)
        val tvLastUpdated = v.findViewById<TextView>(R.id.tvLastUpdated)
        valItem = v.findViewById(R.id.ValItem)
        ivValhala40 = v.findViewById(R.id.valhala40)
        ivValhala60 = v.findViewById(R.id.valhala60)
        ivValhala80 = v.findViewById(R.id.valhala80)
        ivValhala100 = v.findViewById(R.id.valhala100)
        ivValhala40.visibility = View.GONE
        ivValhala60.visibility = View.GONE
        ivValhala80.visibility = View.GONE
        ivValhala100.visibility = View.GONE
        //        final String val40 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-40-1.jpg";
//        val val60 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-601.jpg"
//        val val80 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-801.jpg"
//        val val100 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-1001.jpg"

        etValViewModel = ViewModelProviders.of(requireActivity()).get(EtValViewModel::class.java)
        etValViewModel.getVal().observe(this, Observer { listVal ->
            if(listVal!!.isNotEmpty()){
                ValURL = listVal[0]
                valGlobalURL = listVal[1]
                lastupdated = listVal[2]
                tvLastUpdated.text = lastupdated

                Picasso.with(activity).load(valGlobalURL).into(valGlobal)

                Picasso.with(activity).load(ValURL).into(valItem, object : Callback {
                    override fun onSuccess() {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }

                    override fun onError() {
                        Toast.makeText(activity, "Sorry, something wrong :(", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })

        etValViewModel.setVal()

        return v
    }
}
