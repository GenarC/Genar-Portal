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
import com.genar.portal.adapter.ApplicationAdapter;
import com.genar.portal.helper.Utils;
import com.genar.portal.model.App;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplicationListActivity extends BaseActivity {

    @BindView(R.id.empllist_recyclerview)
    RecyclerView rvEmployeeListRecyclerView;

    ValueEventListener listener;

    public void initialize(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dRef = database.getReference(Utils.DB_app);
        dRef.keepSynced(true);

        final boolean[] firstInitialize = {true};

        listener = dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<App> appList = new ArrayList<>();

                HashMap<String, HashMap<String,String>> hm = (HashMap<String,HashMap<String,String>>) dataSnapshot.getValue();

                for (String key : hm.keySet()) {
                    App app = new App();

                    app.setName(hm.get(key).get("name"));
                    app.setFirmName(hm.get(key).get("firmName"));
                    app.setVersion(hm.get(key).get("version"));
                    app.setPicUrl(hm.get(key).get("picUrl"));
                    app.setAndDownloadUrl(hm.get(key).get("andDownloadUrl"));

                    appList.add(app);
                }

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ApplicationListActivity.this);
                rvEmployeeListRecyclerView.setLayoutManager(layoutManager);
                ApplicationAdapter appAdapter = new ApplicationAdapter (appList,ApplicationListActivity.this);
                rvEmployeeListRecyclerView.setAdapter(appAdapter);

                if(!firstInitialize[0]){
                    Toast.makeText(ApplicationListActivity.this, "Uygulamalar listesi güncellendi!", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.list_activity_application);

        ButterKnife.bind(this);

        initialize();
    }

    @Override
    public void onBackPressed() {
        FirebaseDatabase.getInstance().getReference(Utils.DB_app).removeEventListener(listener);

        super.finish();
    }
}
