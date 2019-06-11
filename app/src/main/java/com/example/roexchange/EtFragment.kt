package com.example.roexchange

import android.nfc.Tag
import android.os.Bundle
import android.provider.ContactsContract
import android.support.constraint.solver.widgets.Snapshot
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast

import com.facebook.shimmer.ShimmerFrameLayout
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

import java.util.HashMap


class EtFragment : Fragment() {

    lateinit var ivMini: PhotoView
    lateinit var ivBoss: PhotoView
    //    ProgressBar progressBar;
    lateinit var container_et_floor: ShimmerFrameLayout
    lateinit var container_et_mini: ShimmerFrameLayout
    private val db = FirebaseFirestore.getInstance()
    private val noteRef = db.collection("URL").document("ET")
    internal var URLMVP: String? = ""
    internal var URLMini: String? = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_et, container, false)


        ivBoss = v.findViewById(R.id.ivBossTower)
        ivMini = v.findViewById(R.id.ivMini)
        //        progressBar = v.findViewById(R.id.progressBar);
        container_et_floor = v.findViewById(R.id.shimmer_container_et_floor)
        container_et_mini = v.findViewById(R.id.shimmer_container_et_mini)

        val note = HashMap<String, Any>()

        noteRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                URLMVP = documentSnapshot.getString("MVP")
                URLMini = documentSnapshot.getString("Mini")
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
        container_et_floor.visibility = View.VISIBLE
        container_et_floor.startShimmer()
        container_et_mini.visibility = View.VISIBLE
        container_et_mini.startShimmer()
        return v
    }

    companion object {

        private val TAG = "ETFragment"
    }


}
