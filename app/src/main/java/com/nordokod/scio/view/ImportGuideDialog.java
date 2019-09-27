package com.nordokod.scio.view;

import android.app.Dialog;
import android.content.Context;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ImportGuideDialog implements BasicDialog {

    private Context context;
    private Dialog dialog;
    private MainController mainController;

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

    public void showDialog(Guide guide) {
        if (!dialog.isShowing()) {

            //CI_User_Photo.setImageBitmap(guide.getUser_photo());
            //TV_User.setText(guide.getUser_Name());
            TV_Category.setText(getCategoryResId(guide.getCategory()));
            TV_Topic.setText(guide.getTopic());
            // TODO: Agregar para la fecha y hora.


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <Build.VERSION_CODES.O){
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            }else{
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
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
