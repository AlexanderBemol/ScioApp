package com.nordokod.scio.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.nordokod.scio.entity.App;
import com.nordokod.scio.entity.AppConstants;
import com.nordokod.scio.entity.Error;
import com.nordokod.scio.process.DownloadImageProcess;
import com.nordokod.scio.process.SystemWriteProcess;
import com.soundcloud.android.crop.Crop;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FirstConfigurationModel {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser currentUser;
    private Context currentContext;
    private Activity currentActivity;
    private FirstConfigurationController fcController;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private int lockedApps;

    public FirstConfigurationModel(FirstConfigurationController fcc,Activity activity,Context context){
        this.currentActivity=activity;
        this.currentContext=context;
        this.fcController=fcc;
        lockedApps=0;
        initMAuth();
    }
    private void initMAuth(){//conectar firebase
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

    /**
     * método para recortar foto
     * @param photo Uri de foto
     */
    public void trimPhoto(Uri photo){
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

    /**
     * Método para subir foto a firebase
     * @param photo a subir
     */
    public void uploadPhoto(Uri photo){
        try{
            Bitmap compressedPhoto=compressPhoto(photo);//primero se comprime
            /*
             * se crea un archivo en la memoria interna que alojará la foto comprimida
             * se hace referencia al archivo que se llamará userPhoto- concatenada al UID (id del user)
             */
            currentUser=mAuth.getCurrentUser();
            String pictureFile = "userPhoto-" +currentUser.getUid()+".jpg";
            File storageDir = currentContext.getFilesDir();
            File file=new File(storageDir,pictureFile);
            if(file.exists()){
                file.delete();
            }

            FileOutputStream out = new FileOutputStream(file);
            compressedPhoto.compress(Bitmap.CompressFormat.PNG, 100, out);

            /*
             * Referencia al almacenamiento de firebase
             */
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            /*
             * se crea referencia a la carpeta que aloja las fotos de los usarios y se solicita subir archivo
             */
            StorageReference sRef = storageReference.child(AppConstants.CLOUD_USERS).child(AppConstants.CLOUD_PROFILE_PHOTO).child("userPhoto-"+currentUser.getUid());
            sRef.putFile(Uri.fromFile(file)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {//listener de éxito

                    /*
                     * se obtiene link de la foto
                     */
                    currentUser=mAuth.getCurrentUser();
                    StorageReference sRef = storageReference.child(AppConstants.CLOUD_USERS).child(AppConstants.CLOUD_PROFILE_PHOTO).child("userPhoto-"+currentUser.getUid());
                    /*
                     * se actualiza link de foto del usario
                     */
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse(sRef.toString()))
                            .build();
                    /*
                     * listener para saber cuandó se actalizó el link correctamente
                     */
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
                public void onCanceled() {//se canceló la subida
                    fcController.uploadCanceled();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {//listener de progreso
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("testeo",taskSnapshot.getBytesTransferred()+"/"+taskSnapshot.getTotalByteCount());
                }
            });
        }catch (Exception e){//algo salió mal
            Log.d("testeo",e.getMessage());
            fcController.uploadCanceled();
        }



    }

    /**
     * método para comprimir foto, reducir calidad
     * @param photo original
     * @return comprimida
     */
    private Bitmap compressPhoto(Uri photo){
        Bitmap bmapPhoto;
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

    /**
     * Obtener el nombre del usario de firebase
     * @return string con nombre
     */
    public String getName() {
        currentUser=mAuth.getCurrentUser();
        String name="";
        if(currentUser!=null){
            name=currentUser.getDisplayName();
        }
        return name;
    }

    /**
     * obtener lista de apps del celular
     * @return ArrayList de apps
     */
    public ArrayList<App> getListOfApps() {
        SystemWriteProcess swp=new SystemWriteProcess(currentContext);
        return swp.getAllApps();
    }

    /**
     * Revisar si se seleccionaron más de 3 apps
     * @param packagePath package de app
     * @param isChecked si el nuevo estado es activado o no
     * @return el nuevo estado luego de revisar..
     */
    public boolean onStateChanged(String packagePath, boolean isChecked) {
        boolean b;
        if(!isChecked){
            lockedApps--;
            b=false;
        }else{
            if(lockedApps<3){
                lockedApps++;
                b=true;
            }else{
                b=false;
                Error error=new Error();
                error.setType(Error.MAXIMUM_NUMBER_OF_APPS_REACHED);
                fcController.showError(error);
            }
        }
        return b;
    }

    /**
     * validar campos
     * @param name String nombre
     * @param birthday String fecha nacimiento
     * @param education int nivel educación
     * @return valid
     */
    public boolean validateFields(String name, String birthday, Integer education) {
        /*
        * la expresion regular regEx sólo permite escribir nombres no vacíos, compuestos por caracteres no especiales, no permite usar más de un espacio seguido
        **/
        String regEx="([A-Za-zÁÉÍÓÚáéíóúÄËÏÖÜäëïöüÑñ]+)([\\s]([A-Za-zÁÉÍÓÚáéíóúÄËÏÖÜäëïöüÑñ])+)*";
        if(!name.matches(regEx)||(name.length()<5||name.length()>30)){
            Error error=new Error();
            error.setType(Error.EMPTY_FIELD);
            fcController.showError(error);
            return false;
        }
        try{
            /*
             * Validar que la fecha sea anterior a hoy
             */
            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date date=sdf.parse(birthday);
            Date dateToday = new Date();
            if(date.after(dateToday)){
                Error error=new Error();
                error.setType(Error.EMPTY_FIELD);
                fcController.showError(error);
                return false;
            }
            /*
             * Revisar si se seleccionó un nivel de estudios
             */
            if (education==0){
                Error error=new Error();
                error.setType(Error.INVALID_OPTION);
                fcController.showError(error);
                return false;
            }

        }
        catch (ParseException e){
            /*
             * esta excepción es por error al convertir fecha, signifa que el usaurio no seleccionó una fecha
             */
            Error error=new Error();
            error.setType(Error.INVALID_OPTION);
            fcController.showError(error);
            return false;
        }
        catch (Exception e){
            Log.d("testeo",e.getMessage());

        }
        return true;
    }
}