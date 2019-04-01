package com.nordokod.scio.View;

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
}
