package com.jamdev.handmedown;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterEditable extends RecyclerView.Adapter<AdapterEditable.ViewHolder> {

    private List<Listing> itemList = new ArrayList<>();
    private Context listingsActivity;

    public AdapterEditable(Context listingsActivity, List<Listing> itemList){
        this.listingsActivity = listingsActivity;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public AdapterEditable.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_design_editable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEditable.ViewHolder holder, int position) {

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

    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener{

        private ImageView listing_pic;
        private TextView listing_title;
        private TextView listing_description;
        private TextView listing_price;
        private TextView listing_date;
        private ImageView edit_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listing_pic = itemView.findViewById(R.id.listing_pic);
            listing_title = itemView.findViewById(R.id.listing_title);
            listing_description = itemView.findViewById(R.id.listing_description);
            listing_price = itemView.findViewById(R.id.listing_price);
            listing_date = itemView.findViewById(R.id.listing_date);
            edit_btn = itemView.findViewById(R.id.btn_edit_listing);


            edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopup(edit_btn);
                }
            });



        }

        public void showPopup(ImageView edit_btn){
            PopupMenu popup = new PopupMenu(listingsActivity, edit_btn);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.popup_menu);
            popup.show();
        }


        public void setData(int resource, String title, String description, String price, String owner, String date) {
            listing_pic.setImageResource(resource);
            listing_title.setText(title);
            listing_description.setText(description);
            listing_price.setText(price);
            listing_date.setText(date);

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch(menuItem.getItemId()){
                case R.id.edit_option:
                    Intent goToEdit = new Intent(listingsActivity, EditListingActivity.class);
                    listingsActivity.startActivity(goToEdit);
                default:
                    return false;
            }
        }
    }
}
