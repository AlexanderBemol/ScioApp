package com.nordokod.scio.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.R;

public class FirstConfigurationPhoto extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle s) {
        return inflater.inflate(R.layout.name_first_configuration, container,false);
    }
}
