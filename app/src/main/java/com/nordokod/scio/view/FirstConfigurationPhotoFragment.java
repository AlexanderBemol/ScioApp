package com.nordokod.scio.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.R;
import com.nordokod.scio.constants.RequestCode;
import com.nordokod.scio.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirstConfigurationPhotoFragment extends Fragment implements BasicFragment {

    private AppCompatImageView IV_Camera;
    private CircleImageView CIV_Photo;
    private User userModel;
    private FirstConfigurationActivity firstConfigurationActivity;
    /**
     * Obejeto del controlador perteneciente a esta Activity.
     */
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
        CIV_Photo.setImageResource(R.drawable.default_photo);
        userModel=new User();
        //setDefaultPhoto();

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

    public void configFragment(FirstConfigurationActivity firstConfigurationActivity){
        this.firstConfigurationActivity=firstConfigurationActivity;
    }

    /**
     * Obtener foto por defecto del usuario
     */
    /*protected void setDefaultPhoto() {
        try{
            switch (userModel.getProfilePhotoHost()){
                case FIREBASE_STORAGE:
                    userModel.getFirebaseProfilePhoto().addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap photo = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            try {
                                userModel.saveProfilePhotoInLocal(firstConfigurationActivity.getApplicationContext(),photo);
                            } catch (Exception e) {
                                firstConfigurationActivity.showError(e);
                            }
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            firstConfigurationActivity.showError(new OperationCanceledException());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            firstConfigurationActivity.showError(e);
                        }
                    });
                    break;
                case GOOGLE_OR_FACEBOOK_STORAGE:
                    userModel.getExternalProfilePhoto(new DownloadImageProcess.CustomListener() {
                        @Override
                        public void onCompleted(Bitmap photo) {
                            try {
                                userModel.saveProfilePhotoInLocal(firstConfigurationActivity.getApplicationContext(),photo);
                            } catch (Exception e) {
                                firstConfigurationActivity.showError(e);
                            }
                            setPhoto(photo);
                        }

                        @Override
                        public void onError(Exception e) {
                            firstConfigurationActivity.showError(e);
                        }
                    });
                    break;
            }
        }catch (Exception e){
            firstConfigurationActivity.showError(e);
        }

    }*/

    /**
     * Método que define la foto elegida por el usuario.
     */
    private void newPhoto() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        firstConfigurationActivity.startActivityForResult(intent, RequestCode.RC_GALLERY.getCode());
    }

    /**
     * Método para mostrar la foto elegida por el usuario y que ya está guardada en Firebase.
     */
    protected void setPhoto(Bitmap photo) {
        if (photo != null)
            CIV_Photo.setImageBitmap(photo);
        else
            CIV_Photo.setImageResource(R.drawable.default_photo);
    }

}
