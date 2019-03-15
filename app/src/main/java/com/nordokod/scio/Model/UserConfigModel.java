package com.nordokod.scio.Model;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nordokod.scio.Controller.UserConfigController;
import com.nordokod.scio.Entidad.User;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UserConfigModel {
    private Context currentContext;
    private Activity currentActivity;
    private UserConfigController userConfCon;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    public UserConfigModel(UserConfigController userConfCon, Activity act, Context cont) {
        this.userConfCon = userConfCon;
        this.currentActivity = act;
        this.currentContext = cont;
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
        this.storage= FirebaseStorage.getInstance();
    }

    public void setBasicInfo(User user) {//nombreUsuario
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getUsername())
                .build();
        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //correcto este pedo
                        }
                    }
                });
    }

    public void setExtraInfo(User user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put(user.fbsBirthDay, user.getBirthDay());
        userData.put(user.fbsEducationalLevel, user.getEducationalLevel());
        db.collection(user.fbsUsersCollection).document(currentUser.getUid()).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //simonky
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //algo salió mal
                    }
                });
    }
    public void uploadPhoto(Uri file){ //subimos foto al storage
       storageRef = storage.getReference().child("Users").child(currentUser.getUid());
       UploadTask upTask= storageRef.putFile(file);
       upTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //no se pudo men
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri fbsPhoto=storageRef.getDownloadUrl().getResult();
                savePhotoPath(fbsPhoto);
            }
        });
    }
    public void savePhotoPath(Uri photoPath){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(photoPath)
                .build();
        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //se guardó la ruta en el user
                        }
                    }
                });
    }
}