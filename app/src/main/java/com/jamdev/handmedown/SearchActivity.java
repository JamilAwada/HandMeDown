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

    private EditText searchInput;
    private String searchTerm;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private List<Listing> listings = new ArrayList<>();
    private Adapter adapter;

    private int picture;

    private String getListingURL = "http://10.0.2.2/HandMeDown/listing_fetch_title.php?title=";
    private GetListingsAPI getListingsAPI;

    private String getUserURL = "http://10.0.2.2/HandMeDown/user_fetch_id.php?id=";
    private GetUserAPI getUserAPI;

    private Listing selectedItem;
    private User seller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);


        return view;

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initRecyclerView();

        searchInput = (EditText) view.findViewById(R.id.et_search);

        // Listens to any input
        searchInput.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                searchTerm = s.toString();
                getListingsAPI = new GetListingsAPI();
                getListingsAPI.execute(getListingURL + searchTerm);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

    // Get the listing's position and execute the API while passing in the listing's seller as a parameter
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
                JSONArray listJsonArray = new JSONArray(values);
                listings = new ArrayList<>();
                for (int i = 0; i < listJsonArray.length(); i++) {
                    JSONObject jsonItemObject = listJsonArray.getJSONObject(i);
                    String id = jsonItemObject.getString("id");
                    String title = jsonItemObject.getString("title");
                    String description = jsonItemObject.getString("description");
                    String price = jsonItemObject.getString("price");
                    String category = jsonItemObject.getString("category");
                    String seller = jsonItemObject.getString("seller");
                    String sellerName = jsonItemObject.getString("seller_name");
                    String posted_on = jsonItemObject.get("posted_on").toString();
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

                    Listing listing = new Listing(title, description, price, category, posted_on, seller, sellerName, picture);
                    listings.add(listing);
                }
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
                    String password = jsonItemObject.getString("password");
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

                    Intent goToExpanded = new Intent(getContext(), ListingExpandedActivity.class);
                    goToExpanded.putExtra("Listing", selectedItem);
                    goToExpanded.putExtra("Seller", seller);
                    startActivity(goToExpanded);

                }
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
        adapter = new Adapter(listings, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}
