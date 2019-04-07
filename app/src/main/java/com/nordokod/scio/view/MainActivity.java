package com.nordokod.scio.view;

import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.nordokod.scio.R;
import com.nordokod.scio.entity.Error;

public class MainActivity extends AppCompatActivity implements BasicActivity {

    private NavigationView navigationMenu;
    private BottomNavigationView navigationBar;
    private FrameLayout frameLayout;

    private HomeFragment    homeFragment;
    private CreateFragment  createFragment;
    private GuidesFragment  guidesFragment;

    //private MainController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initListeners();
    }

    @Override
    public void initComponents() {
        // navigationMenu  = findViewById(R.id.NAV_Menu);
        navigationBar   = findViewById(R.id.NAV_Bar);
        frameLayout     = findViewById(R.id.FL_Main);

        homeFragment    = new HomeFragment(this, this);
        createFragment  = new CreateFragment(this, this);
        guidesFragment  = new GuidesFragment(this, this);

        //mainController = new MainController();
    }

    @Override
    public void initListeners() {
        navigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.NAV_Home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.NAV_Create:
                        selectedFragment = new CreateFragment();
                        break;
                    case R.id.NAV_Guides:
                        selectedFragment = new GuidesFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.FL_Main, selectedFragment).commit();

                return true;
            }
        });
    }

    @Override
    public void showErrorNoticeDialog(Error error) {

    }

    @Override
    public void showSuccessNoticeDialog(String task) {

    }
}
