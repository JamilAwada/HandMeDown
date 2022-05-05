package com.jamdev.handmedown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
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

    private String username;
    private String password;
    int picture;
    private EditText usernameInput;
    private EditText passwordInput;
    private ImageView logo;

    private String userLoginURL = "http://10.0.2.2/HandMeDown/user_login.php";
    UserAuthenticationAPI API;

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
        logo = (ImageView) findViewById(R.id.image_logo);

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        logo.setAnimation(shake);

    }

    public void login(View view) {
        API = new UserAuthenticationAPI();
        API.execute();
    }


    public void goToSignUp(View view) {
        Intent goToSignUp = new Intent(this, SignUpActivity.class);
        startActivity(goToSignUp);
        finish();
    }


    class UserAuthenticationAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // URL and HTTP initialization to connect to API
            HttpClient http_client = new DefaultHttpClient();
            HttpPost http_post = new HttpPost(userLoginURL);

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
                JSONObject json = new JSONObject(values);

                // Returned status from the post API
                String status = json.getString("status");

                if (status.equalsIgnoreCase("Welcome")) {
                    String id = json.getString("id");
                    String name = json.getString("name");
                    String number = json.getString("number");
                    String address = json.getString("address");
                    String username = json.getString("username");
                    String email = json.getString("email");
                    String password = json.getString("password");
                    String fetchedPicture = json.getString("picture");
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

                    User user = new User(id, name, number, address, username, email, password, picture);

                    Toast.makeText(LoginActivity.this, status + " " + name + "!", Toast.LENGTH_SHORT).show();

                    Intent goToLanding = new Intent(LoginActivity.this, MainActivity.class);
                    goToLanding.putExtra("User", user);
                    startActivity(goToLanding);
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