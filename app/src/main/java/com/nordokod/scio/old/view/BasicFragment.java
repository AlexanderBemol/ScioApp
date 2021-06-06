package com.nordokod.scio.old.view;

import android.view.View;

interface BasicFragment {
    /**
     * Este método es usado para inicializar los componentes, variables, objetos, etc.
     *
     * No debe ser llamado por clases externas.
     */
    void initComponents(View view);

    /**
     * Este método es usado para crear los onSetClickListener con su lógica.
     *
     * No debe ser llamado por clases externas.
     */
    void initListeners();

    /**
     * Este método es usado para inicializar las variables o logica necesaria.
     */
    default void initVariables() {}

    /**
     * Este método es usado para inicializar las animaciones.
     *
     * No debe ser llamado por clases externas.
     */
    default void initAnimations() {}

    /**
     * Método usado para cerrar el Activity desde el que se esté invocando el Fragment.
     */
    default void goToFinishActivity() {}
}
