package com.jamdev.handmedown;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
        String id = itemList.get(position).getId();
        String title = itemList.get(position).getTitle();
        String description = itemList.get(position).getDescription();
        String price = itemList.get(position).getPrice();
        String owner = itemList.get(position).getSeller();
        String date = itemList.get(position).getPosted_on();

        holder.setData(id,resource,title,description,price,owner,date);

    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener{

        private String deleteListing_url = "http://10.0.2.2/HandMeDown/listing_delete.php?Id=";
        private DeleteListingAPI api;

        private ImageView listing_pic;
        private TextView listing_title;
        private TextView listing_description;
        private TextView listing_price;
        private TextView listing_date;
        private ImageView edit_btn;
        private String listing_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listing_pic = itemView.findViewById(R.id.card_picture);
            listing_title = itemView.findViewById(R.id.listing_title);
            listing_description = itemView.findViewById(R.id.card_description);
            listing_price = itemView.findViewById(R.id.card_price_text);
            listing_date = itemView.findViewById(R.id.card_date_text);
            edit_btn = itemView.findViewById(R.id.card_btn_edit);


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


        public void setData(String id, int resource, String title, String description, String price, String owner, String date) {
            listing_pic.setImageResource(resource);
            listing_title.setText(title);
            listing_description.setText(description);
            listing_price.setText(price);
            listing_date.setText(date);
            listing_id = id;

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch(menuItem.getItemId()){
                case R.id.edit_option:
                    Intent goToEdit = new Intent(listingsActivity, EditListingActivity.class);
                    goToEdit.putExtra("Item Title", listing_title.getText().toString());
                    goToEdit.putExtra("Item Description", listing_description.getText().toString());
                    goToEdit.putExtra("Item Price", listing_price.getText().toString());
                    goToEdit.putExtra("Item Category", listing_price.getText().toString());
                    goToEdit.putExtra("Item ID", listing_id);
                    listingsActivity.startActivity(goToEdit);
                case R.id.delete_option:
                    deleteListing();
                    itemList.remove(getAbsoluteAdapterPosition());
                default:
                    return false;
            }
        }
        public class DeleteListingAPI extends AsyncTask<String, Void, String> {
            protected String doInBackground(String... urls) {
                // URL and HTTP initialization to connect to API 2
                URL url;
                HttpURLConnection http;

                try {
                    // Connect to API 2
                    url = new URL(urls[0]);
                    http = (HttpURLConnection) url.openConnection();

                    // Retrieve API 2 content
                    InputStream in = http.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);

                    // Read API 2 content line by line
                    BufferedReader br = new BufferedReader(reader);
                    StringBuilder sb = new StringBuilder();

                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    br.close();
                    // Return content from API 2
                    return sb.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(String values) {
                super.onPostExecute(values);
                try {
                    Toast.makeText(listingsActivity, values, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }

        public void deleteListing(){
            api = new DeleteListingAPI();
            api.execute(deleteListing_url + listing_id);

        }
    }
}
