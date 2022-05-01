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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // These 3 lines hide the title and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        // Default fragment is home
        replaceFragment(new HomeActivity());

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
                    LoginIntent = getIntent();
                    user = (User) LoginIntent.getParcelableExtra("User");

                    Bundle bundle3 = new Bundle();
                    bundle3.putString("Name", user.getFullName());
                    bundle3.putString("Phone Number", user.getPhoneNumber());
                    bundle3.putString("Address", user.getAddress());
                    bundle3.putString("Username", user.getUsername());
                    bundle3.putString("Email", user.getEmail());
                    bundle3.putString("Id", user.getId());
                    bundle3.putString("Password", user.getPassword());

                    ListingsActivity fragobj3 = new ListingsActivity();
                    fragobj3.setArguments(bundle3);
                    replaceFragment(fragobj3);
                    break;

                case R.id.nav_profile:
                    LoginIntent = getIntent();
                    user = (User) LoginIntent.getParcelableExtra("User");

                    Bundle bundle4 = new Bundle();
                    bundle4.putString("Name", user.getFullName());
                    bundle4.putString("Phone Number", user.getPhoneNumber());
                    bundle4.putString("Address", user.getAddress());
                    bundle4.putString("Username", user.getUsername());
                    bundle4.putString("Email", user.getEmail());
                    bundle4.putString("Id", user.getId());
                    bundle4.putString("Password", user.getPassword());

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