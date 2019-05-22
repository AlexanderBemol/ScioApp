package com.nordokod.scio.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.nordokod.scio.controller.FirstConfigurationController;
import com.nordokod.scio.entity.App;
import com.nordokod.scio.R;

import java.util.ArrayList;

public class AppRecyclerViewAdapter extends RecyclerView.Adapter<AppRecyclerViewAdapter.ViewHolder> {

    private ArrayList<App> appArrayList;

    private FirstConfigurationController firstConfigurationController;

    public AppRecyclerViewAdapter(ArrayList<App> appArrayList) {
        this.appArrayList = appArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.IV_Icon.setImageDrawable(appArrayList.get(position).getIcon());
        holder.TV_Name.setText(appArrayList.get(position).getName());
        holder.Switch_State.setChecked(appArrayList.get(position).isState());

        initListeners(holder, position);
    }

    /**
     * Método para inicializar el listener que detectará cuando un usuario
     * bloquee o desbloquee una app.
     *
     * @param holder
     * @param position  - Posición del item (app) del RecyclerView.
     */
    private void initListeners(@NonNull ViewHolder holder, final int position) {
        holder.Switch_State.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(firstConfigurationController.onStateChanged(appArrayList.get(position).getPackagePath(), isChecked));
            }
        });
    }

    @Override
    public int getItemCount() {
        return appArrayList.size();
    }

    /**
     * Método para configurar este Adapter con el objeto del controlador usado por el Activity.
     *
     * @param //firstConfigurationController - Controlador del Activity.
     */
    public void configAdapter(FirstConfigurationController controller) {
        this.firstConfigurationController = controller;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView IV_Icon;
        AppCompatTextView TV_Name;
        SwitchCompat Switch_State;

        ViewHolder(View view) {
            super(view);

            IV_Icon = view.findViewById(R.id.IV_Icon);
            TV_Name = view.findViewById(R.id.TV_Name);
            Switch_State = view.findViewById(R.id.Switch_State);
        }
    }
}
