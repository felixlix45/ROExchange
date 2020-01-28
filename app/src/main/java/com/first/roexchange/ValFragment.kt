package com.first.roexchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.shimmer.ShimmerFrameLayout
import com.first.roexchange.viewmodel.EtValViewModel
import com.github.chrisbanes.photoview.PhotoView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ValFragment : androidx.fragment.app.Fragment() {
    private lateinit var ivValhala40: ImageView
    private lateinit var ivValhala60: ImageView
    private lateinit var ivValhala80: ImageView
    private lateinit var ivValhala100: ImageView
    private lateinit var valItem: PhotoView

    private var ValURL: String? = ""
    private var valGlobalURL: String? = ""
    private var lastupdated: String? = ""

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    private lateinit var etValViewModel: EtValViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_val, container, false)

        shimmerFrameLayout = v.findViewById(R.id.shimmer_container_valhalla)
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

        etValViewModel = ViewModelProviders.of(requireActivity()).get(EtValViewModel::class.java)
        etValViewModel.getVal().observe(viewLifecycleOwner, Observer { listVal ->
            if (listVal!!.isNotEmpty()) {
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
