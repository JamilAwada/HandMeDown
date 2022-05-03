package com.jamdev.handmedown;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    EditText tx_name;
    EditText tx_number;
    EditText tx_address;
    EditText tx_username_input;
    EditText tx_email_input;
    EditText tx_password_input;
    RelativeLayout btn_save_changes;
    RelativeLayout btn_logout;
    TextView password_toggle;
    ImageView icon_settings;

    boolean isInModificationMode;
    boolean passIsVisible;
    String userID;

    String fullName = "";
    String phoneNumber = "";
    String address = "";
    String email = "";
    String username = "";
    String password = "";

    String JSONObject;

    private String URL = "http://10.0.2.2/HandMeDown/user_edit.php";
    userInfoUpdateAPI API;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_profile, container, false);

        tx_name = (EditText) view.findViewById(R.id.tx_name);
        tx_name.setInputType(InputType.TYPE_NULL);
        tx_number = (EditText) view.findViewById(R.id.tx_number);
        tx_number.setInputType(InputType.TYPE_NULL);
        tx_address = (EditText) view.findViewById(R.id.tx_address);
        tx_address.setInputType(InputType.TYPE_NULL);
        tx_username_input = (EditText) view.findViewById(R.id.tx_username_input);
        tx_username_input.setInputType(InputType.TYPE_NULL);
        tx_email_input = (EditText) view.findViewById(R.id.tx_email_input);
        tx_email_input.setInputType(InputType.TYPE_NULL);
        tx_password_input = (EditText) view.findViewById(R.id.tx_password_input);
        tx_password_input.setInputType(InputType.TYPE_NULL);
        btn_save_changes = (RelativeLayout) view.findViewById(R.id.btn_user_save_changes);
        btn_logout = (RelativeLayout) view.findViewById(R.id.btn_logout);
        icon_settings = (ImageView) view.findViewById(R.id.image_settings);

        isInModificationMode = false;
        passIsVisible = false;

        password_toggle = (TextView) view.findViewById(R.id.tx_password);

        fullName = getArguments().getString("Name");
        tx_name.setText(fullName);
        phoneNumber = getArguments().getString("Phone Number");
        tx_number.setText(phoneNumber);
        address = getArguments().getString("Address");
        tx_address.setText(address);
        username = getArguments().getString("Username");
        tx_username_input.setText(username);
        email = getArguments().getString("Email");
        tx_email_input.setText(email);
        password = getArguments().getString("Password");
        tx_password_input.setText(password);
        Log.i("password" , tx_password_input.getText().toString());
        userID = getArguments().getString("Id");

        icon_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleModificationMode(view);
            }


        });

        btn_save_changes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                saveChanges(view);
            }
        });

        password_toggle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                togglePass(view);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view);
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


    }

    class userInfoUpdateAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpClient http_client = new DefaultHttpClient();
            HttpPost http_post = new HttpPost(URL);

            BasicNameValuePair idParam = new BasicNameValuePair("Id", userID);
            BasicNameValuePair usernameParam = new BasicNameValuePair("Username", username);
            BasicNameValuePair passwordParam = new BasicNameValuePair("Password", password);
            BasicNameValuePair fullNameParam = new BasicNameValuePair("FullName", fullName);
            BasicNameValuePair emailParam = new BasicNameValuePair("Email", email);
            BasicNameValuePair addressParam = new BasicNameValuePair("Address", address);
            BasicNameValuePair phoneNumberParam = new BasicNameValuePair("PhoneNumber", phoneNumber);
            ArrayList<NameValuePair> name_value_pair_list = new ArrayList<>();
            name_value_pair_list.add(idParam);
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
                if (s.equalsIgnoreCase("User info updated.")) {
                    Toast.makeText(getContext(),  s, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    Log.i("json", JSONObject);
                }
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    public void toggleModificationMode(View view) {
        if (!isInModificationMode) {
            btn_save_changes.setClickable(true);
            btn_save_changes.setVisibility(View.VISIBLE);
            tx_name.setInputType(InputType.TYPE_CLASS_TEXT);
            tx_number.setInputType(InputType.TYPE_CLASS_TEXT);
            tx_address.setInputType(InputType.TYPE_CLASS_TEXT);
            tx_username_input.setInputType(InputType.TYPE_CLASS_TEXT);
            tx_email_input.setInputType(InputType.TYPE_CLASS_TEXT);
            tx_password_input.setInputType(InputType.TYPE_CLASS_TEXT);
            btn_logout.setVisibility(View.INVISIBLE);
            btn_logout.setClickable(false);
            isInModificationMode = true;
        } else {
            btn_save_changes.setClickable(false);
            btn_save_changes.setVisibility(View.INVISIBLE);
            tx_name.setInputType(InputType.TYPE_NULL);
            tx_number.setInputType(InputType.TYPE_NULL);
            tx_address.setInputType(InputType.TYPE_NULL);
            tx_username_input.setInputType(InputType.TYPE_NULL);
            tx_email_input.setInputType(InputType.TYPE_NULL);
            tx_password_input.setInputType(InputType.TYPE_NULL);
            btn_logout.setVisibility(View.VISIBLE);
            btn_logout.setClickable(true);
            isInModificationMode = false;
        }


    }

    public void saveChanges(View view) {
        fullName = tx_name.getText().toString();
        phoneNumber = tx_number.getText().toString();
        address = tx_address.getText().toString();
        email = tx_email_input.getText().toString();
        username = tx_username_input.getText().toString();
        password = tx_password_input.getText().toString();

        btn_save_changes.setClickable(false);
        btn_save_changes.setVisibility(View.INVISIBLE);

        tx_name.setInputType(InputType.TYPE_NULL);
        tx_number.setInputType(InputType.TYPE_NULL);
        tx_address.setInputType(InputType.TYPE_NULL);
        tx_username_input.setInputType(InputType.TYPE_NULL);
        tx_email_input.setInputType(InputType.TYPE_NULL);
        tx_password_input.setInputType(InputType.TYPE_NULL);
        btn_logout.setVisibility(View.VISIBLE);
        btn_logout.setClickable(true);
        isInModificationMode = false;

        API = new userInfoUpdateAPI();
        API.execute();

    }

    public void togglePass(View view){
        if (!passIsVisible){
            tx_password_input.setVisibility(View.VISIBLE);
            passIsVisible = true;
        }
        else {
            tx_password_input.setVisibility(View.INVISIBLE);
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
