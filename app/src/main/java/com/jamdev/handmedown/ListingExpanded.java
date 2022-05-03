package com.jamdev.handmedown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ListingExpanded extends AppCompatActivity {

    TextView title;
    TextView price;
    TextView description;
    TextView sellerName;
    TextView sellerAddress;
    TextView sellerNumber;
    TextView date;
    ImageView callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_expanded);

        title = (TextView) findViewById(R.id.et_title);
        price = (TextView) findViewById(R.id.et_price);
        description = (TextView) findViewById(R.id.et_description);
        sellerName = (TextView) findViewById(R.id.tx_seller_name);
        sellerAddress = (TextView) findViewById(R.id.tx_seller_address);
        sellerNumber = (TextView) findViewById(R.id.tx_seller_number);
        date = (TextView) findViewById(R.id.posted_on);
        callButton = (ImageView) findViewById(R.id.btn_call);

        Bundle intent = getIntent().getExtras();
        Listing item = intent.getParcelable("Listing");
        User seller = intent.getParcelable("Seller");

        title.setText(item.getTitle());
        price.setText(item.getPrice());
        description.setText(item.getDescription());
        sellerName.setText(seller.getFullName());
        sellerAddress.setText(seller.getAddress());
        sellerNumber.setText(seller.getPhoneNumber());
        date.setText(item.getPosted_on());

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dial_number = sellerNumber.getText().toString();
                String dialled_number = "tel:" + dial_number;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(dialled_number));
                startActivity(intent);
            }
        });

    }




}