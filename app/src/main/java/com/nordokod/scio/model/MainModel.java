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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nordokod.scio.data.GuideData;
import com.nordokod.scio.controller.MainController;
import com.nordokod.scio.entity.AppConstants;
import com.nordokod.scio.entity.Error;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.process.DownloadImageProcess;
import com.nordokod.scio.process.NetworkUtils;
import com.nordokod.scio.view.MainActivity;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
    private ArrayList<Guide> listOfGuides;
    private GuideData guideData;

    public MainModel(MainController mc, MainActivity mActivity,Context context){
        this.mainController=mc;
        this.mainActivity=mActivity;
        this.currentContext=context;
        initMAuth();
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
        if (currentUser != null) {
            return currentUser.getDisplayName();
        }else {
            return  "User";
        }
    }

    public void createGuide(Guide guide) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        currentUser=mAuth.getCurrentUser();
        guideData= new GuideData(db,currentUser,currentContext);
        guideData.setCustomListener(new GuideData.customListener() {
            @Override
            public void onSuccesUpload() {
                mainController.succesUpload();
            }

            @Override
            public void onSucces() {
                mainController.succesUpload();
            }

            @Override
            public void onFailure(String errorMessage) {
                mainController.showError(new Error(Error.WHEN_SAVING_ON_DATABASE));
            }

            @Override
            public void onlineLoadSucces(ArrayList<Guide> guides) {

            }
        });
        guide.setUID(currentUser.getUid());
        guide.setIs_actived(true);
        NetworkUtils networkUtils=new NetworkUtils();
        guide.setOnline(false);
        if(networkUtils.isNetworkConnected(currentContext)){
            guideData.saveGuideOnline(guide);
        }
            guideData.saveGuideOffline(guide);
    }

    public void loadGuides(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        currentUser=mAuth.getCurrentUser();
        guideData= new GuideData(db,currentUser,currentContext);
        guideData.setCustomListener(new GuideData.customListener() {
            @Override
            public void onSuccesUpload() {
            }

            @Override
            public void onSucces() {
            }

            @Override
            public void onFailure(String errorMessage) {
            }

            @Override
            public void onlineLoadSucces(ArrayList<Guide> guides) {
                listOfGuides=guides;
            }
        });
    }
    public ArrayList<Guide> getListOfGuides() {
        return listOfGuides;
    }

    public void onSaveMultipleChoiceQuestion(Guide guide,
                                             String question, String option_1, String option_2, String option_3, String option_4,
                                             boolean is_correct_1, boolean is_correct_2, boolean is_correct_3, boolean is_correct_4) {
        // TODO: Logica para guardar la pregunta.
    }

    public void onSaveTrueFalseQuestion(Guide guide, String question, boolean answer) {
        // TODO: Logica para guardar la pregunta.
    }

    public void onSaveOpenAnswerQuestion(Guide guide, String question, String answer) {
        // TODO: Logica para guardar la pregunta.
    }
}

