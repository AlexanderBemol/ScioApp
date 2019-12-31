package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.R;
import com.nordokod.scio.constants.UserOperations;
import com.nordokod.scio.entity.ConfigurationApp;
import com.nordokod.scio.process.SystemWriteProcess;
import com.nordokod.scio.process.UserMessage;

public class HomeFragment extends Fragment implements BasicFragment {

    private Context context;
    private MainActivity mainActivity;
    private SwitchCompat FHome_SW_StudyMode;
    private SystemWriteProcess swp;


    public HomeFragment() { }

    @SuppressLint("ValidFragment")
    public HomeFragment(Context context, MainActivity mainActivity) {
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initComponents(view);
        initListeners();

        return view;
    }

    @Override
    public void initComponents(View view) {
        FHome_SW_StudyMode        = view.findViewById(R.id.FHome_SW_ActivateStudyMode);

        swp = new SystemWriteProcess(context);
        ConfigurationApp configurationApp = swp.readUserConfig();
        FHome_SW_StudyMode.setChecked(configurationApp.isAppLocker());
    }

    @Override
    public void initListeners() {
        FHome_SW_StudyMode.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            ConfigurationApp configurationApp = swp.readUserConfig();
            configurationApp.setAppLocker(isChecked);
            swp.saveUserConfig(configurationApp);
        }));
    }
}
