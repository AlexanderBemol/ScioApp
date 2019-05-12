package com.nordokod.scio.entity;

public class Error {
    private int descriptionResource = 0;
    private String descriptionText = null;
    private int type = GENERAL;

    public Error(){}

    public Error(int type) {
        this.type = type;
    }

    public Error(int description, int type) {
        this.descriptionResource = description;
        this.type = type;
    }

    public Error(String description, int type) {
        this.descriptionText = description;
        this.type = type;
    }

    public int getDescriptionResource() {
        return descriptionResource;
    }

    public void setDescriptionResource(int descriptionResource) {
        this.descriptionResource = descriptionResource;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    /**
     * Constante utilizada cuando no se conoce especificamente el error.
     */
    public static final int GENERAL                 = 0;

    /**
     * Constante utlizada cuando el error es que un campo requerido está vació.
     */
    public static final int EMPTY_FIELD             = 1;

    /**
     * Constante utilizada cuando el error ocurre al momento guardar algo localmente.
     */
    public static final int WHEN_SAVING_ON_DEVICE   = 2;

    /**
     * Constante utilizada cuando el error ocurre al momento de guardar algo en la base de datos.
     */
    public static final int WHEN_SAVING_ON_DATABASE = 3;

    /**
     * Constante utilizada cuando el error ocurre al momento de cargar algo.
     */
    public static final int WHEN_LOADING            = 4;

    /**
     * Constante utilizada cuando el error ocurre al no tener conexión a internet.
     */
    public static final int CONNECTION              = 5;

    /**
     * Constante utilizada cuando el error ocurre al iniciar sesión con correo.
     */
    public static final int LOGIN_MAIL              = 6;

    /**
     * Constante utilizada cuando el error ocurre al iniciar sesión con Facebook.
     */
    public static final int LOGIN_FACEBOOK          = 7;

    /**
     * Constante utilizada cuando el error ocurre al iniciar sesión con Google.
     */
    public static final int LOGIN_GOOGLE            = 8;

    /**
     * Constante utilizada cuando el error ocurre al registrar una nueva cuenta.
     */
    public static final int SIGNUP                  = 9;

    /**
     * Constante utilizada cuando el error ocurre al no seleccionar una opción válida.
     */
    public static final int INVALID_OPTION          = 10;

    /**
     * Constante utilizada cuando el error ocurre al seleccionar una fecha mayor a la actual
     * en el DatePickerDialog.
     */
    public static final int GUY_FROM_THE_FUTURE     = 11;

    /**
     * Constante utilizada cuando el usuario alcanzó el número máximo de aplicaciones bloqueadas.
     * Este limite sólo existe cuando el usuario es FREE.
     */
    public static final int MAXIMUM_NUMBER_OF_APPS_REACHED = 12;
    /**
     * Constante utilizada cuando el usuario introduce un nombre inválido
     */
    public static final int INVALID_USER_NAME = 13;


}
