package com.nordokod.scio.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nordokod.scio.R;
import com.nordokod.scio.controller.MainController;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.model.User;
import com.nordokod.scio.process.DownloadImageProcess;
import com.nordokod.scio.process.MediaProcess;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImportGuideDialog implements BasicDialog {

    private Context context;
    private Dialog dialog;

    private AppCompatImageView IV_Close;
    private AppCompatTextView TV_User, TV_Topic, TV_Category, TV_Month, TV_Day, TV_Time, TV_Hour;
    private AppCompatButton BTN_Import;
    private CircleImageView CI_User_Photo;

    public ImportGuideDialog(Context context) {
        this.context = context;
        initDialog();
        initComponents();
        initListeners();
    }

    @Override
    public void initDialog() {
        dialog = new Dialog(context, R.style.DefaultTheme);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_import_guide);
    }

    @Override
    public void initComponents() {
        IV_Close        = dialog.findViewById(R.id.DIGuide_IV_Close);

        CI_User_Photo   = dialog.findViewById(R.id.DIGuide_CI_Photo);

        TV_User         = dialog.findViewById(R.id.DIGuide_TV_User);
        TV_Topic        = dialog.findViewById(R.id.DIGuide_TV_Topic);
        TV_Category     = dialog.findViewById(R.id.DIGuide_TV_Category);
        TV_Month        = dialog.findViewById(R.id.DIGuide_TV_Month);
        TV_Day          = dialog.findViewById(R.id.DIGuide_TV_Day);
        TV_Time         = dialog.findViewById(R.id.DIGuide_TV_Time);
        TV_Hour         = dialog.findViewById(R.id.DIGuide_TV_Hour);

        BTN_Import      = dialog.findViewById(R.id.DIguide_BTN_Import);
    }

    @Override
    public void initListeners() {
        IV_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        BTN_Import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Mandar a llamar al método del controlador para que haga la importación.
            }
        });
    }

    @Override
    public void showDialog() {

    }

    void showDialog(Guide guide) {
        if (!dialog.isShowing()) {
            User userModel = new User();
            final com.nordokod.scio.entity.User userEntity = new com.nordokod.scio.entity.User();
            userEntity.setUid(guide.getUID());
            userModel.getUserInformation(userEntity).addOnSuccessListener(documentSnapshot -> {
                com.nordokod.scio.entity.User user = userModel.getUserFromDocument(documentSnapshot);
                TV_User.setText(user.getUsername());
                switch (userModel.getProfilePhotoHost(user)){
                    case GOOGLE_OR_FACEBOOK_STORAGE:
                        userModel.getExternalProfilePhoto(new DownloadImageProcess.CustomListener() {
                            @Override
                            public void onCompleted(Bitmap photo) {
                                CI_User_Photo.setImageBitmap(photo);
                            }

                            @Override
                            public void onError(Exception e) {
                                CI_User_Photo.setImageResource(R.drawable.default_photo);
                            }
                        }, user);
                        break;
                    case FIREBASE_STORAGE:
                        userModel.getFirebaseProfilePhoto(user).addOnSuccessListener(bytes -> CI_User_Photo.setImageBitmap(new MediaProcess().createBitmapWithBytes(bytes)));
                        break;
                }
            });


            TV_Category.setText(getCategoryResId(guide.getCategory()));
            TV_Topic.setText(guide.getTopic());

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <Build.VERSION_CODES.O){
                Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_TOAST);
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            }else{
                Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_PHONE);
            }

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            layoutParams.copyFrom(window.getAttributes());

            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

            window.setAttributes(layoutParams);


            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    private int getCategoryResId(int category) {
        switch (category) {
            case 1: return R.string.category_exact_sciences;
            case 2: return R.string.category_social_sciences;
            case 3: return R.string.category_sports;
            case 4: return R.string.category_art;
            case 5: return R.string.category_tech;
            case 6: return R.string.category_entertainment;
            default: return R.string.category_others;
        }
    }
}
