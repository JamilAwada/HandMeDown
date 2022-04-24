package com.jamdev.handmedown;

public class ModelClass {

    private int listing_pic;
    private String listing_title;
    private String listing_description;
    private String listing_price;
    private String listing_owner;
    private String listing_date;
    private int seperator;

    ModelClass(int listing_pic, String listing_title, String listing_description, String listing_price, String listing_owner, String listing_date, int seperator){
        this.listing_pic = listing_pic;
        this.listing_title = listing_title;
        this.listing_description = listing_description;
        this.listing_price = listing_price;
        this.listing_owner = listing_owner;
        this.listing_date = listing_date;
        this.seperator = seperator;
    }

    public int getListing_pic() {
        return listing_pic;
    }

    public String getListing_title() {
        return listing_title;
    }

    public String getListing_description() {
        return listing_description;
    }

    public String getListing_price() {
        return listing_price;
    }

    public String getListing_owner() {
        return listing_owner;
    }

    public String getListing_date() {
        return listing_date;
    }

    public int getSeperator() {
        return seperator;
    }
}
