package com.nordokod.scio.View;

import com.nordokod.scio.Entidad.Error;

interface BasicActivity {
    /**
     * Este método es usado para inicializar los componentes, variables, objetos, etc.
     *
     * No debe ser llamado por clases externas.
     */
    void initComponents();

    /**
     * Este método es usado para crear los onSetClickListener con su lógica.
     *
     * No debe ser llamado por clases externas.
     */
    void initListeners();

    /**
     * Este método es usado para mostrar el Dialog correspondiente al error mandado.
     *
     * Normalmente se manda a llamar desde el controlador.
     *
     * @param error = String que especifica el error ocurrido.
     */
    void showErrorNoticeDialog(Error error);

    /**
     * Este método es usado para mostrar el Dialog correspondiente a la tarea realizada con éxito.
     *
     * Normalmente se manda a llamar desde el controlador.
     *
     * @param task = String que especifica la tarea que fué satisfactoria.
     */
    void showSuccessNoticeDialog(String task);
}
