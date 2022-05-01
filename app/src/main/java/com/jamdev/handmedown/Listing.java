package com.jamdev.handmedown;

import android.os.Parcel;
import android.os.Parcelable;

public class Listing implements Parcelable {

    private String title;
    private String description;
    private String price;
    private String category;
    private String seller;
    private String posted_on;
    private int picture;

    public Listing(String title, String description, String price, String category, String seller, String posted_on, int picture) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.seller = seller;
        this.posted_on = posted_on;
        this.picture = picture;
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

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getPosted_on() {
        return posted_on;
    }

    public void setPosted_on(String posted_on) {
        this.posted_on = posted_on;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    protected Listing(Parcel in) {
        title = in.readString();
        description = in.readString();
        price = in.readString();
        category = in.readString();
        seller = in.readString();
        posted_on = in.readString();
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
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(price);
        parcel.writeString(category);
        parcel.writeString(seller);
        parcel.writeString(posted_on);
        parcel.writeInt(picture);
    }
}
