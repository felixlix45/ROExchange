package com.example.roexchange.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roexchange.DetailActivity;
import com.example.roexchange.R;
import com.example.roexchange.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    Context context;
    ArrayList<Item> itemList;
    ArrayList<Item> copyList;


    public void clear(){
        itemList.clear();
        notifyDataSetChanged();
    }

    public int size(){
        return itemList.size();
    }

    public void addAll(ArrayList<Item> copyList){
        itemList = (ArrayList<Item>)copyList.clone();
        notifyDataSetChanged();
    }

    public ItemAdapter(Context context, ArrayList<Item> itemList){
        this.context = context;
        this.itemList = itemList;
        this.copyList = new ArrayList<>();
    }

    public void filter(String text){
        text = text.toLowerCase(Locale.getDefault());
        itemList.clear();
        if(text.length() == 0){
            itemList = (ArrayList<Item>)copyList.clone();
        }else{
            for (Item item : copyList) {
                if (item.getName().toLowerCase(Locale.getDefault()).contains(text)) {
                    itemList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.bind(itemList.get(i));
//        copyList = (ArrayList<Item>)itemList.clone();
        viewHolder.layoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                String name = itemList.get(i).getName().toString().replaceAll("\\s+","%20");
                intent.putExtra("URL", name);
                intent.putExtra("Types", itemList.get(i).getTypes());
                v.getContext().startActivity(intent);
            }
        });
//        viewHolder.btnFavorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Item " + itemList.get(i).getName() + " has been added to favorite" , Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvTypes;
        ImageButton btnFavorite;
        LinearLayout layoutParent;


        public ViewHolder(View itemView){
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvItemName);
            tvTypes = itemView.findViewById(R.id.tvTypes);
            layoutParent = itemView.findViewById(R.id.layoutParent);
//            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }

        public void bind(Item item){
            tvTypes.setText(item.getTypes());
            tvTitle.setText(item.getName());
        }

    }
}
