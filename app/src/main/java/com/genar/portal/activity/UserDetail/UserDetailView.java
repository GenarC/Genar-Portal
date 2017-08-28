package com.genar.portal.activity.UserDetail;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.StringRes;
import android.widget.EditText;

import com.genar.portal.model.User;

interface UserDetailView {
    void setUserDetails(User user);
    void changeMode();
    void setBitmapImage(Bitmap image);
    void setPicassoImage(String imageUrl);
    Activity getActivity();
    void navigateToActivity(Class targetActivity);
    void showProgressBar();
    void hideProgressBar();
    void setError(EditText editText, @StringRes int error);
}
