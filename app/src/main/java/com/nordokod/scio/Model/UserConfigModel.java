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
import com.nordokod.scio.Controller.UserConfigController;
import com.nordokod.scio.Entidad.User;

import java.util.HashMap;
import java.util.Map;

public class UserConfigModel {
    private Context currentContext;
    private Activity currentActivity;
    private UserConfigController userConfCon;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    public UserConfigModel(UserConfigController userConfCon,Activity act,Context cont){
        this.userConfCon=userConfCon;
        this.currentActivity=act;
        this.currentContext=cont;
        this.mAuth=FirebaseAuth.getInstance();
        this.currentUser=mAuth.getCurrentUser();
    }
    public void setBasicInfo(User user){//nombreUsuario y fotito
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getUsername())
                .setPhotoUri(Uri.parse(user.getPhotoPath()))
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
    public void setExtraInfo(User user){
        Map<String, Object> userData = new HashMap<>();
        userData.put(user.fbsUID,currentUser.getUid());
        userData.put(user.fbsBirthDay,user.getBirthDay());
        userData.put(user.fbsEducationalLevel,user.getEducationalLevel());
        db.collection(user.fbsUsersCollection).add(userData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //se guardó
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //algo mal quizá nacer
                    }
                });
    }

}
