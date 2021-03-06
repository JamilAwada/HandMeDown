package com.jamdev.handmedown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements Adapter.OnListingListener {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private List<Listing> listings = new ArrayList<>();
    private Adapter adapter;
    // Temporary buffer for every listing's picture to load into
    private int picture;
    private Listing selectedItem;

    private RelativeLayout headerBackground;
    private TextView categoryView;
    private ImageView returnButton;

    // Variables to be sent to the ListingExpandedActivity
    private String category = "";
    private User seller;

    private String getListingURL = "http://10.0.2.2/HandMeDown/listing_fetch_category.php?category=";
    private GetListingsAPI getListingsAPI;

    private String getUserURL = "http://10.0.2.2/HandMeDown/user_fetch_id.php?id=";
    private GetUserAPI getUserAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // These 3 lines hide the title and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_category);

        // Receive the selected category from the home fragment and execute the API to fetch all items belonging to that category
        category = getIntent().getExtras().getString("Category");
        getListingsAPI = new GetListingsAPI();
        getListingsAPI.execute(getListingURL + category);

        headerBackground = (RelativeLayout) findViewById(R.id.bg_header);
        returnButton = (ImageView) findViewById(R.id.btn_return);


        // Set the text to reflect the category
        categoryView = (TextView) findViewById(R.id.tx_category);
        categoryView.setText(category);

        // Change the header's background depending on the selected category
        // This is much more efficient resource-wise than creating seperate activities for each category
        // As the only real difference between categories is merely semantic
        switch (category) {
            case "Toys":
                headerBackground.setBackgroundResource(R.drawable.category_header_blue);
                break;
            case "Clothing":
                headerBackground.setBackgroundResource(R.drawable.category_header_yellow);
                break;
            case "Electronics":
                headerBackground.setBackgroundResource(R.drawable.category_header_red);
                break;
            case "Gear":
                headerBackground.setBackgroundResource(R.drawable.category_header_green);
                break;
            case "Disposables":
                headerBackground.setBackgroundResource(R.drawable.category_header_navy);
                break;
            case "Consumables":
                headerBackground.setBackgroundResource(R.drawable.category_header_pink);
                break;
        }

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToHome(view);
            }
        });

    }

    // Clicking on a listing gets you its position in the list and executes the API with that listing's seller ID as a parameter
    @Override
    public void OnListingClick(int position) {
        selectedItem = listings.get(position);
        getUserAPI = new GetUserAPI();
        getUserAPI.execute(getUserURL + selectedItem.getSeller());
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

                JSONArray listingJsonArray = new JSONArray(values);

                // Retrieve JSON object array and decompose it
                for (int i = 0; i < listingJsonArray.length(); i++) {
                    JSONObject jsonItemObject = listingJsonArray.getJSONObject(i);
                    String title = jsonItemObject.getString("title");
                    String description = jsonItemObject.getString("description");
                    String price = jsonItemObject.getString("price");
                    String category = jsonItemObject.getString("category");
                    String seller = jsonItemObject.getString("seller");
                    String sellerName = jsonItemObject.getString("seller_name");
                    String posted_on = jsonItemObject.get("posted_on").toString();
                    String fetchedPicture = jsonItemObject.getString("picture");

                    // I really wish Glide worked
                    if (fetchedPicture.equalsIgnoreCase("DEMO: Man 1")) {
                        picture = R.drawable.demo_man1;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Man 2")) {
                        picture = R.drawable.demo_man2;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Woman 1")) {
                        picture = R.drawable.demo_woman1;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Woman 2")) {
                        picture = R.drawable.demo_woman2;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Bear")) {
                        picture = R.drawable.demo_bear;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Onesie")) {
                        picture = R.drawable.demo_onesie;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Monitor")) {
                        picture = R.drawable.demo_monitor;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Stroller")) {
                        picture = R.drawable.demo_stroller;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Diapers")) {
                        picture = R.drawable.demo_diapers;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Formula")) {
                        picture = R.drawable.demo_formula;
                    } else {
                        picture = R.drawable.no_picture;
                    }

                    Listing listing = new Listing(title, description, price, category, posted_on, seller, sellerName, picture);

                    listings.add(listing);
                }
                // Initialize recyclerview with new listings list
                initRecyclerView();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public class GetUserAPI extends AsyncTask<String, Void, String> {
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

                JSONArray listJsonArray = new JSONArray(values);

                for (int i = 0; i < listJsonArray.length(); i++) {

                    JSONObject jsonItemObject = listJsonArray.getJSONObject(i);
                    String id = jsonItemObject.getString("id");
                    String username = jsonItemObject.getString("username");
                    String name = jsonItemObject.getString("name");
                    String email = jsonItemObject.getString("email");
                    String number = jsonItemObject.getString("number");
                    String address = jsonItemObject.get("address").toString();
                    String fetchedPicture = jsonItemObject.getString("picture");
                    if (fetchedPicture.equalsIgnoreCase("DEMO: Man 1")) {
                        picture = R.drawable.demo_man1;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Man 2")) {
                        picture = R.drawable.demo_man2;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Woman 1")) {
                        picture = R.drawable.demo_woman1;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Woman 2")) {
                        picture = R.drawable.demo_woman2;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Bear")) {
                        picture = R.drawable.demo_bear;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Onesie")) {
                        picture = R.drawable.demo_onesie;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Monitor")) {
                        picture = R.drawable.demo_monitor;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Stroller")) {
                        picture = R.drawable.demo_stroller;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Diapers")) {
                        picture = R.drawable.demo_diapers;
                    } else if (fetchedPicture.equalsIgnoreCase("DEMO: Formula")) {
                        picture = R.drawable.demo_formula;
                    } else {
                        picture = R.drawable.no_picture;
                    }

                    // Get the listing and the seller, and send to the expanded listing view class
                    seller = new User(id, name, number, address, username, email, picture);

                    Intent goToExpanded = new Intent(getApplicationContext(), ListingExpandedActivity.class);
                    goToExpanded.putExtra("Listing", selectedItem);
                    goToExpanded.putExtra("Seller", seller);
                    startActivity(goToExpanded);
                    finish();

                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }


    // Initialize recyclerView function
    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.container_listings);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(listings, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void returnToHome(View view) {
        finish();
    }
}