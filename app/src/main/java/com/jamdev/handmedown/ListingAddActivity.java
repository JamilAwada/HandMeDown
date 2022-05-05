package com.jamdev.handmedown;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ListingAddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText titleInput;
    private EditText descriptionInput;
    private EditText priceInput;
    private Spinner categoryInput;
    private Spinner addDemo;
    private RelativeLayout addListingButton;
    private ImageView returnButton;

    private String listingTitle = "";
    private String listingDescription = "";
    private String listingPrice = "";
    private String listingCategory = "";
    private ImageView pictureInput;
    private int picture;

    private ArrayAdapter<CharSequence> demoAdapter;

    // This was explicitly called because the item categories are fixed
    private String[] categories = {"Toys", "Clothing", "Electronics", "Gear", "Disposables", "Consumables"};

    private String addListingURL = "http://10.0.2.2/HandMeDown/listing_add.php";
    private AddListingAPI addListingAPI;
    // We need the userID to add the listing with the corresponding seller
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // These 3 lines hide the title and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_listing);


        // Get listing details from input fields
        titleInput = (EditText) findViewById(R.id.et_title);
        descriptionInput = (EditText) findViewById(R.id.et_description);
        priceInput = (EditText) findViewById(R.id.et_price);
        pictureInput = (ImageView) findViewById(R.id.card_picture_container);
        addListingButton = (RelativeLayout) findViewById(R.id.btn_add_new);
        returnButton = (ImageView) findViewById(R.id.btn_return);

        // Category is selected through a spinner
        categoryInput = (Spinner) findViewById(R.id.spinner_categories);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, categories);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categoryInput.setAdapter(adapter);

        // Same protocol for demo adapter
        addDemo = (Spinner) findViewById(R.id.add_DEMO);
        demoAdapter = ArrayAdapter.createFromResource(this, R.array.pictures, android.R.layout.simple_spinner_item);
        demoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addDemo.setAdapter(demoAdapter);

        // Get user ID from main, and because it's static, any new listings will be made only to this user
        // And so an API call is not required
        Bundle bundle = getIntent().getExtras();
        userID = bundle.getString("ID");

        // The add listing button executes the addListing() function
        addListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addListing(view);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToListings();
            }
        });

        // Apologies for this implementation, but I tried using Glide instead and it kept crashing my laptop
        // In no real world scenario would this be used, but for testing purposes it will have to suffice
        addDemo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPicture = addDemo.getSelectedItem().toString();
                if (selectedPicture.equalsIgnoreCase("DEMO: Man 1")) {
                    picture = R.drawable.demo_man1;
                } else if (selectedPicture.equalsIgnoreCase("DEMO: Man 2")) {
                    picture = R.drawable.demo_man2;
                } else if (selectedPicture.equalsIgnoreCase("DEMO: Woman 1")) {
                    picture = R.drawable.demo_woman1;
                } else if (selectedPicture.equalsIgnoreCase("DEMO: Woman 2")) {
                    picture = R.drawable.demo_woman2;
                } else if (selectedPicture.equalsIgnoreCase("DEMO: Bear")) {
                    picture = R.drawable.demo_bear;
                } else if (selectedPicture.equalsIgnoreCase("DEMO: Onesie")) {
                    picture = R.drawable.demo_onesie;
                } else if (selectedPicture.equalsIgnoreCase("DEMO: Monitor")) {
                    picture = R.drawable.demo_monitor;
                } else if (selectedPicture.equalsIgnoreCase("DEMO: Stroller")) {
                    picture = R.drawable.demo_stroller;
                } else if (selectedPicture.equalsIgnoreCase("DEMO: Diapers")) {
                    picture = R.drawable.demo_diapers;
                } else if (selectedPicture.equalsIgnoreCase("DEMO: Formula")) {
                    picture = R.drawable.demo_formula;
                } else {
                    picture = R.drawable.no_picture;
                }
                pictureInput.setImageResource(picture);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void addListing(View view) {

        // Collect the entered values
        listingTitle = titleInput.getText().toString();
        listingDescription = descriptionInput.getText().toString();
        listingPrice = priceInput.getText().toString();
        listingCategory = categoryInput.getSelectedItem().toString();

        // Check for a complete form
        if (listingTitle.equalsIgnoreCase("")) {
            Toast.makeText(this, "Incomplete form. Please fill in the title.", Toast.LENGTH_SHORT).show();
        } else if (listingPrice.equalsIgnoreCase("")) {
            Toast.makeText(this, "Incomplete form. Please set a price.", Toast.LENGTH_SHORT).show();
        } else {
            // Execute API
            addListingAPI = new AddListingAPI();
            addListingAPI.execute();
            returnToListings();
        }

    }

    class AddListingAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpClient http_client = new DefaultHttpClient();
            HttpPost http_post = new HttpPost(addListingURL);

            // Name value pairs for POST in PHP file
            BasicNameValuePair titleParam = new BasicNameValuePair("Title", listingTitle);
            BasicNameValuePair descriptionParam = new BasicNameValuePair("Description", listingDescription);
            BasicNameValuePair priceParam = new BasicNameValuePair("Price", listingPrice);
            BasicNameValuePair categoryParam = new BasicNameValuePair("Category", listingCategory);
            BasicNameValuePair sellerParam = new BasicNameValuePair("Seller", userID);
            BasicNameValuePair pictureParam = new BasicNameValuePair("Picture", addDemo.getSelectedItem().toString());
            ArrayList<NameValuePair> name_value_pair_list = new ArrayList<>();
            name_value_pair_list.add(titleParam);
            name_value_pair_list.add(descriptionParam);
            name_value_pair_list.add(priceParam);
            name_value_pair_list.add(categoryParam);
            name_value_pair_list.add(sellerParam);
            name_value_pair_list.add(pictureParam);


            try {
                // Send the list in an encoded form entity
                UrlEncodedFormEntity url_encoded_form_entity = new UrlEncodedFormEntity(name_value_pair_list);

                // Set the entity holding the values in the http_post object
                http_post.setEntity(url_encoded_form_entity);

                // Get API response and return it as a string
                HttpResponse http_response = http_client.execute(http_post);
                InputStream input_stream = http_response.getEntity().getContent();
                InputStreamReader input_stream_reader = new InputStreamReader(input_stream);
                BufferedReader buffered_reader = new BufferedReader(input_stream_reader);
                StringBuilder string_builder = new StringBuilder();
                String buffered_str_chunk = null;
                while ((buffered_str_chunk = buffered_reader.readLine()) != null) {
                    string_builder.append(buffered_str_chunk);
                }
                return string_builder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String values) {
            super.onPostExecute(values);
            try {
                if (values.equalsIgnoreCase("Listing published.")) {
                    Toast.makeText(getApplicationContext(), values, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), values, Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), values, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // I understand this is bad practice, but moving from an activity back to a fragment is a herculean task
    public void returnToListings() {
        finish();
    }

}