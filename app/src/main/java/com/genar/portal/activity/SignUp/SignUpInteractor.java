package com.genar.portal.activity.SignUp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.StringRes;
import android.widget.EditText;

interface SignUpInteractor {
    interface onSignUpFinishedListener{
        void onEditTextError(EditText editText, @StringRes int error);
        void onSuccess();
        void onAuthError(@StringRes int error);
        void onImageError(@StringRes int error);
    }
    interface onImagePickedListener{
        void onImageHandledResult(Bitmap imageBitmap);
    }
    void signUp(EditText name, EditText email, EditText password, EditText status, EditText phoneNumber, EditText skypeId, Activity activity, onSignUpFinishedListener listener);
    void chooseImage(Activity activity);
    void imageActivityResult(int resultCode, Intent data, onImagePickedListener listener);
}
