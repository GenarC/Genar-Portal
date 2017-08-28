package com.genar.portal.activity.SignUp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.StringRes;
import android.widget.EditText;
import android.widget.Toast;

import com.genar.portal.activity.FormView;
import com.genar.portal.activity.MainActivity;

class SignUpPresenterImp implements SignUpPresenter, SignUpInteractor.onSignUpFinishedListener, SignUpInteractor.onImagePickedListener {

    private FormView view;
    private SignUpInteractor interactor;

    SignUpPresenterImp(FormView view){
        this.view = view;
        this.interactor = new SignUpInteacterImp();
    }

    @Override
    public void onEditTextError(EditText editText,@StringRes int error) {
        view.setError(editText, error);
        view.hideProgress();
    }

    @Override
    public void onSuccess() {
        view.navigateToActivity(MainActivity.class);
    }

    @Override
    public void onAuthError(@StringRes int error) {
        Toast.makeText(view.getActivity(), error, Toast.LENGTH_LONG).show();
        view.hideProgress();
    }

    @Override
    public void onImageError(@StringRes int error) {
        view.hideProgress();
        Toast.makeText(view.getActivity(),error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void signUpWithUser(EditText name, EditText email, EditText password, EditText status, EditText phoneNumber, EditText skypeId) {
        if(name.getText().length() !=0 && email.getText().length() !=0 && password.getText().length() !=0){
            view.showProgress();
        }
        interactor.signUp(name,email,password, status,phoneNumber,skypeId, view.getActivity(), this);

        /*if(name.getText().length() !=0 && email.getText().length() !=0 && password.getText().length() !=0 && status.getText().length() !=0 && phoneNumber.getText().length() !=0 && skypeId.getText().length() !=0){
            view.showProgress();
        }
        interactor.signUp(name,email,password, status,phoneNumber,skypeId, view.getActivity(), this);*/
    }

    @Override
    public void pickImage() {
        interactor.chooseImage(view.getActivity());
    }

    @Override
    public void sendImageActivityResult(int resultCode, Intent data) {
        interactor.imageActivityResult(resultCode, data, this);
    }

    @Override
    public void onImageHandledResult(Bitmap imageBitmap) {
        view.setImage(imageBitmap);
    }
}
