package com.nordokod.scio.View;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;

import com.nordokod.scio.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class FirstConfigurationActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private AppCompatButton btnStart, btnSkip, btnNext, btnDone;

    private SmartTabLayout smartTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_configuration);
    }


}
