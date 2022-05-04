package com.jamdev.handmedown;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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
    RelativeLayout refreshButton;
    ImageView refreshIcon;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<Listing> itemList = new ArrayList<>();
    AdapterEditable adapter;

    RotateAnimation rotateLeft;

    private String getListingURL = "http://10.0.2.2/HandMeDown/listing_fetch_seller.php?id=";
    private GetListingsAPI getListingsAPI;

    String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_listings, container, false);

        addListing = (RelativeLayout) view.findViewById(R.id.btn_new_listing);
        refreshButton = (RelativeLayout) view.findViewById(R.id.btn_refresh_listings);
        refreshIcon = (ImageView) view.findViewById(R.id.image_refresh);

        rotateLeft = new RotateAnimation(0, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateLeft.setDuration(1000);
        rotateLeft.setInterpolator(new LinearInterpolator());


        refreshButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               refreshIcon.startAnimation(rotateLeft);
               refresh(view);
           }
        });

        addListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddListing(view);
            }
        });

        // Get all user listings on creation (I.E, search for all listings with foreign key matching the user's ID)
        userID = getArguments().getString("Id");
        getListingURL = getListingURL + userID;
        getListingsAPI = new GetListingsAPI();
        getListingsAPI.execute(getListingURL);

        return view;

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        initRecyclerView();
    }


    public class GetListingsAPI extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            // URL and HTTP initialization to connect to API
            URL url;
            HttpURLConnection http;

            try {
                // Connect to API
                url = new URL(urls[0]);
                http = (HttpURLConnection) url.openConnection();

                // Retrieve API content
                InputStream in = http.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                // Read API content line by line
                BufferedReader br = new BufferedReader(reader);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

                br.close();
                // Return content from API
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String values) {
            super.onPostExecute(values);
            try {
                Log.i("values", values);
                initRecyclerView();
                if (!values.equalsIgnoreCase("0")) {
                    JSONArray listJsonArray = new JSONArray(values);

                    for(int i = 0; i < listJsonArray.length(); i++){

                        JSONObject jsonItemObject = listJsonArray.getJSONObject(i);
                        String id = jsonItemObject.getString("id");
                        String title = jsonItemObject.getString("title");
                        String description = jsonItemObject.getString("description");
                        String price = jsonItemObject.getString("price");
                        String category = jsonItemObject.getString("category");
                        String seller = jsonItemObject.getString("seller");
                        String posted_on = jsonItemObject.get("posted_on").toString();
                        int picture = R.drawable.no_listing_picture;

                        Listing listing = new Listing(id, title,description,price,category,posted_on,seller,picture);


                        itemList.add(listing);
                }

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

    // Goes to the corresponding activity to enter listing details
    public void goToAddListing(View view){
        Intent goToAddListing = new Intent(getContext(), AddListingActivity.class);
        goToAddListing.putExtra("ID", userID);
        startActivity(goToAddListing);
    }

    // I originally did not want to do this, but i was left no choice. I was not able to call fragmentManager and commit() without losing data
    public void refresh(View view){
        itemList.clear();
        GetListingsAPI api = new GetListingsAPI();
        api.execute(getListingURL);
    }
}
