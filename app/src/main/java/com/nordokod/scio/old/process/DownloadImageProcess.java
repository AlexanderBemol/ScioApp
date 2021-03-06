package com.nordokod.scio.old.process;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.util.concurrent.CancellationException;

public class DownloadImageProcess extends AsyncTask<String, Void, Bitmap> {
    CustomListener listener;
    public interface CustomListener {
        void onCompleted(Bitmap photo);
        void onError(Exception e);
    }
    public DownloadImageProcess(){
        this.listener=null;
    }
    public void setListener(CustomListener listener) {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bmp = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inSampleSize = 1;
            options.inScaled = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inPreferQualityOverSpeed=true;
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            listener.onError(e);
        }
        return bmp;
    }

    protected void onPostExecute(Bitmap result) {
        listener.onCompleted(result);
    }

    @Override
    protected void onCancelled() {
        listener.onError(new CancellationException());
    }


}
