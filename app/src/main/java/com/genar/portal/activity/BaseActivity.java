package com.genar.portal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.genar.portal.R;
import com.genar.portal.activity.UserDetail.UserDetailActivity;
import com.genar.portal.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/saved_images/"+User.getCurrentUser().getUserName();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath, options);

        MenuItem menuItem = menu.findItem(R.id.menuUserDetail);
        ConstraintLayout v = (ConstraintLayout) menuItem.getActionView();
        final CircleImageView c = (CircleImageView)v.findViewById(R.id.menuItem);
        c.setImageBitmap(imageBitmap);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    View statusBar = findViewById(android.R.id.statusBarBackground);
                    View navigationBar = findViewById(android.R.id.navigationBarBackground);
                    String transitionName = "imageTransName";

                    Pair<View,String> p1= Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
                    Pair<View,String> p2= Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
                    Pair<View,String> p3= Pair.create((View)c, transitionName);

                    ActivityOptionsCompat actOptions = null;

                    if(navigationBar!=null){
                        actOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(BaseActivity.this,p1,p2,p3);
                    }else{
                        actOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(BaseActivity.this,p1,p3);
                    }

                    ActivityCompat.startActivity(BaseActivity.this, new Intent(BaseActivity.this, UserDetailActivity.class), actOptions.toBundle());
                }
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.menu_main,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuUserDetail:
                View v = item.getActionView();
                break;
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        this.finish();
    }

}
