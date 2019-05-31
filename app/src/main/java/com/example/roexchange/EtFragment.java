package com.example.roexchange;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class EtFragment extends Fragment {
    ImageView ivMini, ivBoss;
//    ProgressBar progressBar;
    ShimmerFrameLayout container_et_floor, container_et_mini;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_et, container, false);
        ivBoss = v.findViewById(R.id.ivBossTower);
        ivMini = v.findViewById(R.id.ivMini);
//        progressBar = v.findViewById(R.id.progressBar);
        container_et_floor = v.findViewById(R.id.shimmer_container_et_floor);
        container_et_mini = v.findViewById(R.id.shimmer_container_et_mini);
        String BossTower = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Endless-Tower-MVP-List-1.jpg";
        String MiniTower = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Endless-Tower-MINI-List-1.jpg";

//        progressBar.setVisibility(View.VISIBLE);
        container_et_floor.setVisibility(View.VISIBLE);
        container_et_floor.startShimmer();
        container_et_mini.setVisibility(View.VISIBLE);
        container_et_mini.startShimmer();
        Picasso.with(getActivity()).load(BossTower).into(ivBoss, new Callback() {
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
        Picasso.with(getActivity()).load(MiniTower).into(ivMini, new Callback() {
            @Override
            public void onSuccess() {
                container_et_mini.setVisibility(View.GONE);
                container_et_mini.stopShimmer();
            }

            @Override
            public void onError() {

            }
        });


        return v;
    }
}
