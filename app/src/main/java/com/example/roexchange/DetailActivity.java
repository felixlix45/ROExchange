package com.example.roexchange;

import android.app.ProgressDialog;
import android.icu.text.DateTimePatternGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DetailActivity extends AppCompatActivity {

    TextView tvName, tvTypes, tvPrice, tvUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvName = findViewById(R.id.tvDetailName);
        tvTypes = findViewById(R.id.tvDetailTypes);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvUpdated = findViewById(R.id.tvDetailUpdated);

        if(getIntent().hasExtra("URL") && getIntent().hasExtra("Types")){
            
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
                                DateTimeFormatter parser = DateTimeFormatter.ISO_DATE_TIME;

                                for(int i = 0; i < response.length(); i++){
                                    tvName.setText("Name : " + response.getJSONObject(i).get("name").toString());
                                    tvTypes.setText("Types : " + getIntent().getStringExtra("Types"));
                                    tvPrice.setText("Price : " + formatter.format(response.getJSONObject(i).getJSONObject("sea").get("latest"))  + " zeny");
//
                                    tvUpdated.setText("Last Updated : " + response.getJSONObject(i).getJSONObject("sea").get("latest_time").toString());
                                    //(response.getJSONObject(i).getJSONObject("sea").get("latest_time").toString());
                                }
                            }
                            catch (Exception e)
                            {

                            }


//                                   item.setName(response.getJSONObject(i).get("name").toString());
//                                int type = response.getJSONObject(i).getInt("type");
//
//                                item.setType(response.getJSONObject(i).getInt("type"));
//                                item.setLastPrice(response.getJSONObject(i).getJSONObject("sea").getInt("latest"));
//                                item.setLastDate(response.getJSONObject(i).getJSONObject("sea").get("latest_time").toString());
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
