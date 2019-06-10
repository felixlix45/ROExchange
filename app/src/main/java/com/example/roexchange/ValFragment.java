package com.example.roexchange;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ValFragment extends Fragment {
    ImageView ivValhala40,ivValhala60,ivValhala80,ivValhala100;
    PhotoView valItem;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.collection("URL").document("VAL");
    String ValURL = "";
//    ProgressBar progressBar;
    ShimmerFrameLayout shimmerFrameLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_val, container, false);

        shimmerFrameLayout = v.findViewById(R.id.shimmer_container_valhalla);
//        progressBar = v.findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);

        valItem = v.findViewById(R.id.ValItem);
        ivValhala40 = v.findViewById(R.id.valhala40);
        ivValhala60 = v.findViewById(R.id.valhala60);
        ivValhala80 = v.findViewById(R.id.valhala80);
        ivValhala100 = v.findViewById(R.id.valhala100);
        ivValhala40.setVisibility(View.GONE);
        ivValhala60.setVisibility(View.GONE);
        ivValhala80.setVisibility(View.GONE);
        ivValhala100.setVisibility(View.GONE);
//        final String val40 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-40-1.jpg";
        final String val60 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-601.jpg";
        final String val80 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-801.jpg";
        final String val100 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-1001.jpg";

        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ValURL = documentSnapshot.getString("VALList");

                        Picasso.with(getActivity()).load(ValURL).into(ivValhala40, new Callback() {
                            @Override
                            public void onSuccess() {
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(getActivity(), "Sorry, something wrong :(", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Picasso.with(getActivity()).load(ValURL).into(valItem, new Callback() {
                            @Override
                            public void onSuccess() {
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(getActivity(), "Sorry, something wrong :(", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Picasso.with(getActivity()).load(val60).into(ivValhala60, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                                ivValhala60.setVisibility(View.GONE);
                            }
                        });
                        Picasso.with(getActivity()).load(val80).into(ivValhala80, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                ivValhala80.setVisibility(View.GONE);
                            }
                        });
                        Picasso.with(getActivity()).load(val100).into(ivValhala100, new Callback() {
                            @Override
                            public void onSuccess() {
//                progressBar.setVisibility(View.GONE);
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);

                            }

                            @Override
                            public void onError() {
                                ivValhala100.setVisibility(View.GONE);
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Fail to get data!", Toast.LENGTH_SHORT).show();
                    }
                });





        return v;
    }
}
