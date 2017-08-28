package com.genar.portal.activity.UserDetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.brouding.blockbutton.BlockButton;
import com.genar.portal.R;
import com.genar.portal.helper.Utils;
import com.genar.portal.model.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailActivity extends AppCompatActivity implements UserDetailView{

    @BindView(R.id.detail_rootLayout)
    ConstraintLayout clRoot;
    @BindView(R.id.userDetail_cardView)
    CardView cvView;
    @BindView(R.id.detail_profile_image)
    CircleImageView civProfileImage;
    @BindView(R.id.detail_txt_name)
    EditText etName;
    @BindView(R.id.detail_txt_status)
    EditText etStatus;
    @BindView(R.id.detail_txtphone)
    EditText etPhone;
    @BindView(R.id.detail_txt_email)
    EditText etEmail;
    @BindView(R.id.detail_txt_skype)
    EditText etSkypeId;
    @BindView(R.id.detail_btn_logout)
    CircularProgressButton btnLogOut;
    @BindView(R.id.detail_btnedit)
    BlockButton btnEdit;
    @BindView(R.id.blockbutton_text)
    TextView tvBlockButtonText;


    private boolean editMode;

    private ArrayList<EditText> editList = new ArrayList<>();

    UserDetailPresenter presenter;

    public void initialize(){
        editMode = false;
        etEmail.setEnabled(false);

        editList.add(etName);
        editList.add(etStatus);
        editList.add(etPhone);
        editList.add(etSkypeId);
        civProfileImage.setClickable(false);

        for (EditText et: editList) {
            et.setEnabled(false);
        }

        setUserDetails(User.getCurrentUser());
        setPicassoImage(User.getCurrentUser().getProfileImage());

        clRoot.setAlpha(0f);

        clRoot.animate()
                .alpha(1f)
                .setDuration(700);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);

        presenter = new UserDetailPresenterImp(this);

        initialize();
    }

    @Override
    public void setError(EditText editText, @StringRes int error) {
        editText.setError(getString(error));
        hideProgressBar();
    }

    @OnClick(R.id.detail_btn_logout)
    public void logOutOrSave(){
        if(editMode){
            if(etName.getText().length() != 0){
                showProgressBar();
                editMode = false;
            }
            presenter.updateUserData(etName,etStatus,etPhone,etSkypeId);
        }else{
            presenter.signOut();
        }
    }

    @OnClick(R.id.detail_btnedit)
    public void changeEditState(){
        changeMode();
    }

    @OnClick(R.id.detail_profile_image)
    public void chooseProfileImage(){
        presenter.pickImage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            presenter.sendImageActivityResult(resultCode, data);

            /*CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String path = resultUri.getPath();

                Bitmap resized = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), Utils.THUMBSIZE, Utils.THUMBSIZE);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                bitmapData = baos.toByteArray();

                civProfileImage.setImageBitmap(resized);
                imageChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }*/
        }
    }

    @Override
    public void setPicassoImage(final String imageUrl){
        Picasso.with(UserDetailActivity.this)
                .load(imageUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.login)
                .into(civProfileImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(UserDetailActivity.this)
                                .load(imageUrl)
                                .placeholder(R.drawable.login)
                                .error(R.drawable.cirle)
                                .into(civProfileImage);
                    }
                });
    }

    @Override
    public void setUserDetails(User user){
        etName.setText(user.getName());
        etStatus.setText(user.getStatus());
        etPhone.setText(user.getPhoneNumber());
        etEmail.setText(user.getUserName());
        etSkypeId.setText(user.getSkypeUsername());
    }

    @Override
    public void changeMode() {
        editMode = !editMode;

        if(editMode){
            //Düzenle tıklandı
            btnLogOut.setText("Kaydet");
            tvBlockButtonText.setText("İptal");

            civProfileImage.setClickable(true);
            for (EditText et: editList) {
                et.setEnabled(true);
                et.setBackgroundResource(R.drawable.text_border);
            }
        }else{
            //İptal tıklandı
            btnLogOut.setText(R.string.btn_logOut);
            tvBlockButtonText.setText(R.string.btn_edit);

            civProfileImage.setClickable(false);
//            imageChanged = false;
            setUserDetails(User.getCurrentUser());
            setPicassoImage(User.getCurrentUser().getProfileImage());

            for (EditText et: editList) {
                et.setEnabled(false);
                et.setError(null);//removes error
                et.clearFocus();
                et.setBackgroundResource(R.drawable.edittext_background);
            }
        }
    }

    @Override
    public void setBitmapImage(Bitmap image) {
        civProfileImage.setImageBitmap(image);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void navigateToActivity(Class targetActivity) {
        Utils.startActivityWithoutFinish(getActivity(),targetActivity);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {

        clRoot.animate()
                .alpha(0f)
                .setDuration(700);

        finish();
    }

    @Override
    public void hideProgressBar() {
        btnLogOut.revertAnimation(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                btnLogOut.setBackgroundResource(R.drawable.button_background);
            }
        });
    }

    @Override
    public void showProgressBar() {
        btnLogOut.startAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
