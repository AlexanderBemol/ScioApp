package com.nordokod.scio.model;

import android.app.Activity;
import android.content.Context;

import com.nordokod.scio.controller.FirstConfigurationController;
import com.nordokod.scio.view.FirstConfigurationActivity;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class FirstConfigurationModelTest {
    private FirstConfigurationActivity fca=mock(FirstConfigurationActivity.class);
    private Context context=mock(Context.class);
    private Activity activity=mock(Activity.class);
    private FirstConfigurationController fcc;
    private  FirstConfigurationModel fcm;

    @Test
    public void validateFieldsCorrectly() {
        fcc=new FirstConfigurationController(context,activity,fca);
        fcm=new FirstConfigurationModel(fcc,activity,context);
        String name="Alexander";
        String birthday="17/03/1998";
        int level=1;
        boolean resultado=fcm.validateFields(name,birthday,level);
        assertTrue(resultado);
    }

    @Test
    public void validateFieldsI1() {//nombre vacío
        fcc=new FirstConfigurationController(context,activity,fca);
        fcm=new FirstConfigurationModel(fcc,activity,context);
        String name="";
        String birthday="17/03/1998";
        int level=1;
        boolean resultado=fcm.validateFields(name,birthday,level);
        assertFalse(resultado);
    }

    @Test //Fecha futura
    public void validateFieldsI2() {
        fcc=new FirstConfigurationController(context,activity,fca);
        fcm=new FirstConfigurationModel(fcc,activity,context);
        String name="";
        String birthday="17/03/2020";
        int level=1;
        boolean resultado=fcm.validateFields(name,birthday,level);
        assertFalse(resultado);

    }
    @Test
    public void validateFieldsI3() {//no selección de nivel
        fcc=new FirstConfigurationController(context,activity,fca);
        fcm=new FirstConfigurationModel(fcc,activity,context);
        String name="Alexander";
        String birthday="17/03/1998";
        int level=0;
        boolean resultado=fcm.validateFields(name,birthday,level);
        assertFalse(resultado);
    }
    @Test //Fecha de Hoy
    public void validateFieldsI4() {
        fcc=new FirstConfigurationController(context,activity,fca);
        fcm=new FirstConfigurationModel(fcc,activity,context);
        String name="Alexander M";
        String birthday="17/05/2019";
        int level=1;
        boolean resultado=fcm.validateFields(name,birthday,level);
        assertFalse(resultado);
    }
    @Test //nombre no válido
    public void validateFieldsI5() {
        fcc=new FirstConfigurationController(context,activity,fca);
        fcm=new FirstConfigurationModel(fcc,activity,context);
        String name="@";
        String birthday="17/03/1998";
        int level=1;
        boolean resultado=fcm.validateFields(name,birthday,level);
        assertFalse(resultado);
    }

}