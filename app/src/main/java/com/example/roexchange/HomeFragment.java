package com.example.roexchange;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.roexchange.R;
import com.example.roexchange.adapter.ItemAdapter;
import com.example.roexchange.model.Item;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    Button btnSearch, btnReset;
    EditText etSearch;
    RecyclerView rvItem;
    ArrayList<Item> listItem = new ArrayList<>();
    ArrayList<Item> copyList = new ArrayList<>();
    ArrayList<Item> savedList = new ArrayList<>();
    CheckBox cbFilter;
    Spinner spinnerFilter;
    ProgressBar progressBar;
    private Context context;
    String URL ="https://www.romexchange.com/api/items.json";
    final ItemAdapter itemAdapter = new ItemAdapter(getActivity(), listItem);

    ShimmerFrameLayout shimmerFrameLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        shimmerFrameLayout = v.findViewById(R.id.shimmer_container);
        shimmerFrameLayout.startShimmer();
        btnSearch = v.findViewById(R.id.btnSearch);
        btnReset = v.findViewById(R.id.reset);
        etSearch = v.findViewById(R.id.etSearchItem);

        rvItem = v.findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvItem.setAdapter(itemAdapter);



        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setMax(100);

        cbFilter = v.findViewById(R.id.cbFilter);
        spinnerFilter = v.findViewById(R.id.spinnerFilter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            final ProgressDialog dialog = ProgressDialog.show(context, null, "Fetching data, please wait");
                progressBar.setVisibility(View.VISIBLE);
                String name = etSearch.getText().toString().replaceAll("\\s+","%20");
                if(!etSearch.equals("")){
                    itemAdapter.clear();
                    if(!cbFilter.isChecked()){
                        JsonArrayRequest request = new JsonArrayRequest(
                                Request.Method.GET,
                                "https://www.romexchange.com/api?exact=false&item=" +name ,
                                null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        progressBar.setVisibility(View.GONE);
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
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Error occured", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        requestQueue.add(request);
                    }else{
                        getFilteredData();
                    }


                }else{

                    Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
                }
                etSearch.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }


        });

        cbFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                getFilteredData();
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
                cbFilter.setChecked(false);
                etSearch.setText("");
                getAllData();
            }
        });

        if(itemAdapter.size() == 0){
            getAllData();
        }
        return v;
    }

    public void getFilteredData(){
        itemAdapter.clear();
        final int pos = spinnerFilter.getSelectedItemPosition()+1;
        String URLFiltered = "https://www.romexchange.com/api/items.json";//exact=false&item=" + etSearch.getText().toString().replaceAll("\\s+","%20") + "&type=" + pos;
//        final ProgressDialog dialog = ProgressDialog.show(getActivity(), null, "Fetching data, please wait");
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URLFiltered,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
//                        dialog.dismiss();
                        try{

                            for(int i = 0; i <response.length(); i++){
                                if(response.getJSONObject(i).getInt("type") == pos){
                                    Item item = new Item();
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
//                        dialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
    public void getAllData(){

//        final ProgressDialog dialog = ProgressDialog.show(getActivity(), null, "Fetching data, please wait");
        shimmerFrameLayout.startShimmer();
        progressBar.setVisibility(View.VISIBLE);
        itemAdapter.clear();
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
//                        dialog.dismiss();
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
                        progressBar.setVisibility(View.GONE);
//                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
    public String typeConvert(int type) {
        switch (type) {
            case 1:
                return "Weapon";
            case 2:
                return "Off-hand";
            case 3:
                return "Armors";
            case 4:
                return "Garmantes";
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
