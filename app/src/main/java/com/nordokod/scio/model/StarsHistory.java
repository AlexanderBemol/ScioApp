package com.nordokod.scio.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.nordokod.scio.entity.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StarsHistory {
    public static final String TABLE_NAME ="Stars_History";
    public static final String ID ="Id";
    public static final String GUIDE ="Guide";
    public static final String QUESTION ="Question";
    public static final String STARS ="Stars";
    public static final String TIMESTAMP ="TimeStamp";
    public static final String SYNC ="Sync";
    public static final String MODE ="Mode";

    private DBAux dbAux;
    private SQLiteDatabase sqLiteDatabase;
    private SimpleDateFormat dateFormat;
    private FirebaseAuth mAuth;

    public StarsHistory(Context context){
        dbAux = new DBAux(context);
        sqLiteDatabase = dbAux.getWritableDatabase();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mAuth = FirebaseAuth.getInstance();
    }

    public void createStarHistory(String idguide, int question, int stars, int mode){
        ContentValues cv = new ContentValues();
        cv.put(GUIDE,idguide);
        cv.put(QUESTION,question);
        cv.put(STARS,stars);
        cv.put(TIMESTAMP,dateFormat.format(new Date()));
        cv.put(SYNC,0);
        cv.put(MODE,mode);

        if(sqLiteDatabase!=null)
            sqLiteDatabase.insert(TABLE_NAME,null,cv);

    }

    public void updateSync(int id ){
        ContentValues cv = new ContentValues();
        String[] args = new String[]{String.valueOf(id)};
        sqLiteDatabase.update(TABLE_NAME, cv, ID+"=?", args);
    }

    /**
     * method to export all the local logs to the cloud
     */
    public void exportData(){
        String query = "SELECT * FROM "+StarsHistory.TABLE_NAME+" WHERE "+SYNC+"=0";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        if(c.getCount()>0){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            WriteBatch writeBatch = db.batch();

            while (c.moveToNext()){
                try {
                    String date = c.getString(c.getColumnIndex(TIMESTAMP));
                    DocumentReference doc = db.collection(User.KEY_USERS).document(mAuth.getUid()).collection(User.KEY_USERS_LOGS).document();
                    Map<String, Object> log = new HashMap<>();
                    log.put("GUIDE",c.getString(c.getColumnIndex(StarsHistory.GUIDE)));
                    log.put("QUESTION",c.getInt(c.getColumnIndex(StarsHistory.QUESTION)));
                    log.put("STARS",c.getInt(c.getColumnIndex(StarsHistory.STARS)));
                    log.put("MODE",c.getInt(c.getColumnIndex(StarsHistory.MODE)));
                    log.put("TIMESTAMP",dateFormat.parse(date));

                    writeBatch.set(doc,log);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //operation
            writeBatch.commit()
                    .addOnSuccessListener((aVoid)-> Log.d("testlogs","success"))
                    .addOnFailureListener(ex->Log.d("testlogs", Objects.requireNonNull(ex.getMessage())));

        }
    }



}
