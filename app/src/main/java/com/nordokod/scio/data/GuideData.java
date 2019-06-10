package com.nordokod.scio.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nordokod.scio.entity.AppConstants;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.process.SystemWriteProcess;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GuideData{
    public interface customListener{
        void onSuccesUpload();
        void onSucces();
        void onFailure(String errorMessage);
        void onlineLoadSucces(ArrayList<Guide> guides);
    }
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private Context currentContext;
    private customListener customListener;
    public GuideData (FirebaseFirestore fs,FirebaseUser fu,Context context){
        this.currentContext=context;
        this.db=fs;
        this.currentUser=fu;
    }
    public void setCustomListener(GuideData.customListener customListener) {
        this.customListener = customListener;
    }
    public void saveGuideOnline(Guide guide){
        DocumentReference categoryReference=db.collection(AppConstants.CLOUD_CATEGORYS).document(String.valueOf(guide.getCategory()));
        Map<String, Object> data = new HashMap<>();
        data.put(AppConstants.CLOUD_GUIDES_CATEGORY,categoryReference);
        data.put(AppConstants.CLOUD_GUIDES_TOPIC,guide.getTopic());
        data.put(AppConstants.CLOUD_GUIDES_DATETIME,guide.getDatetime());
        db.collection(AppConstants.CLOUD_GUIDES).document(currentUser.getUid()).collection(AppConstants.CLOUD_GUIDES).add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        customListener.onSuccesUpload();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customListener.onFailure(e.getMessage());
                        Log.d("testeo",e.getMessage());
                    }
                });
    }
    public void saveGuideOffline(Guide guide){
        try{
            JSONObject JSONGuide=new JSONObject();
            JSONGuide.put(Guide.KEY_ID,guide.getId());
            JSONGuide.put(Guide.KEY_CATEGORY,guide.getCategory());
            JSONGuide.put(Guide.KEY_TOPIC,guide.getTopic());
            JSONGuide.put(Guide.KEY_DATETIME,guide.getDatetime());
            JSONGuide.put(Guide.KEY_UID,guide.getUID());
            JSONGuide.put(Guide.KEY_ACTIVATED,guide.isIs_actived());
            JSONGuide.put(Guide.KEY_ONLINE,guide.isOnline());
            SystemWriteProcess swp=new SystemWriteProcess(currentContext);
            boolean b= swp.saveJSON(JSONGuide,"Guides/",guide.getId());
            if(b)
                customListener.onSucces();
            else
                customListener.onFailure("Error");
        }catch (Exception e){
            customListener.onFailure(e.getMessage());
            Log.d("testeo",e.getMessage());
        }
    }
    public void loadOnlineGuides(){
        CollectionReference userGuides=db.collection(AppConstants.CLOUD_GUIDES).document(currentUser.getUid()).collection(AppConstants.CLOUD_GUIDES);
        userGuides.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(!task.getResult().isEmpty()){
                                ArrayList<Guide> listOfGuides=new ArrayList<>();
                                for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                    Guide guide=new Guide();
                                    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH);
                                    Timestamp ts= (Timestamp) documentSnapshot.getData().get(AppConstants.CLOUD_GUIDES_DATETIME);
                                    Date date=ts.toDate();
                                    String dateS=sdf.format(date);
                                    guide.setDate(dateS);
                                    guide.setTopic((String)documentSnapshot.getData().get(AppConstants.CLOUD_GUIDES_TOPIC));
                                    DocumentReference docRef =(DocumentReference) documentSnapshot.getData().get(AppConstants.CLOUD_GUIDES_CATEGORY);
                                    String category= String.valueOf(docRef.getPath().charAt(docRef.getPath().length()-1));
                                    guide.setCategory(Integer.parseInt(category));
                                    guide.setId(documentSnapshot.getReference().toString());
                                    listOfGuides.add(guide);
                                    customListener.onlineLoadSucces(listOfGuides);
                                }
                                Log.d("testeo","cargaLista");
                            }
                        }
                    }
                });
    }
}
