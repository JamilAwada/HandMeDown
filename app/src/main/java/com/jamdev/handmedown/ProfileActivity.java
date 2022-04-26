package com.jamdev.handmedown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileActivity extends Fragment {

    TextView tx_name;
    TextView tx_number;
    TextView tx_address;
    TextView tx_username;
    TextView tx_email;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_profile, container, false);

        tx_name = (TextView) view.findViewById(R.id.tx_name);
        tx_number = (TextView) view.findViewById(R.id.tx_number);
        tx_address = (TextView) view.findViewById(R.id.tx_address);
        tx_username = (TextView) view.findViewById(R.id.tx_username_input);
        tx_email = (TextView) view.findViewById(R.id.tx_email_input);

        String fullName = getArguments().getString("Name");
        tx_name.setText(fullName);
        String phoneNumber = getArguments().getString("Phone Number");
        tx_number.setText(phoneNumber);
        String address = getArguments().getString("Address");
        tx_address.setText(address);
        String userName = getArguments().getString("Username");
        tx_username.setText(userName);
        String email = getArguments().getString("Email");
        tx_email.setText(email);

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){




    }



}
