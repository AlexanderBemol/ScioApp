package com.nordokod.scio.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.R;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

public class FirstConfigurationEducationFragment extends Fragment implements BasicFragment {

    private SwipeSelector educationSelector;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle s) {
        View view = inflater.inflate(R.layout.education_first_configuration, container,false);
        initComponents(view);

        return view;
    }

    @Override
    public void initComponents(View view) {
        educationSelector   = view.findViewById(R.id.educationSelector);

        String description = getActivity().getString(R.string.education_description_swipe);

        educationSelector.setItems(
                new SwipeItem(0, getActivity().getString(R.string.education_title_default), description),
                new SwipeItem(1, getActivity().getString(R.string.education_title_elementary), description),
                new SwipeItem(2, getActivity().getString(R.string.education_title_secondary_school), description),
                new SwipeItem(3, getActivity().getString(R.string.education_title_high_school), description),
                new SwipeItem(4, getActivity().getString(R.string.education_title_college), description),
                new SwipeItem(5, getActivity().getString(R.string.education_title_master), description),
                new SwipeItem(6, getActivity().getString(R.string.education_title_doctoral), description)
        );
    }

    @Override
    public void initListeners() { }

    public Object getEducation() {
        return educationSelector.getSelectedItem().value;
    }
}
