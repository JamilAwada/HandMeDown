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
import java.util.ArrayList;

public class EditListingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText titleInput;
    EditText descriptionInput;
    EditText priceInput;
    Spinner categoryInput;
    RelativeLayout saveChanges;

    String id = "";
    String title = "";
    String description = "";
    String price = "";
    String category = "";

    listingEditAPI API;

    String JSONObject;

    private String URL = "http://10.0.2.2/HandMeDown/listing_edit.php";
//    listingAddAPI API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // These 3 lines hide the title and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_listing);

        titleInput = (EditText) findViewById(R.id.et_title);
        descriptionInput = (EditText) findViewById(R.id.et_description);
        priceInput = (EditText) findViewById(R.id.et_price);
        saveChanges = (RelativeLayout) findViewById(R.id.btn_save_changes);

        categoryInput = (Spinner) findViewById(R.id.spinner_categories);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categoryInput.setAdapter(adapter);

        Bundle intent = getIntent().getExtras();
        id = intent.getString("Item ID");
        title = intent.getString("Item Title");
        description = intent.getString("Item Description");
        price = intent.getString("Item Price");
        category = intent.getString("Item Category");
        int spinnerPosition = adapter.getPosition(category);
        categoryInput.setSelection(spinnerPosition);

        titleInput.setText(title);
        descriptionInput.setText(description);
        priceInput.setText(price);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editListing(view);
            }
        });
    }

    public void editListing(View view) {

        title = titleInput.getText().toString();
        description = descriptionInput.getText().toString();
        price = priceInput.getText().toString();
        category = categoryInput.getSelectedItem().toString();

        if (title.equalsIgnoreCase("")) {
            Toast.makeText(this, "Incomplete form. Please fill in the title.", Toast.LENGTH_SHORT).show();
        }
        else if (price.equalsIgnoreCase("")){
            Toast.makeText(this, "Incomplete form. Please set a price.", Toast.LENGTH_SHORT).show();
        }
        else {
            API = new listingEditAPI();
            API.execute();

        }



    }

    class listingEditAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpClient http_client = new DefaultHttpClient();
            HttpPost http_post = new HttpPost(URL);

            BasicNameValuePair titleParam = new BasicNameValuePair("Title", title);
            BasicNameValuePair descriptionParam = new BasicNameValuePair("Description", description);
            BasicNameValuePair priceParam = new BasicNameValuePair("Price", price);
            BasicNameValuePair categoryParam = new BasicNameValuePair("Category", category);
            BasicNameValuePair idParam = new BasicNameValuePair("ID", id);
            ArrayList<NameValuePair> name_value_pair_list = new ArrayList<>();
            name_value_pair_list.add(titleParam);
            name_value_pair_list.add(descriptionParam);
            name_value_pair_list.add(priceParam);
            name_value_pair_list.add(categoryParam);
            name_value_pair_list.add(idParam);


            try {
                // This is used to send the list with the api in an encoded form entity
                UrlEncodedFormEntity url_encoded_form_entity = new UrlEncodedFormEntity(name_value_pair_list);

                // This sets the entity (which holds the list of values) in the http_post object
                http_post.setEntity(url_encoded_form_entity);

                // This gets the response from the post api and returns a string of the response.
                HttpResponse http_response = http_client.execute(http_post);
                InputStream input_stream = http_response.getEntity().getContent();
                InputStreamReader input_stream_reader = new InputStreamReader(input_stream);
                BufferedReader buffered_reader = new BufferedReader(input_stream_reader);
                StringBuilder string_builder = new StringBuilder();
                String buffered_str_chunk = null;
                while ((buffered_str_chunk = buffered_reader.readLine()) != null) {
                    string_builder.append(buffered_str_chunk);
                }
                Log.i("result", string_builder.toString());
                JSONObject = string_builder.toString();
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
                    Log.i("json", JSONObject);
                }
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
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

    public void returnToListings(View view){
        onBackPressed();
        onBackPressed();
    }

}