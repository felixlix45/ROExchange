package com.first.roexchange

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast

import com.facebook.shimmer.ShimmerFrameLayout
import com.github.chrisbanes.photoview.PhotoView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_et.*

import java.util.HashMap


class EtFragment : Fragment() {

    lateinit var ivMini: PhotoView
    lateinit var ivBoss: PhotoView
    //    ProgressBar progressBar;
    lateinit var container_et_floor: ShimmerFrameLayout
    lateinit var container_et_mini: ShimmerFrameLayout
    lateinit var container_et_global_mvp: ShimmerFrameLayout
    lateinit var container_et_global_mini: ShimmerFrameLayout
    private val db = FirebaseFirestore.getInstance()
    private val noteRef = db.collection("URL").document("ET")
    internal var URLMVP: String? = ""
    internal var URLMini: String? = ""
    internal var URLGlobalMVP: String? = ""
    internal var URLGlobalMini: String? = ""
    internal var URLLastUpdated: String? = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_et, container, false)
        setHasOptionsMenu(true)

        ivBoss = v.findViewById(R.id.ivBossTower)
        ivMini = v.findViewById(R.id.ivMini)
        var ivGlobalBoss = v.findViewById<PhotoView>(R.id.ivGlobalBossTower)
        var ivGlobalMini = v.findViewById<PhotoView>(R.id.ivGlobalMini)
        var lastUpdated = v.findViewById<TextView>(R.id.tvLastUpdated)
        container_et_floor = v.findViewById(R.id.shimmer_container_et_floor)
        container_et_mini = v.findViewById(R.id.shimmer_container_et_mini)
        container_et_global_mvp = v.findViewById(R.id.shimmer_container_et_global_mvp)
        container_et_global_mini = v.findViewById(R.id.shimmer_container_et_global_mini)

        val note = HashMap<String, Any>()
        container_et_floor.visibility = View.VISIBLE
        container_et_floor.startShimmer()
        container_et_mini.visibility = View.VISIBLE
        container_et_mini.startShimmer()
        if(URLMVP != "" || URLMini != ""){
            container_et_floor.stopShimmer()
            container_et_mini.stopShimmer()
        }

        noteRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                URLMVP = documentSnapshot.getString("MVP")
                URLMini = documentSnapshot.getString("Mini")
                URLGlobalMVP = documentSnapshot.getString("GlobalMVP")
                URLGlobalMini = documentSnapshot.getString("GlobalMini")
                URLLastUpdated = documentSnapshot.getString("LastUpdated")

                if(URLGlobalMVP.equals("http://asbt.in/images/Events/update-soon_9-19-2017_060559.1463650.jpg")){
                    Toast.makeText(activity, "Can't find reliable resource for ET MVP Global Server", Toast.LENGTH_SHORT).show()
                }else if(URLGlobalMini.equals("http://asbt.in/images/Events/update-soon_9-19-2017_060559.1463650.jpg")){
                    Toast.makeText(activity, "Can't find reliable resource for ET Mini Global Server", Toast.LENGTH_SHORT).show()
                }



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

            } else {
                Toast.makeText(activity, "Document does not exist", Toast.LENGTH_SHORT).show()
                URLMVP = ""
                URLMini = ""
            }
        }.addOnFailureListener { e ->
            Toast.makeText(activity, "Fail to get data", Toast.LENGTH_SHORT).show()
            Log.d(TAG, e.toString())
        }

        //        progressBar.setVisibility(View.VISIBLE);

        return v
    }

    companion object {

        private val TAG = "ETFragment"
    }


}
