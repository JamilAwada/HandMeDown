package com.jamdev.handmedown;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class SearchActivity extends Fragment implements Adapter.OnListingListener {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<Listing> itemList = new ArrayList<>();
    Adapter adapter;
    private GetListingsAPI getListingsAPI;
    private String getListing_url = "http://10.0.2.2/HandMeDown/listing_fetch_title.php?title=";
    EditText searchInput;
    String searchTerm;

    private GetUserAPI getUserAPI;
    private String getUser_url = "http://10.0.2.2/HandMeDown/user_fetch_id.php?id=";
    Listing selectedItem;
    User seller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_search, container, false);



        return view;

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

        initRecyclerView();

        searchInput = (EditText) view.findViewById(R.id.search_input);

        searchInput.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                searchTerm = s.toString();
                getListingsAPI = new GetListingsAPI();
                getListingsAPI.execute(getListing_url + searchTerm);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

    }

    @Override
    public void OnListingClick(int position) {
        selectedItem = itemList.get(position);
        getUserAPI = new GetUserAPI();
        getUserAPI.execute(getUser_url + selectedItem.getSeller());
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
                Log.i("soiad", values);
                JSONArray listJsonArray = new JSONArray(values);
                itemList = new ArrayList<>();
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


                    Listing listing = new Listing(title,description,price,category,seller,posted_on,pictures);
                    itemList.add(listing);
                }
                initRecyclerView();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public class GetUserAPI extends AsyncTask<String, Void, String> {
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
                Log.i("message", values);
                JSONArray listJsonArray = new JSONArray(values);

                for(int i=0;i<listJsonArray.length();i++){
                    JSONObject jsonItemObject = listJsonArray.getJSONObject(i);
                    String id = jsonItemObject.getString("id");
                    String username = jsonItemObject.getString("username");
                    String password = jsonItemObject.getString("password");
                    String name = jsonItemObject.getString("name");
                    String email = jsonItemObject.getString("email");
                    String phoneNumber = jsonItemObject.getString("phone_number");
                    String address = jsonItemObject.get("address").toString();
                    int picture = R.drawable.no_picture;

                    seller = new User(name,phoneNumber,address,username,email,picture,id);
                    Intent intent = new Intent(getContext(), ListingExpanded.class);
                    intent.putExtra("Listing", selectedItem);
                    intent.putExtra("Seller", seller);
                    startActivity(intent);

                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }



    // Initialize recyclerView function
    private void initRecyclerView() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.listings_container);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(itemList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }




}
