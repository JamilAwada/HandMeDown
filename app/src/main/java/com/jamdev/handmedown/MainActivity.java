package com.jamdev.handmedown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.jamdev.handmedown.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    User user;
    Intent LoginIntent;

    String userName;
    String userNumber;
    String userAddress;
    String userEmail;
    String userUsername;
    String userPassword;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // These 3 lines hide the title and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        // Default fragment is home
        replaceFragment(new HomeActivity());

        LoginIntent = getIntent();
        user = (User) LoginIntent.getParcelableExtra("User");

        userName = user.getFullName();
        userNumber = user.getPhoneNumber();
        userAddress = user.getAddress();
        userEmail = user.getEmail();
        userUsername = user.getUsername();
        userPassword = user.getPassword();
        userID = user.getId();

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        // Takes item id as param to identify the fragment that has been clicked
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_home:
                    replaceFragment(new HomeActivity());
                    break;
                case R.id.nav_search:
                    replaceFragment(new SearchActivity());
                    break;
                case R.id.nav_listings:


                    Bundle bundle3 = new Bundle();
                    bundle3.putString("Name", userName);
                    bundle3.putString("Phone Number", userNumber);
                    bundle3.putString("Address", userAddress);
                    bundle3.putString("Username", userUsername);
                    bundle3.putString("Email", userEmail);
                    bundle3.putString("Id", userID);
                    bundle3.putString("Password", userPassword);

                    ListingsActivity fragobj3 = new ListingsActivity();
                    fragobj3.setArguments(bundle3);
                    replaceFragment(fragobj3);
                    break;

                case R.id.nav_profile:

                    Bundle bundle4 = new Bundle();
                    bundle4.putString("Name", userName);
                    bundle4.putString("Phone Number", userNumber);
                    bundle4.putString("Address", userAddress);
                    bundle4.putString("Username", userUsername);
                    bundle4.putString("Email", userEmail);
                    bundle4.putString("Id", userID);
                    bundle4.putString("Password", userPassword);

                    ProfileActivity fragobj4 = new ProfileActivity();
                    fragobj4.setArguments(bundle4);
                    replaceFragment(fragobj4);
                    break;
            }
            return true;
        });
    }



    // Function used to replaceFragment, takes target fragment as destination
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }






}