package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.controller.FirstConfigurationController;
import com.nordokod.scio.entity.App;
import com.nordokod.scio.R;

import java.util.ArrayList;

public class FirstConfigurationAppBlockFragment extends Fragment implements BasicFragment {

    private RecyclerView listApps;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<App> arraylistapp;

    private FirstConfigurationActivity firstConfigurationActivity;
    private FirstConfigurationController firstConfigurationController;

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

        layoutManager = new LinearLayoutManager(getContext());
        listApps.setLayoutManager(layoutManager);

        mAdapter = new AppRecyclerViewAdapter(getListOfApps());
        ((AppRecyclerViewAdapter) mAdapter).configAdapter(firstConfigurationController);

        listApps.setAdapter(mAdapter);
    }

    @Override
    public void initListeners() {

    }

    protected void configAdapter(FirstConfigurationController controller,FirstConfigurationActivity firstConfigurationActivity) {
        this.firstConfigurationController = controller;
        this.firstConfigurationActivity = firstConfigurationActivity;
    }

    private ArrayList<App> getListOfApps() {
        return firstConfigurationController.getListOfApps();
    }
}
