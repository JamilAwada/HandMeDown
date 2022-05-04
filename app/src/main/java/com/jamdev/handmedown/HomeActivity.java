package com.jamdev.handmedown;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends Fragment {

    RelativeLayout toys;
    RelativeLayout clothing;
    RelativeLayout electronics;
    RelativeLayout gear;
    RelativeLayout disposables;
    RelativeLayout consumables;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        toys = (RelativeLayout) view.findViewById(R.id.card_toys);
        clothing = (RelativeLayout) view.findViewById(R.id.card_clothing);
        electronics = (RelativeLayout) view.findViewById(R.id.card_electronics);
        gear = (RelativeLayout) view.findViewById(R.id.card_gear);
        disposables = (RelativeLayout) view.findViewById(R.id.card_disposables);
        consumables = (RelativeLayout) view.findViewById(R.id.card_consumables);

        toys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToToys(view);
            }
        });

        clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToClothing(view);
            }
        });

        electronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToElectronics(view);
            }
        });

        gear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGear(view);
            }
        });

        disposables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDisposables(view);
            }
        });

        consumables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToConsumables(view);
            }
        });


        return view;
    }


    public void goToToys(View view){
        Intent iToys = new Intent(getContext(), CategoryActivity.class);
        iToys.putExtra("Category", "Toys");
        startActivity(iToys);
    }

    public void goToClothing(View view){
        Intent iClothing = new Intent(getContext(), CategoryActivity.class);
        iClothing.putExtra("Category", "Clothing");
        startActivity(iClothing);
    }

    public void goToElectronics(View view){
        Intent iElectronics = new Intent(getContext(), CategoryActivity.class);
        iElectronics.putExtra("Category", "Electronics");
        startActivity(iElectronics);
    }

    public void goToGear(View view){
        Intent iGear = new Intent(getContext(), CategoryActivity.class);
        iGear.putExtra("Category", "Gear");
        startActivity(iGear);
    }

    public void goToDisposables(View view){
        Intent iDisposables = new Intent(getContext(), CategoryActivity.class);
        iDisposables.putExtra("Category", "Disposables");
        startActivity(iDisposables);
    }

    public void goToConsumables(View view){
        Intent iConsumables = new Intent(getContext(), CategoryActivity.class);
        iConsumables.putExtra("Category", "Consumables");
        startActivity(iConsumables);
    }

}
