package com.genar.portal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Parcelable, Serializable{

    private static User currentUser;





    private String name;
    private String phoneNumber;
    private String profileImage;
    private String status;
    private String token;
    private String userId;
    private String userName;
    private String role;
    private String skypeUsername;


    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getToken(){
        return token;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSkypeUsername() {
        return skypeUsername;
    }

    public void setSkypeUsername(String skypeUsername) {
        this.skypeUsername = skypeUsername;
    }

    public String getUserId() {
        return userId;
    }

    public static User getCurrentUser(){
        return currentUser;
    }

    public static void init(User user){
        currentUser = user;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(profileImage);
        dest.writeString(status);
        dest.writeString(token);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(role);
    }
    private User(Parcel in){
        this.name = in.readString();
        this.phoneNumber = in.readString();
        this.profileImage = in.readString();
        this.status = in.readString();
        this.token = in.readString();
        this.userId = in.readString();
        this.userName = in.readString();
        this.role = in.readString();
    }
    public static final Creator<User> CREATOR = new Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    public static boolean currentUserHasToken(){
        if(currentUser.token != null){
            return true;
        }else{
            return false;
        }
    }
}
