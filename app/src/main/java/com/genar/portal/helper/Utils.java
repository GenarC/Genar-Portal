package com.genar.portal.helper;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;

import com.genar.portal.R;

import java.io.ByteArrayOutputStream;

public class Utils {
    private static final int THUMBSIZE = 196;
    public static final String DB_notf = "Duyurular";
    public static final String DB_user = "users";
    public static final String DB_app = "applications";

    public static final int REQUEST_CONNECTION = 0;

    public static void startActivity(Activity activity, Class c){
        activity.startActivity(new Intent(activity,c));
        activity.finish();
    }
    public static void startActivityWithSharedOptions(Activity activity, Class c, ActivityOptionsCompat options){
        activity.startActivity(new Intent(activity,c), options.toBundle());
        activity.finish();
    }

    public static void startActivityWithoutFinish(Activity activity, Class c){
        activity.startActivity(new Intent(activity,c));
    }

    public static boolean isNetworkAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static void showAlertDialogSettings(final Activity activity, String title, String message, final boolean activityFinish) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(activity);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.wifi, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        activity.startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), REQUEST_CONNECTION);

                    }
                })
                .setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(activityFinish){
                            activity.finish();
                        }else{
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton(R.string.mobilveri, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//                        activity.startActivityForResult(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS), REQUEST_CONNECTION);
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                        activity.startActivityForResult(intent, REQUEST_CONNECTION);

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static Bitmap UriToBitmap(Uri uri){
        String path = uri.getPath();
        Bitmap resized = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), Utils.THUMBSIZE, Utils.THUMBSIZE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 75, baos);

        return resized;
    }
}
