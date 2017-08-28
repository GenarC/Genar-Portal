package com.genar.portal.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.klinker.android.peekview.PeekViewActivity;
import com.genar.portal.R;
import com.genar.portal.adapter.NotificationAdapter;
import com.genar.portal.helper.Utils;
import com.genar.portal.model.Notification;
import com.genar.portal.model.User;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationListActivity extends PeekViewActivity {

    @BindView(R.id.notf_recyclerview)
    RecyclerView rvView;
    ValueEventListener listener;

    private String role;

    private void initialize() {
        role = User.getCurrentUser().getRole();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dRef = database.getReference(Utils.DB_notf);
        dRef.keepSynced(true);

        final boolean[] firstInitialize = {true};

        listener = dRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Notification> nList = new ArrayList<>();


                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    HashMap<String,String> hm = (HashMap<String,String>) snap.getValue();

                    if(hm!=null){
                        Notification n = new Notification();

                        for (String key1: hm.keySet()) {
                            switch (key1) {
                                case "name" :  n.setName(hm.get(key1));
                                    break;
                                case "post":  n.setPost(hm.get(key1));
                                    break;
                                case "date":  n.setDate(hm.get(key1));
                                    break;
                                case "postId":  n.setPostId(hm.get(key1));
                                    break;
                                case "title":  n.setTitle(hm.get(key1));
                                    break;
                                case "userId":  n.setUserId(hm.get(key1));
                                    break;
                                case "userImageLink":  n.setUserImageLink(hm.get(key1));
                                    break;
                            }
                        }
                        nList.add(n);
                    }
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NotificationListActivity.this);
                rvView.setLayoutManager(layoutManager);
                NotificationAdapter notificationAdapter = new NotificationAdapter (role,nList,NotificationListActivity.this);
                rvView.setAdapter(notificationAdapter);

                if(!firstInitialize[0]){
                    Toast.makeText(NotificationListActivity.this, "Duyurular listesi güncellendi!", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.list_activity_notification);

        ButterKnife.bind(this);

        initialize();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        if(role.equals("admin")){
            MenuInflater inf = getMenuInflater();
            inf.inflate(R.menu.menu_notifications,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuNotificationAdd:
                Utils.startActivityWithoutFinish(this,NotificationAddActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FirebaseDatabase.getInstance().getReference(Utils.DB_notf).removeEventListener(listener);
        super.onBackPressed();
    }
}
