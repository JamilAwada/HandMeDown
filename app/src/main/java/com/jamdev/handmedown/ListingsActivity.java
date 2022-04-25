package com.jamdev.handmedown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListingsActivity extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ModelClass> itemList = new ArrayList<>();
    Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialize recycler view
        initData();
        return inflater.inflate(R.layout.activity_listings, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        initRecyclerView();
    }

    // Add and remove data
    private void initData() {
        itemList.add(new ModelClass(R.drawable.placeholder2, "Bottle","Gently used bottle with replaceable nib", "$10", "Abdallah", "24/05/2002",R.drawable.seperator_line));
        itemList.add(new ModelClass(R.drawable.placeholder2, "Bottle","Gently used bottle with replaceable nib", "$10", "Abdallah", "24/05/2002", R.drawable.seperator_line));
        itemList.add(new ModelClass(R.drawable.placeholder2, "Bottle","Gently used bottle with replaceable nib", "$10", "Abdallah", "24/05/2002", R.drawable.seperator_line));
    }

    // Initialize recyclerView function
    private void initRecyclerView() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.listings_container);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(itemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}
