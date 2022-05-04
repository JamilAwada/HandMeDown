package com.jamdev.handmedown;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    boolean isInModificationMode;
    boolean passIsVisible;

    String id;
    String name = "";
    String number = "";
    String address = "";
    String email = "";
    String username = "";
    String password = "";

    RotateAnimation rotateRight;
    RotateAnimation rotateLeft;


    private String editUserURL = "http://10.0.2.2/HandMeDown/user_edit.php";
    UserInfoUpdateAPI API;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_profile, container, false);

        nameView = (EditText) view.findViewById(R.id.tx_name);
        nameView.setInputType(InputType.TYPE_NULL);
        numberView = (EditText) view.findViewById(R.id.tx_number);
        numberView.setInputType(InputType.TYPE_NULL);
        addressView = (EditText) view.findViewById(R.id.tx_address);
        addressView.setInputType(InputType.TYPE_NULL);
        usernameView = (EditText) view.findViewById(R.id.tx_username_input);
        usernameView.setInputType(InputType.TYPE_NULL);
        emailView = (EditText) view.findViewById(R.id.tx_email_input);
        emailView.setInputType(InputType.TYPE_NULL);
        passwordView = (EditText) view.findViewById(R.id.tx_password_input);
        passwordView.setInputType(InputType.TYPE_NULL);
        saveChangesButton = (RelativeLayout) view.findViewById(R.id.btn_user_save_changes);
        logoutButton = (RelativeLayout) view.findViewById(R.id.btn_logout);
        settingsButton = (ImageView) view.findViewById(R.id.image_settings);

        isInModificationMode = false;
        passIsVisible = false;

        togglePasswordButton = (TextView) view.findViewById(R.id.tx_password);

        name = getArguments().getString("Name");
        nameView.setText(name);
        number = getArguments().getString("Phone Number");
        numberView.setText(number);
        address = getArguments().getString("Address");
        addressView.setText(address);
        username = getArguments().getString("Username");
        usernameView.setText(username);
        email = getArguments().getString("Email");
        emailView.setText(email);
        password = getArguments().getString("Password");
        passwordView.setText(password);
        id = getArguments().getString("Id");

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
            BasicNameValuePair usernameParam = new BasicNameValuePair("Username", username);
            BasicNameValuePair passwordParam = new BasicNameValuePair("Password", password);
            BasicNameValuePair fullNameParam = new BasicNameValuePair("Name", name);
            BasicNameValuePair emailParam = new BasicNameValuePair("Email", email);
            BasicNameValuePair addressParam = new BasicNameValuePair("Address", address);
            BasicNameValuePair phoneNumberParam = new BasicNameValuePair("Number", number);
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
    public void toggleModificationMode(View view) {
        if (!isInModificationMode) {
            saveChangesButton.setClickable(true);
            saveChangesButton.setVisibility(View.VISIBLE);
            nameView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            numberView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            addressView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            usernameView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            emailView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            passwordView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            logoutButton.setVisibility(View.INVISIBLE);
            logoutButton.setClickable(false);
            settingsButton.startAnimation(rotateLeft);
            isInModificationMode = true;

        } else {
            saveChangesButton.setClickable(false);
            saveChangesButton.setVisibility(View.INVISIBLE);
            nameView.setInputType(InputType.TYPE_NULL);
            numberView.setInputType(InputType.TYPE_NULL);
            addressView.setInputType(InputType.TYPE_NULL);
            usernameView.setInputType(InputType.TYPE_NULL);
            emailView.setInputType(InputType.TYPE_NULL);
            passwordView.setInputType(InputType.TYPE_NULL);
            logoutButton.setVisibility(View.VISIBLE);
            logoutButton.setClickable(true);
            settingsButton.startAnimation(rotateRight);
            isInModificationMode = false;

        }


    }

    public void saveChanges(View view) {
        name = nameView.getText().toString();
        number = numberView.getText().toString();
        address = addressView.getText().toString();
        email = emailView.getText().toString();
        username = usernameView.getText().toString();
        password = passwordView.getText().toString();

        saveChangesButton.setClickable(false);
        saveChangesButton.setVisibility(View.INVISIBLE);

        nameView.setInputType(InputType.TYPE_NULL);
        numberView.setInputType(InputType.TYPE_NULL);
        addressView.setInputType(InputType.TYPE_NULL);
        usernameView.setInputType(InputType.TYPE_NULL);
        emailView.setInputType(InputType.TYPE_NULL);
        passwordView.setInputType(InputType.TYPE_NULL);
        logoutButton.setVisibility(View.VISIBLE);
        logoutButton.setClickable(true);
        isInModificationMode = false;

        API = new UserInfoUpdateAPI();
        API.execute();

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





}
