package com.genar.portal.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.genar.portal.R;
import com.genar.portal.activity.Login.LoginActivity;
import com.genar.portal.helper.Utils;
import com.genar.portal.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private boolean networkConnection;
    @BindView(R.id.splashLogo)
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        networkConnection = Utils.isNetworkAvailable(this);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            int SPLASH_DISPLAY_LENGTH = 1500;
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    String uId = auth.getCurrentUser().getUid();

                    FirebaseDatabase.getInstance().getReference(Utils.DB_user).child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            User.init(user);

                            Utils.startActivity(SplashActivity.this,MainActivity.class);
                            finish();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            if(networkConnection) {
                                Toast.makeText(SplashActivity.this, R.string.splashLoginErrorTryAgain, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                Toast.makeText(SplashActivity.this, R.string.splashLoginErrorCheckNetwork, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }, SPLASH_DISPLAY_LENGTH);




//            Utils.startActivity(SplashActivity.this,MainActivity.class);


        }else{
            int SPLASH_DISPLAY_LENGTH = 1500;
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    if(!networkConnection){
                        Utils.showAlertDialogSettings(SplashActivity.this,getString(R.string.connectionError),getString(R.string.checkConnectionToLogin), true);
                    }else{
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            View statusBar = findViewById(android.R.id.statusBarBackground);
                            View navigationBar = findViewById(android.R.id.navigationBarBackground);

                            Pair<View,String> p1= Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
                            Pair<View,String> p2= Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
                            Pair<View,String> p3 = Pair.create((View) logo, "transitionLogo");

                            ActivityOptionsCompat options = null;

                            if(navigationBar!=null){

                                options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this,p1,p2,p3);
                            }else{
                                options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this,p1,p3);
                            }
                            Utils.startActivityWithSharedOptions(SplashActivity.this,LoginActivity.class, options);
                        }else{
                            Utils.startActivity(SplashActivity.this,LoginActivity.class);
                        }
                        finish();
                    }
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        boolean connection = Utils.isNetworkAvailable(SplashActivity.this);
        if(requestCode == Utils.REQUEST_CONNECTION){
            if(connection){
                Utils.startActivity(SplashActivity.this,LoginActivity.class);
                finish();
            }else{
                Utils.showAlertDialogSettings(SplashActivity.this,getString(R.string.connectionError),getString(R.string.checkConnectionToLogin), true);
            }
        }
    }
}
