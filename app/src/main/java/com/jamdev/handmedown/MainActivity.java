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
                    replaceFragment(new ListingsActivity());
                    break;
                case R.id.nav_profile:
                    Intent LoginIntent = getIntent();
                    User user = (User) LoginIntent.getParcelableExtra("User");

                    Bundle bundle = new Bundle();
                    bundle.putString("Name", user.getFullName());
                    bundle.putString("Phone Number", user.getPhoneNumber());
                    bundle.putString("Address", user.getAddress());
                    bundle.putString("Username", user.getUsername());
                    bundle.putString("Email", user.getEmail());

                    ProfileActivity fragobj = new ProfileActivity();
                    fragobj.setArguments(bundle);
                    replaceFragment(fragobj);
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