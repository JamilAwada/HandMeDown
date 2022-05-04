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

    EditText nameView;
    EditText numberView;
    EditText addressView;
    EditText usernameView;
    EditText emailView;
    EditText passwordView;

    String name = "";
    String number = "";
    String address = "";
    String username = "";
    String email = "";
    String password = "";


    private String userSignUpURL = "http://10.0.2.2/HandMeDown/user_signup.php";
    userRegistrationAPI API;

    @Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    // These 3 lines hide the title and action bar
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    getSupportActionBar().hide();
    setContentView(R.layout.activity_signup);

    nameView = (EditText) findViewById(R.id.et_name);
    numberView =(EditText) findViewById(R.id.et_number);
    addressView = (EditText) findViewById(R.id.et_address);
    emailView = (EditText) findViewById(R.id.et_email);
    usernameView = (EditText) findViewById(R.id.et_username);
    passwordView = (EditText) findViewById(R.id.et_password);
    }

    public void registerUser(View view) {

        name = nameView.getText().toString();
        number = numberView.getText().toString();
        address = addressView.getText().toString();
        email = emailView.getText().toString();
        username = usernameView.getText().toString();
        password = passwordView.getText().toString();


        if (password.equalsIgnoreCase("") || username.equalsIgnoreCase("")
                || name.equalsIgnoreCase("") || email.equalsIgnoreCase("") || name.equalsIgnoreCase("") || address.equalsIgnoreCase("")) {
            Toast.makeText(this, "Incomplete form. Please fill in the blank fields.", Toast.LENGTH_SHORT).show();
        } else {
            API = new userRegistrationAPI();
            API.execute();
        }


    }

    class userRegistrationAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // URL and HTTP initialization to connect to API
            HttpClient http_client = new DefaultHttpClient();
            HttpPost http_post = new HttpPost(userSignUpURL);

            BasicNameValuePair usernameParam = new BasicNameValuePair("Username", username);
            BasicNameValuePair passwordParam = new BasicNameValuePair("Password", password);
            BasicNameValuePair nameParam = new BasicNameValuePair("Name", name);
            BasicNameValuePair emailParam = new BasicNameValuePair("Email", email);
            BasicNameValuePair addressParam = new BasicNameValuePair("Address", address);
            BasicNameValuePair numberParam = new BasicNameValuePair("Number", number);
            ArrayList<NameValuePair> name_value_pair_list = new ArrayList<>();
            name_value_pair_list.add(nameParam);
            name_value_pair_list.add(numberParam);
            name_value_pair_list.add(addressParam);
            name_value_pair_list.add(emailParam);
            name_value_pair_list.add(usernameParam);
            name_value_pair_list.add(passwordParam);


            try {
                // Send the list of name value pairs using an encoded form entity
                UrlEncodedFormEntity url_encoded_form_entity = new UrlEncodedFormEntity(name_value_pair_list);

                // Set the post object to contain the entity
                http_post.setEntity(url_encoded_form_entity);

                // Reads API response and makes it a string
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
                if (s.equalsIgnoreCase("User registered")) {
                    Toast.makeText(getApplicationContext(),"Account registered successfully", Toast.LENGTH_SHORT).show();
                    Intent landing = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(landing);
                } else {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void goToLogin(View view){
        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivity(goToLogin);
        finish();
    }
}