package com.genar.portal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class GetBitmapTask implements Runnable {

    private final String uri;
    private final Callback callback;

    public GetBitmapTask(String uri, Callback callback) {
        this.uri = uri;
        this.callback = callback;
    }

    @Override public void run() {
        try {
            URL url = new URL(uri);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            callback.onFinish(bmp);
        } catch (IOException e) {
            callback.onError(e);
        }
    }

    public interface Callback{
        void onFinish(Bitmap bitmap);
        void onError(Throwable t);
    }
    public static void SaveImage(Bitmap finalBitmap, String fileName) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root+ "/saved_images/");
        myDir.mkdirs();

        File file = new File (myDir, fileName);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
