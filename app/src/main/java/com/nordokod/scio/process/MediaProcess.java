package com.nordokod.scio.process;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MediaProcess {
    public Bitmap compressPhoto(Uri photo){
        Bitmap compresedPhoto;
        String format= photo.toString().substring(photo.toString().lastIndexOf(".")).toLowerCase();
        Bitmap original= BitmapFactory.decodeFile(photo.getPath());
        int photoHeight=original.getHeight();
        int compressRate=(256*100)/photoHeight;
        compressRate=(compressRate>100)?100:compressRate;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if(format.equals(".jpg")||format.equals(".jpeg")){
            original.compress(android.graphics.Bitmap.CompressFormat.JPEG,compressRate,out);
        }else if (format.equals(".png")){
            original.compress(android.graphics.Bitmap.CompressFormat.PNG,compressRate,out);
        }
        compresedPhoto=BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        return compresedPhoto;
    }
}
