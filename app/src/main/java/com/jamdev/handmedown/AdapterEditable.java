package com.jamdev.handmedown;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterEditable extends RecyclerView.Adapter<AdapterEditable.ViewHolder> {

    // We need to pass in context so that data can be moved to and from the listingsActivity
    private List<Listing> listings = new ArrayList<>();
    private Context listingsActivity;

    // Pass in context
    public AdapterEditable(Context listingsActivity, List<Listing> listings) {
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
        int listingPicture = listings.get(position).getPicture();

        holder.setData(listingID, listingTitle, listingDescription, listingPrice, listingDate, listingPicture);

    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private String listingID;
        private TextView listingTitleView;
        private TextView listingDescriptionView;
        private TextView listingPriceView;
        private TextView listingDateView;
        private ImageView listingPictureView;

        private ImageView editButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listingTitleView = itemView.findViewById(R.id.listing_title);
            listingDescriptionView = itemView.findViewById(R.id.card_description);
            listingPriceView = itemView.findViewById(R.id.card_price_text);
            listingDateView = itemView.findViewById(R.id.card_date_text);
            listingPictureView = itemView.findViewById(R.id.card_picture_container);

            editButton = itemView.findViewById(R.id.card_btn_edit);


            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToEditListing(view);
                }
            });

        }


        // Redirects the user to the ListingEditActivity
        public void goToEditListing(View view) {
            Intent goToEdit = new Intent(listingsActivity, ListingEditActivity.class);
            goToEdit.putExtra("Item Title", listingTitleView.getText().toString());
            goToEdit.putExtra("Item Description", listingDescriptionView.getText().toString());
            goToEdit.putExtra("Item Price", listingPriceView.getText().toString());
            goToEdit.putExtra("Item Category", listingPriceView.getText().toString());
            goToEdit.putExtra("Item ID", listingID);
            listingsActivity.startActivity(goToEdit);
        }


        public void setData(String id, String title, String description, String price, String date, int picture) {
            listingID = id;
            listingTitleView.setText(title);
            listingDescriptionView.setText(description);
            listingPriceView.setText(price);
            listingDateView.setText(date);
            listingPictureView.setImageResource(picture);
        }


    }
}
