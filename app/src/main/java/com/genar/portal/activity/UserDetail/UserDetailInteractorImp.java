package com.genar.portal.activity.UserDetail;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.genar.portal.R;
import com.genar.portal.activity.Login.LoginActivity;
import com.genar.portal.helper.Utils;
import com.genar.portal.model.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

class UserDetailInteractorImp implements UserDetailInteractor {

    private byte[] bitmapData;
    private boolean imageChanged = false;
    private User user = User.getCurrentUser();

    private UploadTask uTask;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private DatabaseReference fDatabaseRef = FirebaseDatabase.getInstance().getReference(Utils.DB_user);



    @Override
    public void chooseImage(Activity activity) {
        if(Utils.isNetworkAvailable(activity)){
            if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(activity);
            }else{
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1003);
            }
        }else{
            Utils.showAlertDialogSettings(activity, activity.getString(R.string.connectionError), activity.getString(R.string.needNetworkToChangeImage), false);
        }
    }
    @Override
    public void imageActivityResult(int resultCode, Intent data, imagePickedListener listener) {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            Uri uri = result.getUri();
            Bitmap resized = Utils.UriToBitmap(uri);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            bitmapData = baos.toByteArray();
            imageChanged=true;

            listener.onImageHandledResult(resized);
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
        }
    }



    @Override
    public void saveChanges(final EditText etName,final EditText etStatus,final  EditText etPhone, final EditText etSkypeId, final Activity activity, final saveDataListener listener) {

        boolean error = false;

        if(etName.getText().length() == 0){
            listener.onEditTextError(etName, R.string.error_mustBeFilled);
            error = true;
        }


        if(!error){
            Runnable r = new Runnable() {
                @Override
                public void run(){
                    final boolean[] offlineSave = new boolean[1];


                    user.setName(etName.getText().toString());

                    if(etStatus.getText().length() == 0){
                        user.setStatus("");
                    }else{
                        user.setStatus(etStatus.getText().toString());
                    }

                    if(etPhone.getText().length() == 0){
                        user.setPhoneNumber("");
                    }else{
                        user.setPhoneNumber(etPhone.getText().toString());
                    }

                    if(etSkypeId.getText().length() == 0){
                        user.setSkypeUsername("");
                    }else{
                        user.setSkypeUsername(etSkypeId.getText().toString());
                    }

                    if(Utils.isNetworkAvailable(activity)){
                        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        offlineSave[0] = false;
                    }else{
                        offlineSave[0] = true;
                    }

                    DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child(Utils.DB_user).child(user.getUserId());

                    Map childUpdates = new HashMap();

                    childUpdates.put("name", etName.getText().toString());
                    childUpdates.put("status", etStatus.getText().toString());
                    childUpdates.put("phoneNumber", etPhone.getText().toString());
                    childUpdates.put("skypeUsername", etSkypeId.getText().toString());

                    dRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null){


                                if(imageChanged){
                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images").child(user.getUserId()+"_"+System.currentTimeMillis()/1000+".jpeg");

                                    uTask = storageRef.putBytes(bitmapData);
                                    uTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(activity, R.string.detailImageUploadError, Toast.LENGTH_SHORT).show();
                                            User.init(user);
                                            listener.updatePicture(User.getCurrentUser().getProfileImage());
                                            imageChanged = false;

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();

                                            fDatabaseRef.child(user.getUserId()).child("profileImage").setValue(downloadUri.toString());

                                            FirebaseStorage.getInstance().getReferenceFromUrl(user.getProfileImage()).delete();

                                            user.setProfileImage(downloadUri.toString());
                                            User.init(user);
                                            listener.setUser(user);
                                            listener.changeEditState();
                                            listener.updatePicture(downloadUri.toString());
                                            imageChanged = false;
                                            Toast.makeText(activity, R.string.changesSaved, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(activity, R.string.changesSaved, Toast.LENGTH_SHORT).show();
                                    User.init(user);
                                    listener.setUser(user);
                                    if(!offlineSave[0]){
                                        listener.changeEditState();
                                    }
                                }
                            }else{
                                Toast.makeText(activity, R.string.changesFailedTryAgain, Toast.LENGTH_SHORT).show();
                            }
                            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            if(!offlineSave[0]){
                                listener.changeEditState();
                            }
                        }
                    });

                    if(offlineSave[0]){
                        Toast.makeText(activity, R.string.changesSavedOffline, Toast.LENGTH_SHORT).show();
                        listener.changeEditState();
                    } //<-- put your code in here.
                }
            };

            Handler h = new Handler();
            h.postDelayed(r, 1000);
        }

    }

    @Override
    public void signOut(final signOutListener listener) {
        Runnable r = new Runnable() {
            @Override
            public void run(){
                fAuth.signOut();
                if (fAuth.getCurrentUser() == null) {
                    listener.onSignOut(LoginActivity.class);
                }
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 1000);
    }
}
