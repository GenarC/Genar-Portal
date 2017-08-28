package com.genar.portal.activity.UserDetail;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.StringRes;
import android.widget.EditText;

import com.genar.portal.model.User;

interface UserDetailInteractor {
    interface imagePickedListener{
        void onImageHandledResult(Bitmap imageBitmap);
    }
    interface saveDataListener{
        void onEditTextError(EditText editText, @StringRes int error);
        void updatePicture(String imageUrl);
        void changeEditState();
        void setUser(User user);
        void onDbSaveError();
    }
    interface signOutListener{
        void onSignOut(Class targerActivity);
    }
    void imageActivityResult(int resultCode, Intent data, imagePickedListener listener);
    void chooseImage(Activity activity);
    void saveChanges(EditText etName, EditText etStatus, EditText etPhone, EditText etSkypeId, Activity activity, saveDataListener saveListener);
    void signOut(signOutListener listener);
}
