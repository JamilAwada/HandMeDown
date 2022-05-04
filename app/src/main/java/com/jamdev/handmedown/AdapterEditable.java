package com.jamdev.handmedown;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdapterEditable extends RecyclerView.Adapter<AdapterEditable.ViewHolder> {

    private List<Listing> listings = new ArrayList<>();
    private Context listingsActivity;

    // Pass in context
    public AdapterEditable(Context listingsActivity, List<Listing> listings){
        this.listingsActivity = listingsActivity;
        this.listings = listings;
    }

    @NonNull
    @Override
    public AdapterEditable.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_design_editable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEditable.ViewHolder holder, int position) {

        String listingID = listings.get(position).getId();
        String listingTitle = listings.get(position).getTitle();
        String listingDescription = listings.get(position).getDescription();
        String listingPrice = listings.get(position).getPrice();
        String listingDate = listings.get(position).getPosted_on();
        Log.i("Listing date:", listingDate);
        int listingPicture = listings.get(position).getPicture();

        holder.setData(listingID,listingTitle,listingDescription,listingPrice,listingDate,listingPicture);

    }
    @Override
    public int getItemCount() {
        return listings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{



        private String listingID;
        private TextView listingTitle;
        private TextView listingDescription;
        private TextView listingPrice;
        private TextView listingDate;
        private ImageView listingPicture;

        private ImageView editButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listingTitle = itemView.findViewById(R.id.listing_title);
            listingDescription = itemView.findViewById(R.id.card_description);
            listingPrice = itemView.findViewById(R.id.card_price_text);
            listingDate = itemView.findViewById(R.id.card_date_text);
            listingPicture = itemView.findViewById(R.id.card_picture_container);

            editButton = itemView.findViewById(R.id.card_btn_edit);


            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToEditListing(view);
                }
            });


        }


        public void goToEditListing(View view){
            Intent goToEdit = new Intent(listingsActivity, EditListingActivity.class);
            goToEdit.putExtra("Item Title", listingTitle.getText().toString());
            goToEdit.putExtra("Item Description", listingDescription.getText().toString());
            goToEdit.putExtra("Item Price", listingPrice.getText().toString());
            goToEdit.putExtra("Item Category", listingPrice.getText().toString());
            goToEdit.putExtra("Item ID", listingID);
            listingsActivity.startActivity(goToEdit);
        }


        public void setData(String id, String title, String description, String price, String date, int picture) {
            listingID = id;
            listingTitle.setText(title);
            listingDescription.setText(description);
            listingPrice.setText(price);
            listingDate.setText(date);
            listingPicture.setImageResource(picture);
        }


    }
}
