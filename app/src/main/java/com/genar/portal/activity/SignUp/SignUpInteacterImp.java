package com.genar.portal.activity.SignUp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.genar.portal.GetBitmapTask;
import com.genar.portal.R;
import com.genar.portal.activity.MainActivity;
import com.genar.portal.helper.Utils;
import com.genar.portal.model.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;


class SignUpInteacterImp implements SignUpInteractor {

    private String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private byte[] bitmapData;



    private UploadTask uTask;
    private StorageReference sRef;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public void signUp(final EditText name, final  EditText email, final  EditText password, final  EditText status, final  EditText phoneNumber, final EditText skypeId, final Activity activity,final onSignUpFinishedListener listener) {

        boolean error = false;

        if(name.getText().length() == 0){
            listener.onEditTextError(name,R.string.error_mustBeFilled);
            error = true;
        }
        if(email.getText().length() == 0){
            listener.onEditTextError(email,R.string.error_mustBeFilled);
            error = true;
        }
        if(password.getText().length() == 0){
            listener.onEditTextError(password,R.string.error_mustBeFilled);
            error = true;
        }

        if(!error){
            auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                            if (!task.isSuccessful()) {
                                Toast.makeText(activity, R.string.signUpLoginError,
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                final User u = new User();

                                FirebaseUser fUser = auth.getCurrentUser();
                                final String userID = fUser.getUid();

                                u.setName(name.getText().toString());
                                u.setUserId(userID);
                                u.setRole("user");

                                u.setUserName(email.getText().toString());
                                u.setToken(FirebaseInstanceId.getInstance().getToken());

                                if(status.getText().length() == 0){
                                    u.setStatus("");
                                }else{
                                    u.setStatus(status.getText().toString());
                                }

                                if(phoneNumber.getText().length() == 0){
                                    u.setPhoneNumber("");
                                }else{
                                    u.setPhoneNumber(phoneNumber.getText().toString());
                                }

                                if(skypeId.getText().length() == 0){
                                    u.setSkypeUsername("");
                                }else{
                                    u.setSkypeUsername(skypeId.getText().toString());
                                }



                                if (bitmapData == null) {
                                    u.setProfileImage("https://firebasestorage.googleapis.com/v0/b/fir-demo-7d21b.appspot.com/o/profile_images%2Fdefault_image.png?alt=media&token=fa4a4c40-ccc5-498b-9172-fc16e6c992b2");
                                    mDatabase.child(userID).setValue(u, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if(databaseError != null){
                                                Toast.makeText(activity, databaseError.toString(),Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(activity, R.string.signUpDbSaveSuccess,Toast.LENGTH_SHORT).show();
                                                listener.onSuccess();
                                                User.init(u);
                                                activity.startActivity(new Intent(activity, MainActivity.class));
                                                activity.finish();
                                            }
                                        }
                                    });
                                }else{
                                    sRef = FirebaseStorage.getInstance().getReference().child("profile_images").child(userID+"_"+System.currentTimeMillis()/1000+".jpeg");

                                    uTask = sRef.putBytes(bitmapData);

                                    uTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Toast t = Toast.makeText(activity, R.string.signUpProfileImageUploadError,Toast.LENGTH_SHORT);
                                            t.show();

                                            u.setProfileImage("https://firebasestorage.googleapis.com/v0/b/fir-demo-7d21b.appspot.com/o/profile_images%2Fdefault_image.png?alt=media&token=fa4a4c40-ccc5-498b-9172-fc16e6c992b2");
                                            mDatabase.child(userID).setValue(u, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    if(databaseError != null){
                                                        Toast.makeText(activity, databaseError.toString(),Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(activity, R.string.signUpDbSaveSuccess,Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                            @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();

                                            new Thread(new GetBitmapTask(downloadUri.toString(), new GetBitmapTask.Callback() {
                                                @Override public void onFinish(Bitmap bitmap) {
                                                    GetBitmapTask.SaveImage(bitmap,u.getUserName());
                                                }

                                                @Override public void onError(Throwable t) {
                                                    //here you have to handle error
                                                }
                                            })).start();

//                                        Toast.makeText(SignUpActivity.this,"Yükleme Başarılı",Toast.LENGTH_SHORT).show();

                                            u.setProfileImage(downloadUri.toString());
                                            u.setRole("user");

                                            mDatabase.child(userID).setValue(u, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    if(databaseError != null){
                                                        Toast.makeText(activity, databaseError.toString(),Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        DatabaseReference dbRef;
                                                        dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(u.getUserId()).child("token");
                                                        dbRef.setValue(u.getToken());
                                                        Toast.makeText(activity, R.string.signUpDbSaveSuccess,Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            listener.onSuccess();
                                            User.init(u);
                                            activity.startActivity(new Intent(activity, MainActivity.class));
                                            activity.finish();
                                        }
                                    });
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void chooseImage(Activity activity) {
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            CropImage.activity()
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(activity);
        }else{
            ActivityCompat.requestPermissions(activity,perms,1002);
        }
    }

    @Override
    public void imageActivityResult(int resultCode, Intent data, onImagePickedListener listener) {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            Uri uri = result.getUri();
            Bitmap resized = Utils.UriToBitmap(uri);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            bitmapData = baos.toByteArray();

            listener.onImageHandledResult(resized);
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
        }
    }


}
