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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class EtFragment extends Fragment {
    ImageView ivMini, ivBoss;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_et, container, false);
        ivBoss = v.findViewById(R.id.ivBossTower);
        ivMini = v.findViewById(R.id.ivMini);
        progressBar = v.findViewById(R.id.progressBar);
        String BossTower = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Endless-Tower-MVP-List-1.jpg";
        String MiniTower = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Endless-Tower-MINI-List-1.jpg";

        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(getActivity()).load(BossTower).into(ivBoss, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
        Picasso.with(getActivity()).load(MiniTower).into(ivMini);


        return v;
    }
}
