package com.genar.portal.activity.Login;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.genar.portal.R;
import com.genar.portal.activity.FormView;
import com.genar.portal.activity.SignUp.SignUpActivity;
import com.genar.portal.helper.Utils;
import com.google.android.gms.common.api.GoogleApiClient;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements FormView {

    @BindView(R.id.login_mail)
    EditText etUserName;
    @BindView(R.id.login_password)
    EditText etPassword;
    @BindView(R.id.login_btn_login)
    CircularProgressButton btnLogin;

    private LoginPresenter presenter;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        presenter = new LoginPresenterImp(this);

        etPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                }
                return false;
            }
        });











    }

    @OnClick(R.id.login_btn_login)
    public void login(){

        presenter.validateCredentials(etUserName, etPassword);

        if(!Utils.isNetworkAvailable(LoginActivity.this)){
            Utils.showAlertDialogSettings(LoginActivity.this,getString(R.string.connectionError),getString(R.string.checkConnectionToLogin), false);
        }
    }

    @Override
    public void onBackPressed(){
       finish();
    }

    @OnClick(R.id.login_btn_signup)
    public void jumpToSignUp (View view){
        Utils.startActivityWithoutFinish(this,SignUpActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        btnLogin.startAnimation();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void setImage(Bitmap image) {

    }

    @Override
    public void hideProgress() {
            btnLogin.revertAnimation(new OnAnimationEndListener() {
                @Override
                public void onAnimationEnd() {
                    btnLogin.setBackgroundResource(R.drawable.button_background);
                }
            });
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void setError(EditText editText,@StringRes int error) {
        editText.setError(getString(error));
        hideProgress();
    }

    @Override
    public void navigateToActivity(Class targetClass) {
        Utils.startActivity(getActivity(),targetClass);
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
