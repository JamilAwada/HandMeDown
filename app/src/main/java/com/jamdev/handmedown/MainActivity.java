package com.jamdev.handmedown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.jamdev.handmedown.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private User user;
    private String userName;
    private String userNumber;
    private String userAddress;
    private String userEmail;
    private String userUsername;
    private String userPassword;
    private String userID;

    ActivityMainBinding binding;

    Intent LoginIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // These 3 lines hide the title and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        // Default fragment is home
        replaceFragment(new HomeActivity());

        // Get user info from login activity
        LoginIntent = getIntent();
        user = (User) LoginIntent.getParcelableExtra("User");

        // Set user data to be bundled and sent to fragments
        userName = user.getName();
        userNumber = user.getNumber();
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
                    Bundle listingsBundle = new Bundle();
                    listingsBundle.putString("Name", userName);
                    listingsBundle.putString("Phone Number", userNumber);
                    listingsBundle.putString("Address", userAddress);
                    listingsBundle.putString("Username", userUsername);
                    listingsBundle.putString("Email", userEmail);
                    listingsBundle.putString("ID", userID);
                    listingsBundle.putString("Password", userPassword);

                    ListingsActivity listingsFragment = new ListingsActivity();
                    listingsFragment.setArguments(listingsBundle);
                    replaceFragment(listingsFragment);
                    break;

                case R.id.nav_profile:
                    Bundle profileBundle = new Bundle();
                    profileBundle.putString("Name", userName);
                    profileBundle.putString("Phone Number", userNumber);
                    profileBundle.putString("Address", userAddress);
                    profileBundle.putString("Username", userUsername);
                    profileBundle.putString("Email", userEmail);
                    profileBundle.putString("Id", userID);
                    profileBundle.putString("Password", userPassword);

                    ProfileActivity profileFragment = new ProfileActivity();
                    profileFragment.setArguments(profileBundle);
                    replaceFragment(profileFragment);
                    break;
            }
            return true;
        });
    }

    // Function used to replaceFragment, takes destination fragment as parameter
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, fragment);
        fragmentTransaction.commit();
    }





}