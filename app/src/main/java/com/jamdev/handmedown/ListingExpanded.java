package com.jamdev.handmedown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

public class ListingExpanded extends AppCompatActivity {

    TextView listingTitle;
    TextView listingPrice;
    TextView listingDescription;
    TextView sellerName;
    TextView sellerAddress;
    TextView sellerNumber;
    TextView listingDate;
    CircularImageView sellerPicture;

    int fetchedPicture;

    ImageView callButton;
    ImageView returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // These 3 lines hide the title and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_listing_expanded);

        listingTitle = (TextView) findViewById(R.id.et_title);
        listingPrice = (TextView) findViewById(R.id.et_price);
        listingDescription = (TextView) findViewById(R.id.et_description);
        sellerName = (TextView) findViewById(R.id.tx_seller_name);
        sellerAddress = (TextView) findViewById(R.id.tx_seller_address);
        sellerNumber = (TextView) findViewById(R.id.tx_seller_number);
        listingDate = (TextView) findViewById(R.id.posted_on);
        sellerPicture = (CircularImageView) findViewById(R.id.card_seller_picture_container);

        callButton = (ImageView) findViewById(R.id.btn_call);
        returnButton = (ImageView) findViewById(R.id.btn_return);

        Bundle fromListings = getIntent().getExtras();
        Listing item = fromListings.getParcelable("Listing");
        User seller = fromListings.getParcelable("Seller");

        listingTitle.setText(item.getTitle());
        listingPrice.setText(item.getPrice());
        listingDescription.setText(item.getDescription());
        sellerName.setText(seller.getName());
        sellerAddress.setText(seller.getAddress());
        sellerNumber.setText(seller.getNumber());
        listingDate.setText(item.getPosted_on());
        sellerPicture.setImageResource(seller.getPicture());

        // The call button redirects the user to their device's native phone application
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sellerNumber = ListingExpanded.this.sellerNumber.getText().toString();
                String dialledNumber = "tel:" + sellerNumber;
                Intent goToDevicePhoneApp = new Intent(Intent.ACTION_DIAL);
                goToDevicePhoneApp.setData(Uri.parse(dialledNumber));
                startActivity(goToDevicePhoneApp);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                onBackPressed();
            }
        });

    }




}