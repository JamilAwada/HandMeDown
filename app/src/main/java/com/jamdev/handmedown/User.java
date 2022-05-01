package com.jamdev.handmedown;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String id;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String username;
    private String email;
    private String password;
    private int profilePicture;


    public User(String fullName, String phoneNumber, String address, String username, String email, int profilePicture, String id) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.id = id;
    }

    public User(String fullName, String phoneNumber, String address, String username, String email, String password, String id) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.username = username;
        this.email = email;
        this.password = password;
        this.id = id;
    }

    public User(String fullName, String phoneNumber, String address, String username, String email, String password, int profilePicture, String id) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.id = id;
    }

    protected User(Parcel in) {
        fullName = in.readString();
        phoneNumber = in.readString();
        address = in.readString();
        username = in.readString();
        email = in.readString();
        password = in.readString();
        profilePicture = in.readInt();
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public int getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fullName);
        parcel.writeString(phoneNumber);
        parcel.writeString(address);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeInt(profilePicture);
        parcel.writeString(id);
    }
}
