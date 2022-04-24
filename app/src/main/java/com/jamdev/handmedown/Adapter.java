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

    private List<ModelClass> itemList = new ArrayList<>();

    public Adapter(List<ModelClass> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        int resource = itemList.get(position).getListing_pic();
        String title = itemList.get(position).getListing_title();
        String description = itemList.get(position).getListing_description();
        String price = itemList.get(position).getListing_price();
        String owner = itemList.get(position).getListing_owner();
        String date = itemList.get(position).getListing_date();
        int divider = itemList.get(position).getSeperator();

        holder.setData(resource,title,description,price,owner,date,divider);

    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView listing_pic;
        private TextView listing_title;
        private TextView listing_description;
        private TextView listing_price;
        private TextView listing_owner;
        private TextView listing_date;
        private ImageView seperator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listing_pic = itemView.findViewById(R.id.listing_pic);
            listing_title = itemView.findViewById(R.id.listing_title);
            listing_description = itemView.findViewById(R.id.listing_description);
            listing_price = itemView.findViewById(R.id.listing_price);
            listing_owner = itemView.findViewById(R.id.listing_owner);
            listing_date = itemView.findViewById(R.id.listing_date);
            seperator = itemView.findViewById(R.id.seperator);

        }

        public void setData(int resource, String title, String description, String price, String owner, String date, int divider) {
            listing_pic.setImageResource(resource);
            listing_title.setText(title);
            listing_description.setText(description);
            listing_price.setText(price);
            listing_owner.setText(owner);
            listing_date.setText(date);
            seperator.setImageResource(divider);
        }
    }
}
