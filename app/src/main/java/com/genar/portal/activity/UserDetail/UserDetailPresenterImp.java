package com.genar.portal.activity.UserDetail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.StringRes;
import android.widget.EditText;

import com.genar.portal.model.User;

class UserDetailPresenterImp implements UserDetailPresenter, UserDetailInteractor.saveDataListener, UserDetailInteractor.imagePickedListener, UserDetailInteractor.signOutListener {
    UserDetailView view;
    UserDetailInteractor interactor;

    UserDetailPresenterImp(UserDetailView view){
        this.view = view;
        interactor = new UserDetailInteractorImp();
    }
    @Override
    public void onImageHandledResult(Bitmap imageBitmap) {
        view.setBitmapImage(imageBitmap);
    }

    @Override
    public void updatePicture(String imageUrl) {
        view.setPicassoImage(imageUrl);
    }

    @Override
    public void changeEditState() {
        view.hideProgressBar();
        view.changeMode();
    }

    @Override
    public void setUser(User user) {
        view.setUserDetails(user);
    }

    @Override
    public void onDbSaveError() {

    }

    @Override
    public void pickImage() {
        interactor.chooseImage(view.getActivity());
    }

    @Override
    public void onEditTextError(EditText editText, @StringRes int error) {
        view.setError(editText, error);
    }

    @Override
    public void sendImageActivityResult(int resultCode, Intent data) {
        interactor.imageActivityResult(resultCode, data, this);
    }

    @Override
    public void updateUserData(EditText etName, EditText etStatus, EditText etPhone, EditText etSkypeId) {
        interactor.saveChanges(etName, etStatus, etPhone,etSkypeId, view.getActivity(), this);
    }

    @Override
    public void signOut() {
        view.showProgressBar();
        interactor.signOut(this);
    }

    @Override
    public void onSignOut(Class targetActivity) {
        view.navigateToActivity(targetActivity);
    }
}
