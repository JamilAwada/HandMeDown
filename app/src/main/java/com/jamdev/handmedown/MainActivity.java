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
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ModelClass> itemList = new ArrayList<>();
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // These 3 lines hide the title and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_listings);

        // Initialize recycler view
        initData();
        initRecyclerView();


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
                    replaceFragment(new ProfileActivity());
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

    // Intent to go from log in page to sign up page
    public void goToSignUp(View view){
        Intent goToSignUp = new Intent(this, SignUpActivity.class);
        startActivity(goToSignUp);
    }

    // Intent to go from log in page to sign up page
    public void goToLogin(View view){
        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivity(goToLogin);
    }

    // Add and remove data
    private void initData() {
        itemList.add(new ModelClass(R.drawable.placeholder2, "Bottle","Gently used bottle with replaceable nib", "$10", "Abdallah", "24/05/2002",R.drawable.seperator_line));
        itemList.add(new ModelClass(R.drawable.placeholder2, "Bottle","Gently used bottle with replaceable nib", "$10", "Abdallah", "24/05/2002", R.drawable.seperator_line));
        itemList.add(new ModelClass(R.drawable.placeholder2, "Bottle","Gently used bottle with replaceable nib", "$10", "Abdallah", "24/05/2002", R.drawable.seperator_line));
    }

    // Initialize recyclerView function
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.listings_container);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(itemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}