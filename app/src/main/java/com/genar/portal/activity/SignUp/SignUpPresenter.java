package com.genar.portal.activity.SignUp;

import android.content.Intent;
import android.widget.EditText;

interface SignUpPresenter {
    void signUpWithUser(EditText name, EditText email, EditText password, EditText status, EditText phoneNumber, EditText skypeId);
    void pickImage();
    void sendImageActivityResult(int resultCode, Intent data);
}
