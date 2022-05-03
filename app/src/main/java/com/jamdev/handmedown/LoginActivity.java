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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private String URL = "http://10.0.2.2/HandMeDown/user_login.php";
    public String username;
    public String password;
    public EditText usernameInput;
    public EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // These 3 lines hide the title and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        usernameInput = (EditText) findViewById(R.id.et_username);
        passwordInput = (EditText) findViewById(R.id.et_password);

    }

    public void login(View view) {
        userAuthenticationAPI user_authentication_api = new userAuthenticationAPI();
        user_authentication_api.execute();
    }



    public void goToSignup(View view) {
        Intent i1 = new Intent(this, SignUpActivity.class);
        startActivity(i1);
    }


    class userAuthenticationAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpClient http_client = new DefaultHttpClient();
            HttpPost http_post = new HttpPost(URL);

            username = usernameInput.getText().toString();
            password = passwordInput.getText().toString();

            BasicNameValuePair usernameParam = new BasicNameValuePair("Username", username);
            BasicNameValuePair passwordParam = new BasicNameValuePair("Password", password);

            ArrayList<NameValuePair> name_value_pair_list = new ArrayList<>();
            name_value_pair_list.add(usernameParam);
            name_value_pair_list.add(passwordParam);

            try {
                // Send the list of name value pairs using an encoded form entity
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(name_value_pair_list);

                // Set the post object to contain the entity
                http_post.setEntity(entity);

                // Reads API response and string
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
                JSONObject json = new JSONObject(s);
                // Returned status from the post API
                String status = json.getString("status");

                if (status.equalsIgnoreCase("accepted")) {
                    String id = json.getString("Id");
                    String fullName = json.getString("Name");
                    String phoneNumber = json.getString("PhoneNumber");
                    String address = json.getString("Address");
                    String username = json.getString("Username");
                    String email = json.getString("Email");
                    String password = json.getString("Password");
                    int profilePicture = R.drawable.no_picture;

                    User user = new User(fullName, phoneNumber, address, username, email, password, profilePicture, id);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                    finish();
                } else {
                    // Notify user of wrong password/invalid username
                    Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}