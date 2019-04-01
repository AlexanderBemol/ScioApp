package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.entity.App;
import com.nordokod.scio.R;

import java.util.ArrayList;

public class FirstConfigurationAppBlockFragment extends Fragment implements BasicFragment {

    private RecyclerView listApps;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<App> arraylistapp;

    //private FirstConfigurationController firstConfigurationController;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle s) {
        View view = inflater.inflate(R.layout.appblock_first_configuration, container,false);
        initComponents(view);
        initListeners();

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initComponents(View view) {
        listApps = view.findViewById(R.id.listApps);

        layoutManager = new LinearLayoutManager(context);
        listApps.setLayoutManager(layoutManager);

        mAdapter = new AppRecyclerViewAdapter(getListOfApps());
        //((AppRecyclerViewAdapter) mAdapter).configAdapter(firstConfigurationController);
        listApps.setAdapter(mAdapter);
    }

    @Override
    public void initListeners() {

    }
/*
    protected void configAdapter(FirstConfigurationController controller, Context context) {
        this.firstConfigurationController   = controller;
        this.context = context;
    }
*/
    private ArrayList<App> getListOfApps() {
        return new ArrayList<>();
        //return firstConfigurationController.getListOfApps();
    }
}
