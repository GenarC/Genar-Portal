package com.genar.portal.activity.Login;

import android.widget.EditText;

interface LoginPresenter {
    void validateCredentials(EditText userName, EditText password);
    void onDestroy();
}
