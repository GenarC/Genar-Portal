package com.genar.portal.activity.Login;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.widget.EditText;

interface LoginInteractor {
    interface OnLoginFinishedListener{
        void onEditTextError(EditText editText, @StringRes int error);
        void onSuccess();
        void onAuthError(@StringRes int error);
    }

    void login(EditText userName, EditText password, Activity activity, OnLoginFinishedListener listener);
}
