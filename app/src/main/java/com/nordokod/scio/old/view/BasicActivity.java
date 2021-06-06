package com.nordokod.scio.old.view;

import android.content.Context;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatImageView;

import com.nordokod.scio.R;

import java.time.Duration;

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

    /**
     * Este método es usado para inicializar las animaciones necesarias de los componentes.
     *
     * No es obligatorio usarlo.
     */
    default void initAnimations() {}

    /**
     * Este método es usado para inicializar las variables o logica necesaria.
     *
     * No es obligatorio usarlo.
     */
    default void initVariables() {}

    default void showLoadingDialog(AppCompatDialog dialog, Context context) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_loading);

        AppCompatImageView IV_Icon = dialog.findViewById(R.id.DLoading_Icon);
        Animation rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);

        assert IV_Icon != null;
        IV_Icon.startAnimation(rotateAnimation);
        dialog.show();
    }

    default void closeLoadingDialog(AppCompatDialog dialog) {
        dialog.dismiss();
    }
}
