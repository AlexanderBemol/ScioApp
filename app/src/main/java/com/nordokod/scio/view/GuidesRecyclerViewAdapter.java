package com.nordokod.scio.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.nordokod.scio.R;
import com.nordokod.scio.controller.MainController;
import com.nordokod.scio.entity.Guide;

import java.util.ArrayList;

public class GuidesRecyclerViewAdapter extends RecyclerView.Adapter<GuidesRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private MainActivity activity;
    private MainController mainController;
    private ArrayList<Guide> guideArrayList;
    private DialogFragment dialogFragment;
    private int category;

    public GuidesRecyclerViewAdapter(ArrayList<Guide> guideArrayList, int category, Context context, MainActivity activity) {
        this.guideArrayList = guideArrayList;
        this.category = category;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public GuidesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guide_view, parent, false);

        GuidesRecyclerViewAdapter.ViewHolder viewHolder = new GuidesRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GuidesRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.IV_Icon.setImageResource(getCategoryIcon());
        holder.TV_Topic.setText(guideArrayList.get(position).getTopic());
        holder.TV_Category.setText(getCategoryName());
        holder.TV_Days.setText(context.getResources().getString(R.string.txt_days_left, guideArrayList.get(position).getDaysLeft()));
        holder.Switch_State.setChecked(guideArrayList.get(position).isIs_actived());

        initListeners(holder, position);
    }

    private void initListeners(GuidesRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.CL_Guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment = new MenuGuideFragment(context, mainController, guideArrayList.get(position));
                dialogFragment.show(activity.getSupportFragmentManager(), "Menu Guide");
            }
        });

        holder.Switch_State.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Crear un método par que cuando el usaurio active o desactive la guía mande a
                //  llamar un método al backend que haga el cambio en la base de datos o donde sea que se almacene esa guía.
                //  Debe mandar por parametro el ID y el estado.
                // buttonView.setChecked(firstConfigurationController.onStateChanged(appArrayList.get(position).getPackagePath(), isChecked));
            }
        });
    }

    /**
     * Método para configurar este Adapter con el objeto del controlador usado por el Activity.
     *
     * @param //firstConfigurationController - Controlador del Activity.
     */

    public void configAdapter(MainController mainController) {
        this.mainController = mainController;
    }


    @Override
    public int getItemCount() {
        return guideArrayList.size();
    }

    private int getCategoryName() {
        switch (category) {
            case 1:     return R.string.category_exact_sciences;
            case 2:     return R.string.category_social_sciences;
            case 3:     return R.string.category_sports;
            case 4:     return R.string.category_art;
            case 5:     return R.string.category_tech;
            case 6:     return R.string.category_entertainment;
            case 7:     return R.string.category_others;
            default:    return R.string.category_others;
        }
    }

    private int getCategoryIcon() {
        switch (category) {
            case 1:     return R.drawable.ic_flask;
            case 2:     return R.drawable.ic_globe;
            case 3:     return R.drawable.ic_ball;
            case 4:     return R.drawable.ic_palette;
            case 5:     return R.drawable.ic_code;
            case 6:     return R.drawable.ic_theatre;
            case 7:     return R.drawable.ic_scio_face;
            default:    return R.drawable.ic_scio_face;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout CL_Guide;
        AppCompatImageView IV_Icon;
        AppCompatTextView TV_Topic, TV_Category, TV_Days;
        SwitchCompat Switch_State;

        ViewHolder(View view) {
            super(view);

            CL_Guide = view.findViewById(R.id.CL_Guide);
            IV_Icon = view.findViewById(R.id.IV_Icon);
            TV_Topic = view.findViewById(R.id.TV_Topic);
            TV_Days = view.findViewById(R.id.TV_Days);
            TV_Category = view.findViewById(R.id.TV_Category);
            Switch_State = view.findViewById(R.id.Switch_ActivateGuide);
        }
    }
}