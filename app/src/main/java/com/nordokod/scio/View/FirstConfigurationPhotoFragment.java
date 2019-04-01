package com.nordokod.scio.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirstConfigurationPhotoFragment extends Fragment implements BasicFragment {

    private AppCompatImageView IV_Camera;
    private CircleImageView CIV_Photo;

    /**
     * Obejeto del controlador perteneciente a esta Activity.
     */
    //private FirstConfigurationController firstConfigurationController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle s) {
        View view = inflater.inflate(R.layout.photo_first_configuration, container,false);
        initComponents(view);
        initListeners();

        return view;
    }

    @Override
    public void initComponents(View view) {
        IV_Camera = view.findViewById(R.id.imgCamera);
        CIV_Photo = view.findViewById(R.id.photo);
    }

    @Override
    public void initListeners() {
        IV_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPhoto();
            }
        });
    }
/*
    /**
     * Método para configurar este Fragment con el objeto del controlador usado por el Activity.
     * /
    protected void configFragment(FirstConfigurationController controller) {
        this.firstConfigurationController = controller;
    }
*/
    /**
     * Método que define la foto por defecto.
     */
    protected void skip() {
        CIV_Photo.setImageResource(R.drawable.default_photo);
    }

    /**
     * Método que define la foto elegida por el usuario.
     */
    private void newPhoto() {
        //firstConfigurationController.newPhoto();
    }

    /**
     * Método para mostrar la foto elegida por el usuario y que ya está guardada en Firebase.
     */
    protected void setPhoto(Bitmap photo) {
        if (photo != null)
            CIV_Photo.setImageBitmap(photo);
    }
}
