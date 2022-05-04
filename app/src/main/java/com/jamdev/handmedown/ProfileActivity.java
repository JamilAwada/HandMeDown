package com.jamdev.handmedown;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ProfileActivity extends Fragment {

    EditText nameView;
    EditText numberView;
    EditText addressView;
    EditText usernameView;
    EditText emailView;
    EditText passwordView;

    RelativeLayout saveChangesButton;
    RelativeLayout logoutButton;

    TextView togglePasswordButton;
    ImageView settingsButton;

    boolean isInModificationMode = false;
    boolean passIsVisible = false;

    User user;

    String id;
    String userName = "";
    String userNumber = "";
    String userAddress = "";
    String userEmail = "";
    String userUsername = "";
    String userPassword = "";

    RotateAnimation rotateRight;
    RotateAnimation rotateLeft;


    private String editUserURL = "http://10.0.2.2/HandMeDown/user_edit.php";
    UserInfoUpdateAPI userInfoUpdateAPI;

    private String getUserURL = "http://10.0.2.2/HandMeDown/user_fetch_id.php?id=";
    private GetUserAPI getUserAPI;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_profile, container, false);

        nameView = (EditText) view.findViewById(R.id.tx_name);
        nameView.setEnabled(false);
        numberView = (EditText) view.findViewById(R.id.tx_number);
        numberView.setEnabled(false);
        addressView = (EditText) view.findViewById(R.id.tx_address);
        addressView.setEnabled(false);
        usernameView = (EditText) view.findViewById(R.id.tx_username_input);
        usernameView.setEnabled(false);
        emailView = (EditText) view.findViewById(R.id.tx_email_input);
        emailView.setEnabled(false);
        passwordView = (EditText) view.findViewById(R.id.tx_password_input);
        passwordView.setEnabled(false);
        saveChangesButton = (RelativeLayout) view.findViewById(R.id.btn_user_save_changes);
        logoutButton = (RelativeLayout) view.findViewById(R.id.btn_logout);
        settingsButton = (ImageView) view.findViewById(R.id.image_settings);

        togglePasswordButton = (TextView) view.findViewById(R.id.tx_password);

        id = getArguments().getString("Id");

        getUserAPI = new GetUserAPI();
        getUserAPI.execute(getUserURL + id);


        rotateRight = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateRight.setDuration(1000);
        rotateRight.setInterpolator(new LinearInterpolator());

        rotateLeft = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateLeft.setDuration(1000);
        rotateLeft.setInterpolator(new LinearInterpolator());


        // On Click Listeners for the respective buttons

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleModificationMode(view);
                getUserAPI = new GetUserAPI();
                getUserAPI.execute(getUserURL + id);
            }


        });

        saveChangesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                saveChanges(view);
            }
        });

        togglePasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                togglePass(view);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view);
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


    }

    class UserInfoUpdateAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpClient http_client = new DefaultHttpClient();
            HttpPost http_post = new HttpPost(editUserURL);

            BasicNameValuePair idParam = new BasicNameValuePair("ID", id);
            BasicNameValuePair usernameParam = new BasicNameValuePair("Username", userUsername);
            BasicNameValuePair passwordParam = new BasicNameValuePair("Password", userPassword);
            BasicNameValuePair fullNameParam = new BasicNameValuePair("Name", userName);
            BasicNameValuePair emailParam = new BasicNameValuePair("Email", userEmail);
            BasicNameValuePair addressParam = new BasicNameValuePair("Address", userAddress);
            BasicNameValuePair phoneNumberParam = new BasicNameValuePair("Number", userNumber);
            ArrayList<NameValuePair> name_value_pair_list = new ArrayList<>();
            name_value_pair_list.add(idParam);
            name_value_pair_list.add(fullNameParam);
            name_value_pair_list.add(phoneNumberParam);
            name_value_pair_list.add(addressParam);
            name_value_pair_list.add(emailParam);
            name_value_pair_list.add(usernameParam);
            name_value_pair_list.add(passwordParam);


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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s.equalsIgnoreCase("User info updated.")) {
                    Toast.makeText(getContext(),  s, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




    }

    public class GetUserAPI extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            // URL and HTTP initialization to connect to API 2
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

                for(int i = 0; i < listJsonArray.length(); i++){

                    JSONObject jsonItemObject = listJsonArray.getJSONObject(i);
                    String id = jsonItemObject.getString("id");
                    String username = jsonItemObject.getString("username");
                    String password = jsonItemObject.getString("password");
                    String name = jsonItemObject.getString("name");
                    String email = jsonItemObject.getString("email");
                    String number = jsonItemObject.getString("number");
                    String address = jsonItemObject.get("address").toString();
                    int picture = R.drawable.no_picture;

                    // Get the listing and the seller, and send to the expanded listing view class
                    user = new User(id,name,number,address,username,email,picture);

                    userName = name;
                    userNumber = number;
                    userAddress = address;
                    userUsername = username;
                    userEmail = email;

                    nameView.setText(userName);
                    numberView.setText(userNumber);
                    addressView.setText(userAddress);
                    usernameView.setText(userUsername);
                    emailView.setText(userEmail);
                    passwordView.setText(userPassword);


                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
    public void toggleModificationMode(View view) {
        if (!isInModificationMode) {
            saveChangesButton.setClickable(true);
            saveChangesButton.setVisibility(View.VISIBLE);
            nameView.setEnabled(true);
            numberView.setEnabled(true);
            addressView.setEnabled(true);
            usernameView.setEnabled(true);
            emailView.setEnabled(true);
            passwordView.setEnabled(true);
            logoutButton.setVisibility(View.INVISIBLE);
            logoutButton.setClickable(false);
            settingsButton.startAnimation(rotateLeft);
            isInModificationMode = true;

        } else {
            saveChangesButton.setClickable(false);
            saveChangesButton.setVisibility(View.INVISIBLE);
            nameView.setEnabled(false);
            numberView.setEnabled(false);
            addressView.setEnabled(false);
            usernameView.setEnabled(false);
            emailView.setEnabled(false);
            passwordView.setEnabled(false);
            logoutButton.setVisibility(View.VISIBLE);
            logoutButton.setClickable(true);
            settingsButton.startAnimation(rotateRight);
            isInModificationMode = false;

        }


    }

    public void saveChanges(View view) {
        userName = nameView.getText().toString();
        userNumber = numberView.getText().toString();
        userAddress = addressView.getText().toString();
        userEmail = emailView.getText().toString();
        userUsername = usernameView.getText().toString();
        userPassword = passwordView.getText().toString();

        saveChangesButton.setEnabled(false);
        saveChangesButton.setVisibility(View.INVISIBLE);

        nameView.setInputType(InputType.TYPE_NULL);
        numberView.setInputType(InputType.TYPE_NULL);
        addressView.setEnabled(false);
        usernameView.setInputType(InputType.TYPE_NULL);
        emailView.setInputType(InputType.TYPE_NULL);
        passwordView.setInputType(InputType.TYPE_NULL);
        logoutButton.setVisibility(View.VISIBLE);
        logoutButton.setClickable(true);
        isInModificationMode = false;

        userInfoUpdateAPI = new UserInfoUpdateAPI();
        userInfoUpdateAPI.execute();

        getUserAPI = new GetUserAPI();
        getUserAPI.execute(getUserURL + id);

    }

    public void togglePass(View view){
        if (!passIsVisible){
            passwordView.animate().alpha(1f).setDuration(1000);
            passwordView.setVisibility(View.VISIBLE);
            passIsVisible = true;
        }
        else {
            passwordView.animate().alpha(0f).setDuration(1000);
            passwordView.setInputType(InputType.TYPE_NULL);
            passIsVisible = false;
        }

    }

    public void logout(View view){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        finishActivity();
    }

    private void finishActivity() {
        if(getActivity() != null) {
            getActivity().finish();
        }
    }

    public void refreshUI(View view){
        GetUserAPI getUserAPI = new GetUserAPI();
        getUserAPI.execute(getUserURL + id);

    }





}
