package com.jamdev.handmedown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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

public class SignUpActivity extends AppCompatActivity {

    EditText fullNameInput;
    EditText phoneNumberInput;
    EditText addressInput;
    EditText emailInput;
    EditText usernameInput;
    EditText passwordInput;

    String fullName = "";
    String phoneNumber = "";
    String address = "";
    String email = "";
    String username = "";
    String password = "";

    String JSONObject;

    private String URL = "http://10.0.2.2/HandMeDown/user_signup.php";
    userRegistrationAPI API;

    @Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    // These 3 lines hide the title and action bar
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    getSupportActionBar().hide();
    setContentView(R.layout.activity_signup);

    fullNameInput = (EditText) findViewById(R.id.et_name);
    phoneNumberInput =(EditText) findViewById(R.id.et_number);
    addressInput = (EditText) findViewById(R.id.et_address);
    emailInput = (EditText) findViewById(R.id.et_email);
    usernameInput = (EditText) findViewById(R.id.et_username);
    passwordInput = (EditText) findViewById(R.id.et_password);
    }

    public void registerUser(View view) {

        fullName = fullNameInput.getText().toString();
        phoneNumber = phoneNumberInput.getText().toString();
        address = addressInput.getText().toString();
        email = emailInput.getText().toString();
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();


        if (password.equalsIgnoreCase("") || username.equalsIgnoreCase("")
                || fullName.equalsIgnoreCase("") || email.equalsIgnoreCase("") || fullName.equalsIgnoreCase("") || address.equalsIgnoreCase("")) {
            Toast.makeText(this, "Incomplete form. Please fill in the blank fields.", Toast.LENGTH_SHORT).show();
        } else {
            API = new userRegistrationAPI();
            API.execute();
        }


    }

    class userRegistrationAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpClient http_client = new DefaultHttpClient();
            HttpPost http_post = new HttpPost(URL);


            BasicNameValuePair usernameParam = new BasicNameValuePair("Username", username);
            BasicNameValuePair passwordParam = new BasicNameValuePair("Password", password);
            BasicNameValuePair fullNameParam = new BasicNameValuePair("FullName", fullName);
            BasicNameValuePair emailParam = new BasicNameValuePair("Email", email);
            BasicNameValuePair addressParam = new BasicNameValuePair("Address", address);
            BasicNameValuePair phoneNumberParam = new BasicNameValuePair("PhoneNumber", phoneNumber);
            ArrayList<NameValuePair> name_value_pair_list = new ArrayList<>();
            name_value_pair_list.add(fullNameParam);
            name_value_pair_list.add(phoneNumberParam);
            name_value_pair_list.add(addressParam);
            name_value_pair_list.add(emailParam);
            name_value_pair_list.add(usernameParam);
            name_value_pair_list.add(passwordParam);


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
                if (s.equalsIgnoreCase("User registered")) {
                    Toast.makeText(getApplicationContext(),"Account registered successfully", Toast.LENGTH_SHORT).show();
                    Intent landing = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(landing);
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

    public void goToLogin(View view){
        Intent i2 = new Intent(this, LoginActivity.class);
        startActivity(i2);
    }
}