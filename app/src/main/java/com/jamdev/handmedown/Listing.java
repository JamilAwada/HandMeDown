package com.jamdev.handmedown;

import android.os.Parcel;
import android.os.Parcelable;

public class Listing implements Parcelable {

    private String id;
    private String title;
    private String description;
    private String price;
    private String category;
    private String posted_on;
    private String seller;
    private String sellerName;
    private int picture;

    public Listing(int picture, String id, String title, String description, String price, String category, String posted_on, String seller) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.posted_on = posted_on;
        this.seller = seller;
        this.picture = picture;
    }

    public Listing(String title, String description, String price, String category, String posted_on, String seller, int picture) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.posted_on = posted_on;
        this.seller = seller;
        this.picture = picture;
    }

    public Listing(String title, String description, String price, String category, String posted_on, String seller, String sellerName, int picture) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.posted_on = posted_on;
        this.seller = seller;
        this.sellerName = sellerName;
        this.picture = picture;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPosted_on() {
        return posted_on;
    }

    public void setPosted_on(String posted_on) {
        this.posted_on = posted_on;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    protected Listing(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        price = in.readString();
        category = in.readString();
        posted_on = in.readString();
        seller = in.readString();
        sellerName = in.readString();
        picture = in.readInt();
    }


    public static final Creator<Listing> CREATOR = new Creator<Listing>() {
        @Override
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        @Override
        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(price);
        parcel.writeString(category);
        parcel.writeString(posted_on);
        parcel.writeString(seller);
        parcel.writeString(sellerName);
        parcel.writeInt(picture);
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}
