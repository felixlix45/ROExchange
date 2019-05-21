package com.example.roexchange;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.roexchange.adapter.ItemAdapter;
import com.example.roexchange.model.Item;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnSearch;
    EditText etSearch;
    RecyclerView rvItem;
    ArrayList<Item> listItem;
    private Context context;
    String URL ="https://www.romexchange.com/api/items.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        listItem = new ArrayList<>();
        final ItemAdapter itemAdapter =new ItemAdapter(this,listItem);
        btnSearch = findViewById(R.id.btnSearch);
        rvItem = findViewById(R.id.rvItem);
        etSearch = findViewById(R.id.etSearchItem);
        rvItem.setLayoutManager(new LinearLayoutManager(this));
        rvItem.setAdapter(itemAdapter);



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = ProgressDialog.show(context, null, "Fetching data, please wait");
//                Toast.makeText(MainActivity.this, "CLICKED", Toast.LENGTH_SHORT).show();
                String name = etSearch.getText().toString().replaceAll("\\s+","%20");
//                Toast.makeText(context, name, Toast.LENGTH_LONG).show();
                if(!etSearch.equals("")){
                    itemAdapter.clear();
                    JsonArrayRequest request = new JsonArrayRequest(
                            Request.Method.GET,
                            "https://www.romexchange.com/api?exact=false&item=" +name ,
                            null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    dialog.dismiss();
                                    try{
                                        for(int i = 0; i <response.length(); i++){
                                            Item item = new Item();
                                            item.setName(response.getJSONObject(i).get("name").toString());
                                            item.setTypes(typeConvert(response.getJSONObject(i).getInt("type")));
                                            listItem.add(item);

                                        }
                                        itemAdapter.notifyDataSetChanged();
                                    }
                                    catch (Exception e){

                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    final RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(request);
                }else{
                    Toast.makeText(MainActivity.this, "Null", Toast.LENGTH_SHORT).show();
                }

            }


        });

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

                            for(int i = 0; i <response.length(); i++){
                                Item item = new Item();
                                item.setName(response.getJSONObject(i).get("name").toString());
                                item.setTypes(typeConvert(response.getJSONObject(i).getInt("type")));
//                                detailJson();
                                //                                item.setType(response.getJSONObject(i).getInt("type"));
//                                item.setLastPrice(response.getJSONObject(i).getJSONObject("sea").getInt("latest"));
//                                item.setLastDate(response.getJSONObject(i).getJSONObject("sea").get("latest_time").toString());
                                listItem.add(item);
                            }
                            itemAdapter.notifyDataSetChanged();
                        }
                        catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }


//    public void detailJson(){
//        final ProgressDialog dialog = ProgressDialog.show(context, null, "Fetching data, please wait");
//        JsonArrayRequest request = new JsonArrayRequest(
//                Request.Method.GET,
//                "https://www.romexchange.com/api?exact=false&item=" +name ,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        dialog.dismiss();
//                        try{
//                            for(int i = 0; i <response.length(); i++){
//                                Item item = new Item();
//                                item.setName(response.getJSONObject(i).get("name").toString());
//                                int type = response.getJSONObject(i).getInt("type");
//
//                                item.setType(response.getJSONObject(i).getInt("type"));
//                                item.setLastPrice(response.getJSONObject(i).getJSONObject("sea").getInt("latest"));
//                                item.setLastDate(response.getJSONObject(i).getJSONObject("sea").get("latest_time").toString());
//                                listItem.add(item);
//
//                            }
//                            itemAdapter.notifyDataSetChanged();
//                        }
//                        catch (Exception e){
//
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        dialog.dismiss();
//                        Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//    }
    public String typeConvert(int type){
        switch (type){
            case 1:
                return "Weapon";
            case 2 :
                return "Off-hand";
            case 3:
                return "Armors";
            case 4:
                return  "Garmantes";
            case 5:
                return "Footgears";
            case 6:
                return "Accessory";
            case 7:
                return "Blueprint";
            case 8:
                return "Potion / Effect";
            case 9:
                return "Refine";
            case 10:
                return "Scroll / Album";
            case 11:
                return "Material";
            case 12:
                return "Holiday Material";
            case 13:
                return "Pet Material";
            case 14:
                return "Premium";
            case 15:
                return "Costume";
            case 16:
                return "Head";
            case 17:
                return "Face";
            case 18:
                return "Back";
            case 19:
                return "Mouth";
            case 20:
                return "Tail";
            case 21:
                return "Weapon Card";
            case 22:
                return "Off-Hand Card";
            case 23:
                return "Armor Card";
            case 24:
                return "Garments Card";
            case 25:
                return "Shoe Card";
            case 26:
                return "Accessory Card";
            case 27:
                return "Headwear Card";

        }
        return "NULL";
    }
}
