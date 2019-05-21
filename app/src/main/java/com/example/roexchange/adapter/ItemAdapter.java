package com.example.roexchange.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roexchange.DetailActivity;
import com.example.roexchange.R;
import com.example.roexchange.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    Context context;
    ArrayList<Item> itemList;

    public void clear(){
        itemList.clear();
        notifyDataSetChanged();
    }


    public ItemAdapter(Context context, ArrayList<Item> itemList){
        this.context = context;
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.bind(itemList.get(i));

        viewHolder.layoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                String name = itemList.get(i).getName().toString().replaceAll("\\s+","%20");
                intent.putExtra("URL", name);
                intent.putExtra("Types", itemList.get(i).getTypes());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvprice, tvDate, tvTypes;
        LinearLayout layoutParent;

        public ViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvItemName);
//            tvprice = itemView.findViewById(R.id.tvPriceNow);
//            tvDate = itemView.findViewById(R.id.tvLastUpdated);
            tvTypes = itemView.findViewById(R.id.tvTypes);
            layoutParent = itemView.findViewById(R.id.layoutParent);
        }

        public void bind(Item item){
//            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            SimpleDateFormat outputFormat = new SimpleDateFormat("E, dd-MM-yyyy HH:mm:ss Z" );
//            Date date = new Date();
//            try {
//                date = inputFormat.parse(item.getLastDate());
//                String formattedDate = outputFormat.format(date);
//                Toast.makeText(context, formattedDate, Toast.LENGTH_SHORT).show();
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

            tvTypes.setText(item.getTypes());
            tvTitle.setText(item.getName());
//            tvprice.setText("Price : " + String.valueOf(item.getLastPrice()));
//            tvDate.setText(item.getLastDate());


        }

    }
}
