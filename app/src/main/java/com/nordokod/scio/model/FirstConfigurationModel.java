package com.nordokod.scio.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nordokod.scio.controller.FirstConfigurationController;
import com.nordokod.scio.entity.AppConstants;
import com.nordokod.scio.process.DownloadImageProcess;
import com.soundcloud.android.crop.Crop;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FirstConfigurationModel {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser currentUser;
    private Context currentContext;
    private Activity currentActivity;
    private FirstConfigurationController fcController;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public FirstConfigurationModel(FirstConfigurationController fcc,Activity activity,Context context){
        this.currentActivity=activity;
        this.currentContext=context;
        this.fcController=fcc;
        initMAuth();
    }
    public void initMAuth(){//conectar firebase
        mAuth=FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser=firebaseAuth.getCurrentUser();
            }
        };
        mAuth.addAuthStateListener(mAuthListner);
    }
    public void requestPhoto(){//obtener foto
        String photopath;
        try{

            currentUser=mAuth.getCurrentUser();
            photopath = currentUser.getPhotoUrl().toString();//obtener link de la foto
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();//obtenemos referencia a la bd

            if(photopath.contains(storageReference.toString())){//si el link de la foto es de firebase...

                StorageReference sRef = storage.getReferenceFromUrl(photopath);//obtenemos la referencia al link
                final File localFile = File.createTempFile("Images", "bmp");//archivo temporal
                sRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {//petición de descarga
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap userPhoto;
                        userPhoto = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        fcController.setDefaultUserPhoto(userPhoto);//se envía la foto al CircleView
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        fcController.setDefaultScioPhoto();//si algo sale mal se usa la foto por defecto
                    }
                });
            }else{//descargar de Google o Facebook
                for (UserInfo profile : currentUser.getProviderData()) {
                    // check if the provider id matches "facebook.com"
                    if (profile.getProviderId().equals("facebook.com")) {
                        String facebookUserId = profile.getUid();
                        photopath = "https://graph.facebook.com/" + facebookUserId + "/picture?type=medium";
                    }
                }
                DownloadImageProcess dip=new DownloadImageProcess();
                dip.setListener(new DownloadImageProcess.CustomListener() {
                    @Override
                    public void onCompleted(Bitmap photo) {
                        fcController.setDefaultUserPhoto(photo);
                    }

                    @Override
                    public void onCancelled() {
                        fcController.setDefaultScioPhoto();
                    }
                });
                dip.execute(photopath);
            }

        }catch (Exception e){
            fcController.setDefaultScioPhoto();
        }
    }
    public void photoFromStorage(){//método para elegir foto de los archivos del celular
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        currentActivity.startActivityForResult(intent, AppConstants.GALLERY_REQUEST_CODE);//lanzar selector
    }
    public void trimPhoto(Uri photo){//método para recortar foto
        try {
            currentUser=mAuth.getCurrentUser();
            String pictureFile = "userPhoto-" +currentUser.getUid()+".jpg";
            File storageDir = currentContext.getFilesDir();
            File file=new File(storageDir,pictureFile);    //se crea archivo local para almacenar foto del resultado
            if(file.exists()){
                file.delete();
            }
            Crop.of(photo,Uri.fromFile(file)).asSquare().withMaxSize(512,512) .start(currentActivity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void uploadPhoto(Uri photo){     //método para subir foto a firebase
        try{
            Bitmap compressedPhoto=compressPhoto(photo);

            currentUser=mAuth.getCurrentUser();
            String pictureFile = "userPhoto-" +currentUser.getUid()+".jpg";
            File storageDir = currentContext.getFilesDir();
            File file=new File(storageDir,pictureFile);
            if(file.exists()){
                file.delete();
            }

            FileOutputStream out = new FileOutputStream(file);
            compressedPhoto.compress(Bitmap.CompressFormat.PNG, 100, out);

            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

            StorageReference sRef = storageReference.child(AppConstants.CLOUD_USERS).child(AppConstants.CLOUD_PROFILE_PHOTO).child("userPhoto-"+currentUser.getUid());
            sRef.putFile(Uri.fromFile(file)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    currentUser=mAuth.getCurrentUser();
                    StorageReference sRef = storageReference.child(AppConstants.CLOUD_USERS).child(AppConstants.CLOUD_PROFILE_PHOTO).child("userPhoto-"+currentUser.getUid());
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse(sRef.toString()))
                            .build();
                    currentUser.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            fcController.uploadComplete();
                        }
                    });

                }
            })
             .addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    fcController.uploadCanceled();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("testeo",taskSnapshot.getBytesTransferred()+"/"+taskSnapshot.getTotalByteCount());
                }
            });
        }catch (Exception e){
            Log.d("testeo",e.getMessage().toString());
            fcController.uploadCanceled();
        }



    }
    private Bitmap compressPhoto(Uri photo){
        Bitmap bmapPhoto = null;
        String format= photo.toString().substring(photo.toString().lastIndexOf(".")).toLowerCase();
        Bitmap original= BitmapFactory.decodeFile(photo.getPath());
        int photoHeight=original.getHeight();
        int compressRate=(256*100)/photoHeight;
        compressRate=(compressRate>100)?100:compressRate;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if(format.equals(".jpg")||format.equals(".jpeg")){
            original.compress(Bitmap.CompressFormat.JPEG,compressRate,out);
        }else if (format.equals(".png")){
            original.compress(Bitmap.CompressFormat.PNG,compressRate,out);
        }
        bmapPhoto=BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        return bmapPhoto;
    }


}