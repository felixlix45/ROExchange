package com.example.roexchange;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnSearch, btnReset;
    EditText etSearch;
    RecyclerView rvItem;
    ArrayList<Item> listItem = new ArrayList<>();
    ArrayList<Item> copyList = new ArrayList<>();
    CheckBox cbFilter;
    Spinner spinnerFilter;
    private Context context;
    String URL ="https://www.romexchange.com/api/items.json";
    final ItemAdapter itemAdapter = new ItemAdapter(this,listItem);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        btnSearch = findViewById(R.id.btnSearch);
        btnReset = findViewById(R.id.reset);
        rvItem = findViewById(R.id.rvItem);
        etSearch = findViewById(R.id.etSearchItem);
        rvItem.setLayoutManager(new LinearLayoutManager(this));
        rvItem.setAdapter(itemAdapter);
        cbFilter = findViewById(R.id.cbFilter);
        spinnerFilter = findViewById(R.id.spinnerFilter);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//            final ProgressDialog dialog = ProgressDialog.show(context, null, "Fetching data, please wait");
            String name = etSearch.getText().toString().replaceAll("\\s+","%20");
            if(!etSearch.equals("")){
                itemAdapter.clear();
                JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    "https://www.romexchange.com/api?exact=false&item=" +name ,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
//                                    dialog.dismiss();
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
//                                    dialog.dismiss();
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

        cbFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            itemAdapter.clear();
            if(cbFilter.isChecked()) {
                spinnerFilter.setVisibility(View.VISIBLE);
                getFilteredData();
            }else {
                spinnerFilter.setVisibility(View.INVISIBLE);
                getAllData();
            }
            }
        });

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                itemAdapter.clear();
                final ProgressDialog dialog = ProgressDialog.show(context, null, "Fetching data, please wait");

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
                                        if(cbFilter.isChecked()){
                                            if(response.getJSONObject(i).getInt("type") != spinnerFilter.getSelectedItemPosition()+1){
                                                continue;
                                            }else{
                                                item.setName(response.getJSONObject(i).get("name").toString());
                                                item.setTypes(typeConvert(response.getJSONObject(i).getInt("type")));
                                                listItem.add(item);
                                            }
                                        }else{
                                            item.setName(response.getJSONObject(i).get("name").toString());
                                            item.setTypes(typeConvert(response.getJSONObject(i).getInt("type")));
                                            listItem.add(item);
                                        }
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(context,  s, Toast.LENGTH_SHORT).show();
//                itemAdapter.filter(etSearch.getText().toString().toLowerCase(Locale.getDefault()));
            }


            @Override
            public void afterTextChanged(Editable s) {

//                JsonArrayRequest request = new JsonArrayRequest(
//                        Request.Method.GET,
//                        "https://www.romexchange.com/api?exact=false&item=" + s.toString() ,
//                        null,
//                        new Response.Listener<JSONArray>() {
//                            @Override
//                            public void onResponse(JSONArray response) {
//
//                                try{
//                                    for(int i = 0; i <response.length(); i++){
//                                        Item item = new Item();
//                                        item.setName(response.getJSONObject(i).get("name").toString());
//                                        item.setTypes(typeConvert(response.getJSONObject(i).getInt("type")));
//                                        listItem.add(item);
//                                    }
//                                    itemAdapter.notifyDataSetChanged();
//                                }
//                                catch (Exception e){
//
//                                }
//
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//
//                                Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                );
//                final RequestQueue requestQueue = Volley.newRequestQueue(context);
//                requestQueue.add(request);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemAdapter.clear();
                getAllData();
            }
        });

        getAllData();
    }

    public void getFilteredData(){
        final ProgressDialog dialog = ProgressDialog.show(context, null, "Fetching data, please wait");
        itemAdapter.clear();
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                    dialog.dismiss();
                    try{
//                        Toast.makeText(context, String.valueOf(response.length()), Toast.LENGTH_SHORT).show();
                        for(int i = 0; i <response.length(); i++){
                            Item item = new Item();
                            if(cbFilter.isChecked()){
                                if(response.getJSONObject(i).getInt("type") != spinnerFilter.getSelectedItemPosition()+1){
                                    continue;
                                }else{
                                    item.setName(response.getJSONObject(i).get("name").toString());
                                    item.setTypes(typeConvert(response.getJSONObject(i).getInt("type")));
                                    listItem.add(item);
                                }
                            }else{
                                item.setName(response.getJSONObject(i).get("name").toString());
                                item.setTypes(typeConvert(response.getJSONObject(i).getInt("type")));
                                listItem.add(item);
                            }
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
    public void getAllData(){
        final ProgressDialog dialog = ProgressDialog.show(this, null, "Fetching data, please wait");
        itemAdapter.clear();
        JsonArrayRequest request = new JsonArrayRequest(
            Request.Method.GET,
            URL,
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                dialog.dismiss();
                try{
//                    Toast.makeText(context, String.valueOf(response.length()), Toast.LENGTH_SHORT).show();
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
    }
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
