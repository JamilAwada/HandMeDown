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

    private List<Listing> itemList = new ArrayList<>();
    private OnListingListener onListingListener;

    public Adapter(List<Listing> itemList, OnListingListener onListingListener){
        this.itemList = itemList;
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

        int resource = itemList.get(position).getPicture();
        String title = itemList.get(position).getTitle();
        String description = itemList.get(position).getDescription();
        String price = itemList.get(position).getPrice();
        String owner = itemList.get(position).getSeller();
        String date = itemList.get(position).getPosted_on();

        holder.setData(resource,title,description,price,owner,date);

    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private ImageView listing_pic;
        private TextView listing_title;
        private TextView listing_description;
        private TextView listing_price;
        private TextView listing_owner;
        private TextView listing_date;
        OnListingListener onListingListener;

        public ViewHolder(@NonNull View itemView, OnListingListener onListingListener) {
            super(itemView);
            this.onListingListener = onListingListener;
            listing_pic = itemView.findViewById(R.id.listing_pic);
            listing_title = itemView.findViewById(R.id.listing_title);
            listing_description = itemView.findViewById(R.id.listing_description);
            listing_price = itemView.findViewById(R.id.listing_price);
            listing_owner = itemView.findViewById(R.id.listing_owner);
            listing_date = itemView.findViewById(R.id.listing_date);

            itemView.setOnClickListener(this);

        }

        public void setData(int resource, String title, String description, String price, String owner, String date) {
            listing_pic.setImageResource(resource);
            listing_title.setText(title);
            listing_description.setText(description);
            listing_price.setText(price);
            listing_owner.setText(owner);
            listing_date.setText(date);

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
