package com.genar.portal.model;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Notification {
    private String date;
    private String name;
    private String post;
    private String title;
    private String userId;
    private String postId;
    private String userImageLink;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserImageLink() {
        return userImageLink;
    }

    public void setUserImageLink(String userImageLink) {
        this.userImageLink = userImageLink;
    }

    public Notification( String title, String post) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy, H:mm");
        date = formatter.format(new Date());

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        this.post = post;
        this.title = title;
    }

    public Notification() {
    }
}
