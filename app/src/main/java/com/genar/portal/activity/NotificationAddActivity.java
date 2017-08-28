package com.genar.portal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.genar.portal.R;
import com.genar.portal.helper.Utils;
import com.genar.portal.model.Notification;
import com.genar.portal.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationAddActivity extends AppCompatActivity {

    @BindView(R.id.notificationadd_btn_send)
    Button btnAddNotification;
    @BindView(R.id.notificationadd_title)
    EditText etTitle;
    @BindView(R.id.notificationadd_notfbody)
    EditText etBody;

    DatabaseReference fbDatabase;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_add);

        fbDatabase = FirebaseDatabase.getInstance().getReference(Utils.DB_notf);
        user = getIntent().getParcelableExtra("user");

        ButterKnife.bind(this);
    }

    @OnClick(R.id.notificationadd_btn_send)
    public void addNotification(){
        if(etTitle.getText().toString().equals("") || etBody.getText().toString().equals("")){
            Toast.makeText(NotificationAddActivity.this, R.string.notfAddMissingAreas, Toast.LENGTH_SHORT)
                    .show();
        }else{
            final Notification n = new Notification(etTitle.getText().toString(), etBody.getText().toString());

            final String autoId = fbDatabase.push().getKey();
            n.setPostId(autoId);

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference(Utils.DB_user);

            ref.child(n.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    n.setName(dataSnapshot.getValue(User.class).getName());
                    n.setUserImageLink(dataSnapshot.getValue(User.class).getProfileImage());

                    fbDatabase.child(autoId).setValue(n, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
//                                System.out.println("Data could not be saved " + databaseError.getMessage());
                            } else {
                                Toast.makeText(NotificationAddActivity.this,"'" + etTitle.getText().toString()+ "'" + getString(R.string.notfSendWithTitle),Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }
                    });
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("error","error");
                }
            });
        }
        /*Intent intent = new Intent(NotificationAddActivity.this, NotificationListActivity.class);
        startActivity(intent);*/
    }
}
