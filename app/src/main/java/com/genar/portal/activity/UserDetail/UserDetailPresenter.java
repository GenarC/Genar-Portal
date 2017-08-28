package com.genar.portal.activity.UserDetail;

import android.content.Intent;
import android.widget.EditText;

interface UserDetailPresenter {
    void pickImage();
    void sendImageActivityResult(int resultCode, Intent data);
    void updateUserData(EditText etName, EditText etStatus, EditText etPhone, EditText etSkypeId);
    void signOut();
}
