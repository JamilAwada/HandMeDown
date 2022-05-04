package com.jamdev.handmedown;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String id;
    private String name;
    private String number;
    private String address;
    private String username;
    private String email;
    private String password;
    private int picture;


    public User(String id, String name, String number, String address, String username, String email, int picture) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.address = address;
        this.username = username;
        this.email = email;
        this.picture = picture;
    }

    public User(String id, String name, String number, String address, String username, String email, String password) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.address = address;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String id, String name, String number, String address, String username, String email, String password, int picture) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.address = address;
        this.username = username;
        this.email = email;
        this.password = password;
        this.picture = picture;
    }

    protected User(Parcel in) {
        name = in.readString();
        number = in.readString();
        address = in.readString();
        username = in.readString();
        email = in.readString();
        password = in.readString();
        picture = in.readInt();
        id = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(number);
        parcel.writeString(address);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeInt(picture);
        parcel.writeString(id);
    }
}
