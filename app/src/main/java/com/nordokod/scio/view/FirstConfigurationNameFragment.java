package com.nordokod.scio.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.R;
import com.nordokod.scio.model.User;

import java.util.Objects;

public class FirstConfigurationNameFragment extends Fragment implements BasicFragment {

    private AppCompatEditText ET_Name;
    private FirstConfigurationActivity firstConfigurationActivity;
    private User userModel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle s) {
        View view = inflater.inflate(R.layout.name_first_configuration, container,false);
        initComponents(view);
        loadDefaultName();
        return view;
    }

    @Override
    public void initComponents(View view) {
        ET_Name = view.findViewById(R.id.txtName);
        userModel = new User();
    }

    @Override
    public void initListeners() {

    }

    protected void configFragment(FirstConfigurationActivity firstConfigurationActivity) {
        this.firstConfigurationActivity=firstConfigurationActivity;
    }

    public void loadDefaultName(){
        try {
            setName(userModel.getBasicUserInfo().getUsername());
        } catch (Exception e) {
            firstConfigurationActivity.showError(e);
        }
    }
    public void setName(String name) {
        ET_Name.setText(name);
    }
    public String getName(){
        return Objects.requireNonNull(ET_Name.getText()).toString();
    }

    public void updateName() {
        /*userModel.updateUsername(getName()).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                firstConfigurationActivity.showError(new OperationCanceledException());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firstConfigurationActivity.showError(e);
            }
        });*/
    }
}
