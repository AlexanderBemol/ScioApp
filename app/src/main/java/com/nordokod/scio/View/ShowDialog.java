package com.nordokod.scio.View;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.WindowManager;

import com.nordokod.scio.R;
import com.victor.loading.newton.NewtonCradleLoading;

class ShowDialog {
    private Context context;
    private Dialog dialog;

    public ShowDialog(Dialog context){
        this.dialog = context;
    }

    @SuppressLint("ResourceType")
    void showDialog(Type type, int message){
        if (dialog == null)
            dialog = new Dialog(context);

        if (dialog.isShowing())
            dialog.dismiss();

        switch (type){
            case SUCCESS:
                dialog.findViewById(R.layout.dialog_success);
                showSuccessDialog(message);
                break;
            case LOADING:
                dialog.findViewById(R.layout.dialog_progress);
                showLoadingDialog();
                break;
            case ERROR:
                dialog.findViewById(R.layout.dialog_error);
                showErrorDialog(message);
                break;
            case CLOSE_DIALOG:
                dialog.dismiss();
                return;
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        dialog.getWindow().getAttributes().windowAnimations = R.style.NoticeDialogAnimation;

        dialog.show();

        if (type != Type.LOADING) {
            Handler handler;
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            }, 1000);

            handler = null;
        }
    }

    private void showSuccessDialog(int message){
        AppCompatTextView txtMessage = dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(message);
    }

    private void showErrorDialog(int message){
        AppCompatTextView txtMessage = dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(message);
    }

    private void showLoadingDialog(){
        NewtonCradleLoading loading = dialog.findViewById(R.id.loading);
        loading.start();
    }

    enum Type {
        SUCCESS,
        ERROR,
        LOADING,
        CLOSE_DIALOG
    }

}
