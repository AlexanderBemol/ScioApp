package com.nordokod.scio.view;

import com.nordokod.scio.entity.Error;

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
}
