package com.genar.portal.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.genar.portal.R;
import com.genar.portal.adapter.EmployeeAdapter;
import com.genar.portal.helper.Utils;
import com.genar.portal.model.User;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeListActivity extends BaseActivity {

    @BindView(R.id.v_recyclerview)
    RecyclerView rv_View;

    ValueEventListener listener;


    public void initialize(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dRef = database.getReference(Utils.DB_user);
        dRef.keepSynced(true);

        final boolean[] firstInitialize = {true};

        listener = dRef.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<User> uList = new ArrayList<>();

                for (DataSnapshot snap: dataSnapshot.getChildren()) {

                    HashMap<String,String> hm = (HashMap<String,String>) snap.getValue();
                    User u = new User();

                    u.setName(hm.get("name"));
                    u.setPhoneNumber(hm.get("phoneNumber"));
                    u.setUserName(hm.get("userName"));
                    u.setStatus(hm.get("status"));
                    u.setProfileImage(hm.get("profileImage"));
                    u.setSkypeUsername(hm.get("skypeUsername"));

                    uList.add(u);
                }

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EmployeeListActivity.this);
                rv_View.setLayoutManager(layoutManager);
                EmployeeAdapter employeeAdapter = new EmployeeAdapter(uList,EmployeeListActivity.this, getSupportFragmentManager());
                rv_View.setAdapter(employeeAdapter);

                if(!firstInitialize[0]){
                    Toast.makeText(EmployeeListActivity.this, "Çalışanlar listesi güncellendi!", Toast.LENGTH_LONG).show();
                }else{
                    firstInitialize[0] = false;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity_employee);

        ButterKnife.bind(this);

        initialize();
    }

    @Override
    public void onBackPressed() {
        FirebaseDatabase.getInstance().getReference(Utils.DB_user).removeEventListener(listener);
        super.onBackPressed();
    }
}
