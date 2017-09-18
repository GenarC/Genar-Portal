package com.genar.portal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.genar.portal.R;
import com.genar.portal.helper.Utils;
import com.genar.portal.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.ibtn_employee)
    ImageView imgvEmployees;

    @BindView(R.id.ibtn_application)
    ImageView imgvApplications;

    @BindView(R.id.ibtn_notifications)
    ImageView imgvNotifications;

    @BindView(R.id.ibtn_technews)
    ImageView imgvTechNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        if(!User.currentUserHasToken()){

            if(User.getCurrentUser() != null){
                DatabaseReference dbRef;
                String userID = User.getCurrentUser().getUserId();
                dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("token");
                String token = FirebaseInstanceId.getInstance().getToken();
                dbRef.setValue(token);
            }
        }
    }

    @OnClick({R.id.ibtn_employee, R.id.ibtn_application, R.id.ibtn_notifications, R.id.ibtn_technews})
    public void onItemClick(View v){
        switch(v.getId()){
            case R.id.ibtn_employee:
                Utils.startActivityWithoutFinish(this,EmployeeListActivity.class);
                break;
           /* case R.id.ibtn_application:
                Utils.startActivityWithoutFinish(this,ApplicationListActivity.class);
                break;*/
            case R.id.ibtn_notifications:
                Utils.startActivityWithoutFinish(this,NotificationListActivity.class);
                break;
            case R.id.ibtn_technews:
                Utils.startActivityWithoutFinish(this,TechNewsActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
