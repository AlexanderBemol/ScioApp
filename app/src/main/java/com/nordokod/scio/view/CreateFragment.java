package com.nordokod.scio.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.R;

public class CreateFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private Activity activity;
    //private CreateController createController;

    public CreateFragment() { }

    @SuppressLint("ValidFragment")
    public CreateFragment(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_guide, container, false);

        initComponents(view);
        initListeners();

        return view;
    }

    @Override
    public void initComponents(View view) {
        // TODO: Inicializar controlador y componentes.
    }

    @Override
    public void initListeners() {

    }
}
