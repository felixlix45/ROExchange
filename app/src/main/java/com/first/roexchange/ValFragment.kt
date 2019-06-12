package com.first.roexchange

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast

import com.facebook.shimmer.ShimmerFrameLayout
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
    //    ProgressBar progressBar;
    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_val, container, false)

        shimmerFrameLayout = v.findViewById(R.id.shimmer_container_valhalla)
        //        progressBar = v.findViewById(R.id.progressBar);
        //        progressBar.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer()
        shimmerFrameLayout.visibility = View.VISIBLE

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
        val val60 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-601.jpg"
        val val80 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-801.jpg"
        val val100 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-1001.jpg"

        noteRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    ValURL = documentSnapshot.getString("VALList")


                    Picasso.with(activity).load(ValURL).into(ivValhala40, object : Callback {
                        override fun onSuccess() {
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                        }

                        override fun onError() {
                            Toast.makeText(activity, "Sorry, something wrong :(", Toast.LENGTH_SHORT).show()
                        }
                    })
                    Picasso.with(activity).load(ValURL).into(valItem, object : Callback {
                        override fun onSuccess() {
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE
                        }

                        override fun onError() {
                            Toast.makeText(activity, "Sorry, something wrong :(", Toast.LENGTH_SHORT).show()
                        }
                    })
                    Picasso.with(activity).load(val60).into(ivValhala60, object : Callback {
                        override fun onSuccess() {

                        }

                        override fun onError() {

                            ivValhala60.visibility = View.GONE
                        }
                    })
                    Picasso.with(activity).load(val80).into(ivValhala80, object : Callback {
                        override fun onSuccess() {

                        }

                        override fun onError() {
                            ivValhala80.visibility = View.GONE
                        }
                    })
                    Picasso.with(activity).load(val100).into(ivValhala100, object : Callback {
                        override fun onSuccess() {
                            //                progressBar.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer()
                            shimmerFrameLayout.visibility = View.GONE

                        }

                        override fun onError() {
                            ivValhala100.visibility = View.GONE
                        }
                    })
                }
                .addOnFailureListener { Toast.makeText(activity, "Fail to get data!", Toast.LENGTH_SHORT).show() }
        return v
    }
}
