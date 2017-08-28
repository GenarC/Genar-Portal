package com.genar.portal.activity.Login;

import android.support.annotation.StringRes;
import android.widget.EditText;
import android.widget.Toast;

import com.genar.portal.activity.FormView;
import com.genar.portal.activity.MainActivity;


class LoginPresenterImp implements LoginPresenter, LoginInteractor.OnLoginFinishedListener{

    private FormView view;
    private LoginInteractor interactor;

    LoginPresenterImp(FormView formView){
        this.view = formView;
        this.interactor = new LoginInteractorImp();
    }


    @Override
    public void onAuthError(@StringRes int error) {
        Toast.makeText(view.getActivity(), error, Toast.LENGTH_LONG).show();
        view.hideProgress();
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
    public void validateCredentials(EditText userName, EditText password) {
        if (userName.getText().length() != 0 && password.getText().length() != 0){
            view.showProgress();
        }
        interactor.login(userName, password, view.getActivity(), this);
    }

    @Override
    public void onDestroy() {

    }
}
