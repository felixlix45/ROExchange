package com.example.roexchange;

import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class EtFragment extends Fragment {

    private static final String TAG = "ETFragment";

    ImageView ivMini, ivBoss;
//    ProgressBar progressBar;
    ShimmerFrameLayout container_et_floor, container_et_mini;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.collection("URL").document("ET");
    String URLMVP = "";
    String URLMini = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_et, container, false);


        ivBoss = v.findViewById(R.id.ivBossTower);
        ivMini = v.findViewById(R.id.ivMini);
//        progressBar = v.findViewById(R.id.progressBar);
        container_et_floor = v.findViewById(R.id.shimmer_container_et_floor);
        container_et_mini = v.findViewById(R.id.shimmer_container_et_mini);

        Map<String, Object> note = new HashMap<>();

        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    URLMVP = documentSnapshot.getString("MVP");
                    URLMini = documentSnapshot.getString("Mini");
//                    Toast.makeText(getActivity(), URLMVP, Toast.LENGTH_SHORT).show();

                    Picasso.with(getActivity()).load(URLMVP).into(ivBoss, new Callback() {
                        @Override
                        public void onSuccess() {
//                progressBar.setVisibility(View.GONE);
                            container_et_floor.setVisibility(View.GONE);
                            container_et_floor.stopShimmer();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(getActivity(), "Something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Picasso.with(getActivity()).load(URLMini).into(ivMini, new Callback() {
                        @Override
                        public void onSuccess() {
                            container_et_mini.setVisibility(View.GONE);
                            container_et_mini.stopShimmer();
                        }

                        @Override
                        public void onError() {

                        }
                    });

                }else{
                    Toast.makeText(getActivity(), "Document does not exist", Toast.LENGTH_SHORT).show();
                    URLMVP = "";
                    URLMini = "";
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Fail to get data", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

//        progressBar.setVisibility(View.VISIBLE);
        container_et_floor.setVisibility(View.VISIBLE);
        container_et_floor.startShimmer();
        container_et_mini.setVisibility(View.VISIBLE);
        container_et_mini.startShimmer();
        return v;
    }


}
