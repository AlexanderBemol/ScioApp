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
    public void validateFields() {
        fcm=mock(FirstConfigurationModel.class);
        String name="Alexander";
        String birthday="17/03/1998";
        int level=1;
        boolean resultado=fcm.validateFields(name,birthday,level);
        assertTrue(resultado);
    }

}