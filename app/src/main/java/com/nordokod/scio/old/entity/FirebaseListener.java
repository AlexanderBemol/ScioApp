package com.nordokod.scio.old.entity;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public interface FirebaseListener {
    void OnSucces();
    void OnSucces(Object data);
    void OnSucces(ArrayList<Object> data);
    void OnCanceled();
    void OnCanceled(Exception exception);
    void OnFailure(Exception exception);
    void OnProgress(int percentage);

}
