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

    private List<Listing> listings = new ArrayList<>();
    private OnListingListener onListingListener;

    public Adapter(List<Listing> listings, OnListingListener onListingListener){
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

        String listingTitle = listings.get(position).getTitle();
        String listingDescription = listings.get(position).getDescription();
        String listingPrice = listings.get(position).getPrice();
        String listingDate = listings.get(position).getPosted_on();
        String listingSeller = listings.get(position).getSeller();
        int listingPicture = listings.get(position).getPicture();

        holder.setData(listingPicture,listingTitle,listingDescription,listingPrice,listingSeller,listingDate);

    }
    @Override
    public int getItemCount() {
        return listings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private TextView listingTitleView;
        private TextView listingDescriptionView;
        private TextView listingPriceView;
        private TextView listingDateView;
        private TextView listingOwnerView;
        private ImageView listingPictureView;

        OnListingListener onListingListener;

        public ViewHolder(@NonNull View itemView, OnListingListener onListingListener) {
            super(itemView);
            this.onListingListener = onListingListener;
            listingPictureView = itemView.findViewById(R.id.card_picture);
            listingTitleView = itemView.findViewById(R.id.listing_title);
            listingDescriptionView = itemView.findViewById(R.id.card_description);
            listingPriceView = itemView.findViewById(R.id.card_price_text);
            listingOwnerView = itemView.findViewById(R.id.card_seller_text);
            listingDateView = itemView.findViewById(R.id.card_date_text);

            itemView.setOnClickListener(this);

        }

        public void setData(int resource, String title, String description, String price, String owner, String date) {
            listingPictureView.setImageResource(resource);
            listingTitleView.setText(title);
            listingDescriptionView.setText(description);
            listingPriceView.setText(price);
            listingOwnerView.setText(owner);
            listingDateView.setText(date);

        }

        @Override
        public void onClick(View view) {
            onListingListener.OnListingClick(getBindingAdapterPosition());
        }
    }

    public interface OnListingListener{
        void OnListingClick(int position);
    }
}
