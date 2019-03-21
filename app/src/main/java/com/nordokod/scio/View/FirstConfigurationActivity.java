package com.nordokod.scio.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.nordokod.scio.R;

import me.relex.circleindicator.CircleIndicator;

public class FirstConfigurationActivity extends AppCompatActivity {
    //==============================================================================================
    // VARIABLES Y OBJETOS
    //==============================================================================================
    // Botones.
    private AppCompatButton btnSkip;
    // Constantes.
    private final int NUM_PAGES = 5;
    //
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;

    //==============================================================================================
    // ON CREATE
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_configuration);
        initComponents();
        initListeners();
    }
    //==============================================================================================
    // COMPONENTS
    //==============================================================================================
    private void initComponents(){
        viewPager       = findViewById(R.id.viewPager);
        circleIndicator  = findViewById(R.id.pageIndicator);
        btnSkip         = findViewById(R.id.btnSkip);

        viewPager.setAdapter(fragmentStatePagerAdapter);
        circleIndicator.setViewPager(viewPager);
    }
    //==============================================================================================
    // LISTENERS
    //==============================================================================================
    private void initListeners(){
        // Botón para saltar paso.
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == NUM_PAGES){
                    finishConfiguration();
                }else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                }
            }
        });

        circleIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if ((position == 0 || position == 2) && btnSkip.getVisibility() == View.VISIBLE)
                    btnSkip.setVisibility(View.GONE);
                else
                    btnSkip.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    //==============================================================================================
    // OTROS
    //==============================================================================================
    private FragmentStatePagerAdapter fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new FirstConfigurationName();
                case 1: return new FirstConfigurationBirthday();
                case 2: return new FirstConfigurationSchool();
                case 3: return new FirstConfigurationAppBlock();
                case 4: return new FirstConfigurationComplete();
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    };
    //==============================================================================================
    // FINALIZAR LA COONFIGURACIÓN.
    // Se mandan los datos para almacenarse.
    //==============================================================================================
    private void finishConfiguration(){
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("configuration_complete", true);
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);

        finish();
    }
}
