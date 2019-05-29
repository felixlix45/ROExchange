package com.example.roexchange;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.DateTimePatternGenerator;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.roexchange.model.Item;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity{

    TextView tvName, tvTypes, tvPrice, tvUpdated, tvLastPrice;
    Toolbar toolbar;
    ArrayList<Item> savedList = new ArrayList<>();
    private LineChart mChart;

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("FavoriteItem", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(savedList);
        editor.putString("task list", json);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(getIntent().getStringExtra("act").contains("MainActivity")){
            inflater.inflate(R.menu.detail_menu_item,menu);
        }else if(getIntent().getStringExtra("act").contains("FavoriteActivity")){
            inflater.inflate(R.menu.detail_menu_item_delete, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("FavoriteItem", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Item>>() {}.getType();
        savedList = gson.fromJson(json, type);
        if(savedList == null){
            savedList = new ArrayList<>();
        }
        Item obj = new Item();

        switch (item.getItemId()){

            case R.id.itemAddToFav:
                if(savedList != null){
                    for(int i = 0; i < savedList.size(); i++){
                        if(savedList.get(i).getName().equals(tvName.getText().toString().replaceAll("Name : ", "")) ){
                            Toast.makeText(this, "Already added to favorite", Toast.LENGTH_SHORT).show();
                            return true;

                        }
                    }
                }
                obj.setName(tvName.getText().toString().replaceAll("Name : ", ""));
                obj.setTypes(tvTypes.getText().toString(). replaceAll("Types : ", ""));
                savedList.add(obj);
                saveData();
                Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_delete:
                int i = getIntent().getIntExtra("pos",-1);
                if(i != -1) {
                    savedList.remove(i);
                    saveData();
                    Toast.makeText(this, "Deleted from favorite", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(this, savedList.size(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "-1", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        Toast.makeText(this, savedList.size(), Toast.LENGTH_SHORT).show();
        tvName = findViewById(R.id.tvDetailName);
        tvTypes = findViewById(R.id.tvDetailTypes);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvUpdated = findViewById(R.id.tvDetailUpdated);
        tvLastPrice = findViewById(R.id.tvDetailBeforeUpdatePrice);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mChart =findViewById(R.id.LineChart);

        Description desc = new Description();
        desc.setText("");
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(true);
        mChart.setNoDataText("CLICK ME MASTER!");
        mChart.setDescription(desc);
        mChart.setDrawBorders(true);


        if(getIntent().hasExtra("URL") && getIntent().hasExtra("Types")){
            String act = getIntent().getStringExtra("act").replaceAll("@.+","");
            final ArrayList<Entry> yValue = new ArrayList<>();
            final int[] priceArray = new int[7];
//            Toast.makeText(this, act, Toast.LENGTH_SHORT).show();
            String URL = "https://www.romexchange.com/api?item=" + getIntent().getStringExtra("URL");
            final ProgressDialog dialog = ProgressDialog.show(this, null, "Fetching data, please wait");
            JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                dialog.dismiss();
                try{
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    for(int i = 0; i < response.length(); i++){
                        tvName.setText("Name : " + response.getJSONObject(i).get("name").toString());
                        tvTypes.setText("Types : " + getIntent().getStringExtra("Types"));
                        tvPrice.setText("Current Price : " + formatter.format(response.getJSONObject(i).getJSONObject("sea").get("latest"))  + " zeny");
                        JSONArray jsonArray = response.getJSONObject(i).getJSONObject("sea").getJSONObject("week").getJSONArray("data");
                        for(int j = 0; j < jsonArray.length(); j++){
                            priceArray[j] = jsonArray.getJSONObject(j).getInt("price");
                            yValue.add(new Entry((float)j+1, (float)priceArray[j]));
                        }

                        tvLastPrice.setText("Previous Price : " + formatter.format(jsonArray.getJSONObject(5).getInt("price")) + " zeny");
                        String date =response.getJSONObject(i).getJSONObject("sea").get("latest_time").toString().replaceAll("T"," ").replaceAll("Z"," GMT/UTC Time");
                        if(!date.equals("")){
                            tvUpdated.setText("Last Updated : " + date);
                        }else{
                            tvUpdated.setText("Last Updated : " + response.getJSONObject(i).getJSONObject("sea").get("latest_time").toString());
                        }
                        LineDataSet set1 = new LineDataSet(yValue, "SEA Server");

                        set1.setFillAlpha(200);
                        set1.setColor(Color.BLUE);
                        set1.setLineWidth(2.5f);
                        set1.setCircleColor(Color.BLUE);
                        set1.setCircleHoleColor(Color.BLUE);
                        set1.setCircleRadius(5f);
                        set1.notifyDataSetChanged();

                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set1);

                        LineData data = new LineData(dataSets);
                        mChart.setData(data);
                        getSupportActionBar().setTitle(tvName.getText().toString().replaceAll("Name : ", ""));
                    }

                }
                catch (Exception e)
                {

                }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(DetailActivity.this, "Error while fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);


        }

    }

}
