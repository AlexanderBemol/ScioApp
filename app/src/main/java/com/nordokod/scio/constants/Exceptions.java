package com.nordokod.scio.constants;

public enum Exceptions {
    INVALID_VALUE_EXCEPTION("SDE-01","Valor Inválido");
    private String Code;
    private String Title;
    Exceptions(String code,String Title){
        this.Code=code;
        this.Title=Title;
    }
    public String getCode(){
        return this.Code;
    }
    public String getTitle(){
        return this.Title;
    }
}
