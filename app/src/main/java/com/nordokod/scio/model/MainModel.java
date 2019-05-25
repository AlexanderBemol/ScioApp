package com.nordokod.scio.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nordokod.scio.controller.MainController;
import com.nordokod.scio.process.DownloadImageProcess;
import com.nordokod.scio.view.MainActivity;

import java.io.File;

public class MainModel {
    private MainController mainController;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser currentUser;
    private Context currentContext;
    private MainActivity mainActivity;
    private Activity currentActivity;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public MainModel(MainController mc, MainActivity mActivity,Context context){
        this.mainController=mc;
        this.mainActivity=mActivity;
        initMAuth();
        this.currentContext=context;
    }
    public void initMAuth(){
        mAuth=FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser=firebaseAuth.getCurrentUser();
            }
        };
        mAuth.addAuthStateListener(mAuthListner);
    }

    /**
     * Obtener Foto del usuario, preferente del almacenamiento local
     */
    public void requestPhoto(){
        currentUser=mAuth.getCurrentUser();
        String pictureFile = "userPhoto-" +currentUser.getUid()+".jpg";
        File storageDir = currentContext.getFilesDir();
        File file=new File(storageDir,pictureFile);
        if(file.exists()){
            Bitmap localPhoto= BitmapFactory.decodeFile(file.getPath());
            mainController.setDefaultUserPhoto(localPhoto);
        }else{
            try{
                String photopath;
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
                            mainController.setDefaultUserPhoto(userPhoto);//se envía la foto al CircleView
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mainController.setDefaultScioPhoto();//si algo sale mal se usa la foto por defecto
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
                            mainController.setDefaultUserPhoto(photo);
                        }

                        @Override
                        public void onCancelled() {
                            mainController.setDefaultScioPhoto();
                        }
                    });
                    dip.execute(photopath);
                }

            }catch (Exception e){
                mainController.setDefaultScioPhoto();
            }
        }


    }

    public void logOut() {
        mAuth.signOut();
    }
    public String getName(){
        currentUser=mAuth.getCurrentUser();
        return currentUser.getDisplayName();
    }
}

