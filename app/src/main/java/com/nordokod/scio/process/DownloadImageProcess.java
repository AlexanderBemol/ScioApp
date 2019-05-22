package com.nordokod.scio.process;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;

public class DownloadImageProcess extends AsyncTask<String, Void, Bitmap> {
    CustomListener listener;
    public interface CustomListener {
        void onCompleted(Bitmap photo);
        void onCancelled();
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
            listener.onCancelled();
        }
        return bmp;
    }

    protected void onPostExecute(Bitmap result) {
        listener.onCompleted(result);
    }

    @Override
    protected void onCancelled() {
        listener.onCancelled();
    }


}
