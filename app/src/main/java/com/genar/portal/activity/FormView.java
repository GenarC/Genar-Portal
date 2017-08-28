package com.genar.portal.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.StringRes;
import android.widget.EditText;

public interface FormView {

    void showProgress();

    void setImage(Bitmap image);

    void hideProgress();

    void setError(EditText editText, @StringRes int error);

    void navigateToActivity(Class targetClass);

    Activity getActivity();
}