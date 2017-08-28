package com.genar.portal.activity.Login;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.genar.portal.GetBitmapTask;
import com.genar.portal.R;
import com.genar.portal.helper.Utils;
import com.genar.portal.model.User;


class LoginInteractorImp implements LoginInteractor {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public void login(EditText userName, final EditText password,final Activity activity, final OnLoginFinishedListener listener) {
        final boolean[] error = {false};
        if (TextUtils.isEmpty(userName.getText().toString())){
            listener.onEditTextError(userName, R.string.missingMailAddress);
            error[0] = true;
        }
        if (TextUtils.isEmpty(password.getText().toString())){
            listener.onEditTextError(password, R.string.missingPassword);
            error[0] = true;
        }
        if (!error[0]){
            auth.signInWithEmailAndPassword(userName.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                error[0] = true;
                                if (password.getText().length() < 6) {
                                    listener.onEditTextError(password, R.string.minimum_password);
                                } else {
                                    listener.onAuthError(R.string.auth_failed);
                                }

                            } else {
                                DatabaseReference dbRef;
                                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                dbRef = FirebaseDatabase.getInstance().getReference().child(Utils.DB_user).child(userID).child("token");
                                String token = FirebaseInstanceId.getInstance().getToken();

                                dbRef.setValue(token, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if(databaseError != null){
                                            Toast.makeText(activity, /*databaseError.getMessage()*/ "Token Error",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                if(!uId.isEmpty()){
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Utils.DB_user).child(uId);

                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User.init(dataSnapshot.getValue(User.class));

                                            new Thread(new GetBitmapTask(User.getCurrentUser().getProfileImage(), new GetBitmapTask.Callback() {
                                                @Override public void onFinish(Bitmap bitmap) {
                                                    GetBitmapTask.SaveImage(bitmap,User.getCurrentUser().getUserName());
                                                    listener.onSuccess();
                                                }

                                                @Override public void onError(Throwable t) {
                                                    //here you have to handle error
                                                }
                                            })).start();
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else{
                                    listener.onAuthError(R.string.loginErrorTryAgain);
                                    auth.signOut();
                                }
                            }
                        }
                    });
        }
    }
}
