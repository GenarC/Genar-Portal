package com.genar.portal.service;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by cm_gn on 20.07.2017.
 */

public class InstanceIDListenerService extends FirebaseInstanceIdService {

    /**
     * The Application's current Instance ID token is no longer valid
     * and thus a new one must be requested.
     */
    @Override
    public void onTokenRefresh() {

        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            DatabaseReference dbRef;
            String userID = fAuth.getCurrentUser().getUid();
            dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("token");
            String token = FirebaseInstanceId.getInstance().getToken();
            dbRef.setValue(token);
        }
    }

}
