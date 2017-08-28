package com.genar.portal.activity.SignUp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.genar.portal.R;
import com.genar.portal.activity.FormView;
import com.genar.portal.helper.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity implements FormView {

    @BindView(R.id.signup_fullname)
    EditText etFullName;
    @BindView(R.id.signup_email)
    EditText etEmail;
    @BindView(R.id.signup_password)
    EditText etPassword;
    @BindView(R.id.signup_loodosposition)
    EditText etStatus;
    @BindView(R.id.signup_phone)
    EditText etPhone;
    @BindView(R.id.signup_skype)
    EditText etSkypeId;
    @BindView(R.id.signup_profile_image)
    ImageView civProfileImage;
    @BindView(R.id.signup_btn_signup)
    CircularProgressButton btnSignUp;

    SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);
        presenter = new SignUpPresenterImp(this);
    }

    @OnClick(R.id.signup_profile_image)
    public void chooseProfileImage(){
        presenter.pickImage();
    }

    @OnClick(R.id.signup_btn_signup)
    public void signUp(){
        presenter.signUpWithUser(etFullName,etEmail,etPassword,etStatus,etPhone,etSkypeId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            presenter.sendImageActivityResult(resultCode, data);
        }
    }

    @Override
    public void setError(EditText editText,@StringRes int error) {
        editText.setError(getString(error));
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

    public void setImage(Bitmap image){
        civProfileImage.setImageBitmap(image);
    }

    @Override
    public void showProgress() {
        btnSignUp.startAnimation();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideProgress() {
        btnSignUp.revertAnimation();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}