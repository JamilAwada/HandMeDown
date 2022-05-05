package com.jamdev.handmedown;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    // The onListingListener listens for user clicks
    // It will be used to redirect the user to the ListingExpanded class
    private List<Listing> listings = new ArrayList<>();
    private OnListingListener onListingListener;

    public Adapter(List<Listing> listings, OnListingListener onListingListener) {
        this.listings = listings;
        this.onListingListener = onListingListener;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_design, parent, false);
        return new ViewHolder(view, onListingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        // This is all data we need to display on the individual listing,
        // Except for listingSeller, which will be used to fetch user details with the respective API instead
        String listingTitle = listings.get(position).getTitle();
        String listingDescription = listings.get(position).getDescription();
        String listingPrice = listings.get(position).getPrice();
        String listingDate = listings.get(position).getPosted_on();
        String listingSeller = listings.get(position).getSeller();
        String listingSellerName = listings.get(position).getSellerName();
        int listingPicture = listings.get(position).getPicture();

        holder.setData(listingTitle, listingDescription, listingPrice, listingDate, listingSeller, listingSellerName, listingPicture);

    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The UI components of the individual listing
        private TextView listingTitleView;
        private TextView listingDescriptionView;
        private TextView listingPriceView;
        private TextView listingDateView;
        private TextView listingSellerView;
        private ImageView listingPictureView;

        OnListingListener onListingListener;

        // Link the components to the layout components
        public ViewHolder(@NonNull View itemView, OnListingListener onListingListener) {
            super(itemView);
            this.onListingListener = onListingListener;
            listingPictureView = itemView.findViewById(R.id.card_picture_container);
            listingTitleView = itemView.findViewById(R.id.listing_title);
            listingDescriptionView = itemView.findViewById(R.id.card_description);
            listingPriceView = itemView.findViewById(R.id.card_price_text);
            listingSellerView = itemView.findViewById(R.id.card_seller_text);
            listingDateView = itemView.findViewById(R.id.card_date_text);

            itemView.setOnClickListener(this);

        }

        // Set their data to hold whatever the earlier onBindViewHolder received from the click listener
        public void setData(String title, String description, String price, String date, String seller, String sellerName, int picture) {
            listingTitleView.setText(title);
            listingDescriptionView.setText(description);
            listingPriceView.setText(price);
            listingDateView.setText(date);
            listingSellerView.setText(sellerName);
            listingPictureView.setImageResource(picture);

        }

        // Get the position of the item clicked
        @Override
        public void onClick(View view) {
            onListingListener.OnListingClick(getBindingAdapterPosition());
        }
    }

    public interface OnListingListener {
        void OnListingClick(int position);
    }
}
