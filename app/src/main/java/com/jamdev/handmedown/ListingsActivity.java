package com.jamdev.handmedown;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListingsActivity extends Fragment  {

    RelativeLayout addListing;
    RelativeLayout refreshBtn;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<Listing> itemList = new ArrayList<>();
    AdapterEditable adapter;
    private GetListingsAPI getListingsAPI;


    private String getListing_url = "http://10.0.2.2/HandMeDown/listing_fetch_seller.php?id=";

    String userID;

    ImageView edit_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_listings, container, false);

        addListing = (RelativeLayout) view.findViewById(R.id.btn_new_listing);
        refreshBtn = (RelativeLayout) view.findViewById(R.id.btn_refresh_listings);

        refreshBtn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               refresh(view);
           }
        });

        addListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddListing(view);
            }
        });

        userID = getArguments().getString("Id");
        getListing_url = getListing_url + userID;
        getListingsAPI = new GetListingsAPI();
        getListingsAPI.execute(getListing_url);


        return view;

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        initRecyclerView();
    }



    public class GetListingsAPI extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            // URL and HTTP initialization to connect to API 2
            URL url;
            HttpURLConnection http;

            try {
                // Connect to API 2
                url = new URL(urls[0]);
                http = (HttpURLConnection) url.openConnection();

                // Retrieve API 2 content
                InputStream in = http.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                // Read API 2 content line by line
                BufferedReader br = new BufferedReader(reader);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

                br.close();
                // Return content from API 2
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String values) {
            super.onPostExecute(values);
            try {

                JSONArray listJsonArray = new JSONArray(values);

                for(int i=0;i<listJsonArray.length();i++){
                    JSONObject jsonItemObject = listJsonArray.getJSONObject(i);
                    String id = jsonItemObject.getString("id");
                    String title = jsonItemObject.getString("title");
                    String description = jsonItemObject.getString("description");
                    String price = jsonItemObject.getString("price");
                    String category = jsonItemObject.getString("category");
                    String seller = jsonItemObject.getString("seller");
                    String posted_on = jsonItemObject.get("posted_on").toString();
                    int pictures = R.drawable.placeholder2;


                    Listing listing = new Listing(id, title,description,price,category,seller,posted_on,pictures);
                    itemList.add(listing);
                }
                initRecyclerView();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }




    // Initialize recyclerView function
    private void initRecyclerView() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.container_listings);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdapterEditable(getContext(), itemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void goToAddListing(View view){
        Intent goToAddListing = new Intent(getContext(), AddListingActivity.class);
        goToAddListing.putExtra("ID", userID);
        startActivity(goToAddListing);
    }


    public void refresh(View view){
        itemList.clear();
        GetListingsAPI api = new GetListingsAPI();
        api.execute(getListing_url);
    }
}
