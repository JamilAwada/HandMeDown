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

public class ListingExpandedActivity extends AppCompatActivity {

    private TextView listingTitle;
    private TextView listingPrice;
    private TextView listingDescription;
    private TextView sellerName;
    private TextView sellerAddress;
    private TextView sellerNumber;
    private TextView listingDate;
    private ImageView listingPicture;
    private CircularImageView sellerPicture;

    private ImageView callButton;
    private ImageView returnButton;

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
        listingDate = (TextView) findViewById(R.id.posted_on);
        listingPicture = (ImageView) findViewById(R.id.card_picture_container);

        sellerName = (TextView) findViewById(R.id.tx_seller_name);
        sellerAddress = (TextView) findViewById(R.id.tx_seller_address);
        sellerNumber = (TextView) findViewById(R.id.tx_seller_number);
        sellerPicture = (CircularImageView) findViewById(R.id.card_seller_picture_container);

        callButton = (ImageView) findViewById(R.id.btn_call);
        returnButton = (ImageView) findViewById(R.id.btn_return);

        // Get extra from whichever activity led here
        Bundle fromListings = getIntent().getExtras();
        Listing listing = fromListings.getParcelable("Listing");
        User seller = fromListings.getParcelable("Seller");

        listingTitle.setText(listing.getTitle());
        listingPrice.setText(listing.getPrice());
        listingDescription.setText(listing.getDescription());
        listingDate.setText(listing.getPosted_on());
        listingPicture.setImageResource(listing.getPicture());

        sellerName.setText(seller.getName());
        sellerAddress.setText(seller.getAddress());
        sellerNumber.setText(seller.getNumber());
        sellerPicture.setImageResource(seller.getPicture());

        // The call button redirects the user to their device's native phone application
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sellerNumber = ListingExpandedActivity.this.sellerNumber.getText().toString();
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