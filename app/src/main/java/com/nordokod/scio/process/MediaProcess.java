package com.nordokod.scio.process;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MediaProcess {
    /**
     * Método para comprimir foto
     * @param photo uri de la foto
     * @return bitmap de la foto comprimida
     */
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

    /**
     * Método para convertir arreglo de bytes a bitmap
     * @param bytes bytes de la foto
     * @return bitmap de la foto
     */
    public Bitmap createBitmapWithBytes(byte[] bytes){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }
}
