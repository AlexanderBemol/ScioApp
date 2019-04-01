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

    private FirstConfigurationController firstConfigurationController;
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
        ((AppRecyclerViewAdapter) mAdapter).configAdapter(firstConfigurationController);
        listApps.setAdapter(mAdapter);
    }

    @Override
    public void initListeners() {

    }

    protected void configAdapter(FirstConfigurationController controller, Context context) {
        this.firstConfigurationController   = controller;
        this.context = context;
    }

    private ArrayList<App> getListOfApps() {
        arraylistapp = new ArrayList<>();

        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_facebook), "Facebook", "holis1", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_settings), "Whatsapp", "holis2", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_panda), "Scio", "holis3", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_successed), "Discord", "holis4", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_error), "Instagram", "holis5", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_facebook), "Facebook", "holis6", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_settings), "Whatsapp", "holis7", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_panda), "Scio", "holis8", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_successed), "Discord", "holis9", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_error), "Instagram", "holis0", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_facebook), "Facebook", "holis90", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_settings), "Whatsapp", "holis87", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_panda), "Scio", "holi43s", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_successed), "Discord", "holis76", false));
        arraylistapp.add(new App(getResources().getDrawable(R.drawable.ic_error), "Instagram", "holis65", false));

        return arraylistapp;
        //return firstConfigurationController.getListOfApps();
    }
}
