package com.nordokod.scio.view;

import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatImageView;

import com.nordokod.scio.R;

interface BasicActivity {
    /**
     * Este método es usado para inicializar los componentes, variables, objetos, etc.
     * <p>
     * No debe ser llamado por clases externas.
     */
    void initComponents();

    /**
     * Este método es usado para crear los onSetClickListener con su lógica.
     * <p>
     * No debe ser llamado por clases externas.
     */
    void initListeners();

    default void showLoadingDialog(AppCompatDialog dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_loading);

        AppCompatImageView IV_Icon = dialog.findViewById(R.id.DLoading_Icon);
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, (-10.0f * 360.0f), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(Animation.INFINITE);

        assert IV_Icon != null;
        IV_Icon.startAnimation(rotateAnimation);
        dialog.show();
    }

    default void closeLoadingDialog(AppCompatDialog dialog) {
        dialog.dismiss();
    }
}
