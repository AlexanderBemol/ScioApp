package com.nordokod.scio.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.R;

public class FirstConfigurationCompleteFragment extends Fragment implements BasicFragment {

    private AppCompatImageView IV_Image;
    private AppCompatTextView TV_Title, TV_Description;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle s) {
        View view = inflater.inflate(R.layout.complete_first_configuration, container,false);
        initComponents(view);

        return view;
    }


    @Override
    public void initComponents(View view) {
        IV_Image        = view.findViewById(R.id.IV_Image);
        TV_Title        = view.findViewById(R.id.title);
        TV_Description  = view.findViewById(R.id.TV_Description);
    }

    @Override
    public void initListeners() {

    }

    protected void onComplete() {
        IV_Image.setImageResource(R.drawable.img_complete);
        TV_Title.setText(R.string.title_done_first_configuration);
        TV_Description.setText(R.string.description_done_first_configuration);
    }
}
