package com.jamdev.handmedown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<Listing> itemList = new ArrayList<>();
    Adapter adapter;
    private GetListingsAPI getListingsAPI;
    private String getListing_url = "http://10.0.2.2/HandMeDown/listing_fetch_category.php?category=";
    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        category = getIntent().getExtras().getString("Category");
        getListingsAPI = new GetListingsAPI();
        getListingsAPI.execute(getListing_url + category);
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
                Log.i("message", values);
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


                    Listing listing = new Listing(title,description,price,category,seller,posted_on,pictures);
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
        recyclerView = (RecyclerView) findViewById(R.id.listings_container);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(itemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}