package com.nordokod.scio.old.view;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.old.entity.App;
import com.nordokod.scio.R;

import java.util.ArrayList;

public class AppRecyclerViewAdapter extends RecyclerView.Adapter<AppRecyclerViewAdapter.ViewHolder> {

    private ArrayList<App> appArrayList;
    AppRecyclerViewAdapter(ArrayList<App> appArrayList) {
        this.appArrayList = appArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.IV_Icon.setImageDrawable(appArrayList.get(position).getIcon());
        holder.TV_Name.setText(appArrayList.get(position).getName());

        initListeners(holder, position);

        holder.Switch_State.setChecked(appArrayList.get(position).isState());
    }

    /**
     * Método para inicializar el listener que detectará cuando un usuario
     * bloquee o desbloquee una app.
     *
     * @param holder
     * @param position  - Posición del item (app) del RecyclerView.
     */
    private void initListeners(@NonNull ViewHolder holder, final int position) {
        holder.Switch_State.setOnCheckedChangeListener((buttonView, isChecked) -> {
            buttonView.setChecked(isChecked);
            appArrayList.get(position).setState(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return appArrayList.size();
    }

    public ArrayList<App> getAppArrayList() {
        return appArrayList;
    }

    /**
     * Método para configurar este Adapter con el objeto del controlador usado por el Activity.
     *
     * @param //firstConfigurationController - Controlador del Activity.
     */

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView IV_Icon;
        AppCompatTextView TV_Name;
        SwitchCompat Switch_State;

        ViewHolder(View view) {
            super(view);

            IV_Icon = view.findViewById(R.id.QCard_IV_Icon);
            TV_Name = view.findViewById(R.id.TV_Name);
            Switch_State = view.findViewById(R.id.Switch_State);
        }
    }
}
