package com.jamdev.handmedown;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.mikhaellopez.circularimageview.CircularImageView;

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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ProfileActivity extends Fragment {

    EditText nameView;
    EditText numberView;
    EditText addressView;
    EditText usernameView;
    EditText emailView;
    CircularImageView pictureView;

    RelativeLayout saveChangesButton;
    RelativeLayout logoutButton;

    TextView togglePasswordButton;
    ImageView settingsButton;
    Dialog dialog;

    RelativeLayout cancelPassButton;
    RelativeLayout savePassButton;

    String newPass;

    boolean isInModificationMode = false;

    User user;

    int picture;

    String id;
    String userName = "";
    String userNumber = "";
    String userAddress = "";
    String userEmail = "";
    String userUsername = "";
    String userPassword = "";
    String userPicture = "";
    String oldName = "";

    Spinner profileDemo;
    ArrayAdapter<CharSequence> adapter;

    EditText oldPasswordInput;
    EditText newPasswordInput;

    RotateAnimation rotateRight;
    RotateAnimation rotateLeft;


    private String editUserURL = "http://10.0.2.2/HandMeDown/user_edit.php";
    UserInfoUpdateAPI userInfoUpdateAPI;

    private String getUserURL = "http://10.0.2.2/HandMeDown/user_fetch_id.php?id=";
    private GetUserAPI getUserAPI;

    private String changePassURL = "http://10.0.2.2/HandMeDown/user_change_pass.php";
    private ChangePassAPI changePassAPI;


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
        pictureView = (CircularImageView) view.findViewById(R.id.container_profile_picture);
        profileDemo = (Spinner) view.findViewById(R.id.profile_DEMO);
        profileDemo.setEnabled(false);
        adapter = ArrayAdapter.createFromResource(getContext(), R.array.pictures, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileDemo.setAdapter(adapter);
        saveChangesButton = (RelativeLayout) view.findViewById(R.id.btn_user_save_changes);
        logoutButton = (RelativeLayout) view.findViewById(R.id.btn_logout);
        settingsButton = (ImageView) view.findViewById(R.id.image_settings);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialogue_bg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.dialogue_shape));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        cancelPassButton = dialog.findViewById(R.id.btn_cancel);
        savePassButton = dialog.findViewById(R.id.btn_save_new_password);

        oldPasswordInput = (EditText) dialog.findViewById(R.id.et_old_input);
        newPasswordInput = (EditText) dialog.findViewById(R.id.et_new_input);

        togglePasswordButton = (TextView) view.findViewById(R.id.tx_password);

        id = getArguments().getString("ID");

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
                dialog.show();
            }
        });

        cancelPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        savePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPass = newPasswordInput.getText().toString();
                if (newPass.equalsIgnoreCase("") || oldPasswordInput.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getContext(),    "Missing fields.", Toast.LENGTH_SHORT).show();
                }
                else {
                    changePassAPI = new ChangePassAPI();
                    changePassAPI.execute();
                }


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
            BasicNameValuePair fullNameParam = new BasicNameValuePair("Name", userName);
            BasicNameValuePair emailParam = new BasicNameValuePair("Email", userEmail);
            BasicNameValuePair addressParam = new BasicNameValuePair("Address", userAddress);
            BasicNameValuePair phoneNumberParam = new BasicNameValuePair("Number", userNumber);
            BasicNameValuePair pictureParam = new BasicNameValuePair("Picture", userPicture);
            ArrayList<NameValuePair> name_value_pair_list = new ArrayList<>();
            name_value_pair_list.add(idParam);
            name_value_pair_list.add(fullNameParam);
            name_value_pair_list.add(phoneNumberParam);
            name_value_pair_list.add(addressParam);
            name_value_pair_list.add(emailParam);
            name_value_pair_list.add(usernameParam);
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
                    String fetchedPicture = jsonItemObject.getString("picture");
                    if (fetchedPicture.equalsIgnoreCase("DEMO: Man 1")){
                        picture = R.drawable.demo_man1;
                    }
                    else if (fetchedPicture.equalsIgnoreCase("DEMO: Man 2")){
                        picture = R.drawable.demo_man2;
                    }
                    else if (fetchedPicture.equalsIgnoreCase("DEMO: Woman 1")){
                        picture = R.drawable.demo_woman1;
                    }
                    else if (fetchedPicture.equalsIgnoreCase("DEMO: Woman 2")){
                        picture = R.drawable.demo_woman2;
                    }
                    else if (fetchedPicture.equalsIgnoreCase("DEMO: Bear")){
                        picture = R.drawable.demo_bear;
                    }
                    else if (fetchedPicture.equalsIgnoreCase("DEMO: Onesie")){
                        picture = R.drawable.demo_onesie;
                    }
                    else if (fetchedPicture.equalsIgnoreCase("DEMO: Monitor")){
                        picture = R.drawable.demo_monitor;
                    }
                    else if (fetchedPicture.equalsIgnoreCase("DEMO: Stroller")){
                        picture = R.drawable.demo_stroller;
                    }
                    else if (fetchedPicture.equalsIgnoreCase("DEMO: Diapers")){
                        picture = R.drawable.demo_diapers;
                    }
                    else if (fetchedPicture.equalsIgnoreCase("DEMO: Formula")){
                        picture = R.drawable.demo_formula;
                    }
                    else {
                        picture = R.drawable.no_picture;
                    }


                    // Get the listing and the seller, and send to the expanded listing view class
                    user = new User(id,name,number,address,username,email, password, picture);

                    userName = name;
                    oldName = name;
                    userNumber = number;
                    userAddress = address;
                    userUsername = username;
                    userEmail = email;
                    userPassword = password;
                    userPicture = fetchedPicture;

                    numberView.setText(userNumber);
                    addressView.setText(userAddress);
                    usernameView.setText(userUsername);
                    emailView.setText(userEmail);
                    profileDemo.setSelection(adapter.getPosition(fetchedPicture));
                    pictureView.setImageResource(picture);


                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    class ChangePassAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpClient http_client = new DefaultHttpClient();
            HttpPost http_post = new HttpPost(changePassURL);

            BasicNameValuePair idParam = new BasicNameValuePair("ID", id);
            BasicNameValuePair oldPasswordParam = new BasicNameValuePair("OldPassword", oldPasswordInput.getText().toString());
            BasicNameValuePair newPasswordParam = new BasicNameValuePair("NewPassword", newPass);
            ArrayList<NameValuePair> name_value_pair_list = new ArrayList<>();
            name_value_pair_list.add(idParam);
            name_value_pair_list.add(oldPasswordParam);
            name_value_pair_list.add(newPasswordParam);


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

    }


    public void toggleModificationMode(View view) {
        if (!isInModificationMode) {
            saveChangesButton.setVisibility(View.VISIBLE);
            saveChangesButton.setClickable(true);
            settingsButton.startAnimation(rotateLeft);
            logoutButton.setVisibility(View.INVISIBLE);
            logoutButton.setClickable(false);
            numberView.setEnabled(true);
            addressView.setEnabled(true);
            usernameView.setEnabled(true);
            emailView.setEnabled(true);
            profileDemo.setEnabled(true);
            isInModificationMode = true;

        } else {
            saveChangesButton.setVisibility(View.INVISIBLE);
            saveChangesButton.setClickable(false);
            settingsButton.startAnimation(rotateRight);
            logoutButton.setVisibility(View.VISIBLE);
            logoutButton.setClickable(true);
            numberView.setEnabled(false);
            addressView.setEnabled(false);
            usernameView.setEnabled(false);
            emailView.setEnabled(false);
            profileDemo.setEnabled(false);
            isInModificationMode = false;

        }


    }

    public void saveChanges(View view) {
        if (isInModificationMode){
            userName = nameView.getText().toString();
            userNumber = numberView.getText().toString();
            userAddress = addressView.getText().toString();
            userEmail = emailView.getText().toString();
            userUsername = usernameView.getText().toString();
            userPicture = profileDemo.getSelectedItem().toString();

            userInfoUpdateAPI = new UserInfoUpdateAPI();
            userInfoUpdateAPI.execute();

            getUserAPI = new GetUserAPI();
            getUserAPI.execute(getUserURL + id);

            toggleModificationMode(view);
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
