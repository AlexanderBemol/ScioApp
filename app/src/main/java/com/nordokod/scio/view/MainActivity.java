package com.nordokod.scio.view;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.nordokod.scio.R;
import com.nordokod.scio.entity.Error;

public class MainActivity extends AppCompatActivity implements BasicActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationMenu;
    private BottomNavigationView navigationBar;
    private FrameLayout frameLayout;
    private Toolbar toolbar;
    private ActionBar actionBar;

    private HomeFragment    homeFragment;
    private NewGuideFragment createFragment;
    private GuidesFragment  guidesFragment;
    private DialogFragment dialogFragment;
    private Fragment selectedFragment = null;

    //private MainController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initListeners();

        navigationBar.setSelectedItemId(R.id.NAV_Home);
    }

    @Override
    public void initComponents() {
        drawerLayout    = findViewById(R.id.DrawerLayout);
        navigationMenu  = findViewById(R.id.NAV_Menu);
        navigationBar   = findViewById(R.id.NAV_Bar);
        frameLayout     = findViewById(R.id.FL_Main);
        toolbar         = findViewById(R.id.Toolbar);

        homeFragment    = new HomeFragment(this, this);
        createFragment  = new NewGuideFragment(this, this);
        guidesFragment  = new GuidesFragment(this, this);

        //mainController = new MainController();
        // Toolbar
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    @Override
    public void initListeners() {
        navigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.NAV_Home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.NAV_Create:
                        dialogFragment = createFragment;

                        dialogFragment.show(getSupportFragmentManager(), "New");
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

    public void onCloseFragment() {
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("New")).commit();
    }

    public void onClickCategoryListener(View view) {
        createFragment.onClickCategoryListener(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showErrorNoticeDialog(Error error) {

    }

    @Override
    public void showSuccessNoticeDialog(String task) {

    }
}
