package com.jamdev.handmedown;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import androidx.appcompat.app.AppCompatActivity;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class EditListingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText titleInput;
    EditText descriptionInput;
    EditText priceInput;
    Spinner categoryInput;
    RelativeLayout saveChanges;
    RelativeLayout deleteListing;
    ImageView returnButton;

    int picture;

    Spinner editDemo;

    ImageView listingPicture;

    ArrayAdapter<CharSequence> adapter2;

    String id = "";
    String title = "";
    String description = "";
    String price = "";
    String category = "";
    String fetchedPicture = "";

    String[] categories = { "Toys", "Clothing", "Electronics", "Gear", "Disposables", "Consumables" };

    private String editListingURL = "http://10.0.2.2/HandMeDown/listing_edit.php";
    EditListingAPI editListingAPI;

    private String deleteListingURL = "http://10.0.2.2/HandMeDown/listing_delete.php?ID=";
    private DeleteListingAPI deleteListingAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // These 3 lines hide the title and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_listing);

        listingPicture = (ImageView) findViewById(R.id.card_picture_container);

        titleInput = (EditText) findViewById(R.id.et_title);
        descriptionInput = (EditText) findViewById(R.id.et_description);
        priceInput = (EditText) findViewById(R.id.et_price);
        saveChanges = (RelativeLayout) findViewById(R.id.btn_save_changes);
        deleteListing = (RelativeLayout) findViewById(R.id.btn_delete_listing);
        returnButton = (ImageView) findViewById(R.id.btn_return);

        editDemo = (Spinner) findViewById(R.id.edit_DEMO);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.pictures, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editDemo.setAdapter(adapter2);

        // Category is selected through a spinner
        categoryInput = (Spinner) findViewById(R.id.spinner_categories);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, categories);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categoryInput.setAdapter(adapter);

        // Get listing details from Listings activity
        Bundle fromListings = getIntent().getExtras();
        id = fromListings.getString("Item ID");
        title = fromListings.getString("Item Title");
        description = fromListings.getString("Item Description");
        price = fromListings.getString("Item Price");
        category = fromListings.getString("Item Category");
        int spinnerPosition = adapter.getPosition(category);
        categoryInput.setSelection(spinnerPosition);

        // Set the pre-existing details
        titleInput.setText(title);
        descriptionInput.setText(description);
        priceInput.setText(price);

        // The save changes button executes the editListing() function
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editListing(view);
            }
        });

        deleteListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteListing();
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                returnToListings();
            }
        });

        editDemo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = editDemo.getSelectedItem().toString();
                if (item.equalsIgnoreCase("DEMO: Man 1")){
                    picture = R.drawable.demo_man1;
                }
                else if (item.equalsIgnoreCase("DEMO: Man 2")){
                    picture = R.drawable.demo_man2;
                }
                else if (item.equalsIgnoreCase("DEMO: Woman 1")){
                    picture = R.drawable.demo_woman1;
                }
                else if (item.equalsIgnoreCase("DEMO: Woman 2")){
                    picture = R.drawable.demo_woman2;
                }
                else if (item.equalsIgnoreCase("DEMO: Bear")){
                    picture = R.drawable.demo_bear;
                }
                else if (item.equalsIgnoreCase("DEMO: Onesie")){
                    picture = R.drawable.demo_onesie;
                }
                else if (item.equalsIgnoreCase("DEMO: Monitor")){
                    picture = R.drawable.demo_monitor;
                }
                else if (item.equalsIgnoreCase("DEMO: Stroller")){
                    picture = R.drawable.demo_stroller;
                }
                else if (item.equalsIgnoreCase("DEMO: Diapers")){
                    picture = R.drawable.demo_diapers;
                }
                else if (item.equalsIgnoreCase("DEMO: Formula")){
                    picture = R.drawable.demo_formula;
                }
                else {
                    picture = R.drawable.no_picture;
                }
                listingPicture.setImageResource(picture);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void editListing(View view) {

        // Collect the entered values
        title = titleInput.getText().toString();
        description = descriptionInput.getText().toString();
        price = priceInput.getText().toString();
        category = categoryInput.getSelectedItem().toString();

        // Check for a complete form
        if (title.equalsIgnoreCase("")) {
            Toast.makeText(this, "Incomplete form. Please fill in the title.", Toast.LENGTH_SHORT).show();
        }
        else if (price.equalsIgnoreCase("")){
            Toast.makeText(this, "Incomplete form. Please set a price.", Toast.LENGTH_SHORT).show();
        }
        else {
            editListingAPI = new EditListingAPI();
            editListingAPI.execute();
            returnToListings();

        }



    }

    class EditListingAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpClient http_client = new DefaultHttpClient();
            HttpPost http_post = new HttpPost(editListingURL);

            // Name value pairs for POST in PHP file
            BasicNameValuePair titleParam = new BasicNameValuePair("Title", title);
            BasicNameValuePair descriptionParam = new BasicNameValuePair("Description", description);
            BasicNameValuePair priceParam = new BasicNameValuePair("Price", price);
            BasicNameValuePair categoryParam = new BasicNameValuePair("Category", category);
            BasicNameValuePair idParam = new BasicNameValuePair("ID", id);
            BasicNameValuePair pictureParam = new BasicNameValuePair("Picture", editDemo.getSelectedItem().toString());
            ArrayList<NameValuePair> name_value_pair_list = new ArrayList<>();
            name_value_pair_list.add(titleParam);
            name_value_pair_list.add(descriptionParam);
            name_value_pair_list.add(priceParam);
            name_value_pair_list.add(categoryParam);
            name_value_pair_list.add(idParam);
            name_value_pair_list.add(pictureParam);


            try {
                // Send the list in an encoded form entity
                UrlEncodedFormEntity url_encoded_form_entity = new UrlEncodedFormEntity(name_value_pair_list);

                // Set the entity in the http_post object
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s.equalsIgnoreCase("Listing info updated.")) {
                    Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




    }

    public class DeleteListingAPI extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {

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

    public void returnToListings(){
        onBackPressed();
        onBackPressed();
    }

    public void deleteListing(){
        deleteListingAPI = new DeleteListingAPI();
        deleteListingAPI.execute(deleteListingURL + id);
        returnToListings();
    }

}