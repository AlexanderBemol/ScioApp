package com.nordokod.scio.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Window;

import com.nordokod.scio.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImportGuideDialog implements BasicDialog {

    private Context context;
    private Dialog dialog;

    private AppCompatImageView BTN_Cancel;
    private AppCompatTextView TV_User, TV_Topic, TV_Category, TV_Month, TV_Day, TV_Time, TV_Hour;
    private AppCompatButton BTN_Import;
    private CircleImageView CI_User_Photo;

    @Override
    public void initDialog() {
        dialog = new Dialog(context, R.style.Theme_AppCompat_Dialog_Alert);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_import_guide);
    }

    @Override
    public void initComponents() {

    }

    @Override
    public void initListeners() {

    }

    @Override
    public void showDialog() {

    }
}
