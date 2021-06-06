package com.nordokod.scio.old.constants;

public enum StudyLevel {
    PRIMARY(1,"Primaria"),
    HIGH_SCHOOL1(2,"Secundaria"),
    HIGH_SCHOOL2(3,"Bachillerato"),
    UNIVERSITY(4,"Universidad"),
    MASTER(5,"Maestría"),
    DOCTORATE(6,"Doctorado");
    private int levelCode;
    private String levelDescription;
    StudyLevel(int levelCode,String levelDescription){
        this.levelCode=levelCode;
        this.levelDescription=levelDescription;
    }
    public int getLevelCode(){
        return this.levelCode;
    }
    public String getLevelDescription(){
        return this.levelDescription;
    }
}
