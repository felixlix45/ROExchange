package com.example.roexchange;

import android.app.ProgressDialog;
import android.icu.text.DateTimePatternGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvName = findViewById(R.id.tvDetailName);
        tvTypes = findViewById(R.id.tvDetailTypes);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvUpdated = findViewById(R.id.tvDetailUpdated);
        tvLastPrice = findViewById(R.id.tvDetailBeforeUpdatePrice);
        mChart =findViewById(R.id.LineChart);

//        mChart.setOnChartGestureListener(this);
//        mChart.setOnChartValueSelectedListener(this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(true);


        if(getIntent().hasExtra("URL") && getIntent().hasExtra("Types")){

            final ArrayList<Entry> yValue = new ArrayList<>();
            final int[] priceArray = new int[7];

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
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddThh:mm:ssZ");

                                for(int i = 0; i < response.length(); i++){
                                    tvName.setText("Name : " + response.getJSONObject(i).get("name").toString());
                                    tvTypes.setText("Types : " + getIntent().getStringExtra("Types"));

                                    tvPrice.setText("Current Price : " + formatter.format(response.getJSONObject(i).getJSONObject("sea").get("latest"))  + " zeny");
                                    JSONArray jsonArray = response.getJSONObject(i).getJSONObject("sea").getJSONObject("week").getJSONArray("data");
                                    for(int j = 0; j < jsonArray.length(); j++){
                                        priceArray[j] = jsonArray.getJSONObject(j).getInt("price");
                                        Log.i("PRICE", String.valueOf(jsonArray.getJSONObject(j).getInt("price")));
                                        yValue.add(new Entry((float)j+1, (float)priceArray[j]));
                                        Log.i("HARGA", String.valueOf(priceArray[j]));
                                    }

                                    tvLastPrice.setText("Previous Price : " + formatter.format(jsonArray.getJSONObject(5).getInt("price")) + " zeny");
                                    String date =response.getJSONObject(i).getJSONObject("sea").get("latest_time").toString().replaceAll("T"," ").replaceAll("Z"," GMT/UTC Time");
                                    if(!date.equals("")){
                                        tvUpdated.setText("Last Updated : " + date);
                                    }else{
                                        tvUpdated.setText("Last Updated : " + response.getJSONObject(i).getJSONObject("sea").get("latest_time").toString());
                                    }
                                }
                                LineDataSet set1 = new LineDataSet(yValue, "SEA Server");

                                set1.setFillAlpha(200);

                                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                dataSets.add(set1);

                                LineData data = new LineData(dataSets);
                                mChart.setData(data);
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
