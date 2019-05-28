package com.example.roexchange;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MoreFragment extends Fragment {
    ImageView ivValhala40,ivValhala60,ivValhala80,ivValhala100;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_more, container, false);
        ivValhala40 = v.findViewById(R.id.valhala40);
        ivValhala60 = v.findViewById(R.id.valhala60);
        ivValhala80 = v.findViewById(R.id.valhala80);
        ivValhala100 = v.findViewById(R.id.valhala100);
        String val40 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-40-1.jpg";
        String val60 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-60-1.jpg";
        String val80 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-80-1.jpg";
        String val100 = "https://ragnamobileguide.com/wp-content/uploads/2019/01/Valhalla-100-1.jpg";

        Picasso.with(getActivity()).load(val40).into(ivValhala40);
        Picasso.with(getActivity()).load(val60).into(ivValhala60);
        Picasso.with(getActivity()).load(val80).into(ivValhala80);
        Picasso.with(getActivity()).load(val100).into(ivValhala100);

        return v;
    }
}
