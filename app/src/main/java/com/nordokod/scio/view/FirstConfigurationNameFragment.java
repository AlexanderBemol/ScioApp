package com.nordokod.scio.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.R;

public class FirstConfigurationNameFragment extends Fragment implements BasicFragment {

    private AppCompatEditText ET_Name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle s) {
        View view = inflater.inflate(R.layout.name_first_configuration, container,false);
        initComponents(view);

        return view;
    }

    @Override
    public void initComponents(View view) {
        ET_Name = view.findViewById(R.id.txtName);
    }

    @Override
    public void initListeners() {

    }

    public String getName() {
        return ET_Name.getText().toString();
    }
}
