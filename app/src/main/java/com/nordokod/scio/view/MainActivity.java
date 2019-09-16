package com.nordokod.scio.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.nordokod.scio.R;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements BasicActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationMenu;
    private Toolbar toolbar;
    private ActionBar actionBar;

    private User user;

    // Objetos para el menú de navegación inferior
    private AHBottomNavigation bottomNavigation;
    private AHBottomNavigationItem itemHome, itemCreate, itemGuides;
    private NoSwipePager viewPager;
    private BottomBarAdapter pagerAdapter;

    private CircleImageView CIV_Photo;
    private AppCompatTextView TV_Name, BTN_Logout;

    private HomeFragment homeFragment;
    private NewGuideFragment createFragment;
    private GuidesFragment guidesFragment;
    private DialogFragment dialogFragment;
    private Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initListeners();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void initComponents() {
        drawerLayout        = findViewById(R.id.DrawerLayout);
        navigationMenu      = findViewById(R.id.NAV_Menu);

        // Bottom Navigation Bar
        itemHome            = new AHBottomNavigationItem(R.string.navbar_title_home, R.drawable.ic_home, R.color.onSurfaceColor);
        itemCreate          = new AHBottomNavigationItem(R.string.navbar_title_create, R.drawable.ic_new, R.color.onSurfaceColor);
        itemGuides          = new AHBottomNavigationItem(R.string.navbar_title_guides, R.drawable.ic_my_guides, R.color.onSurfaceColor);

        bottomNavigation    = findViewById(R.id.NAV_Bar);
        bottomNavigation.addItem(itemHome);
        bottomNavigation.addItem(itemCreate);
        bottomNavigation.addItem(itemGuides);
        bottomNavigation.setCurrentItem(0);
        bottomNavigation.setColored(true);
        bottomNavigation.setDefaultBackgroundColor(R.color.backgroundColor);
        bottomNavigation.setColoredModeColors(R.color.iconSelectedColor, R.color.iconNormalColor);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        viewPager       = findViewById(R.id.VP_Main);
        viewPager.setPagingEnabled(false);
        pagerAdapter    = new BottomBarAdapter(getSupportFragmentManager());

        homeFragment    = new HomeFragment(this, this);
        createFragment  = new NewGuideFragment(this);
        guidesFragment  = new GuidesFragment(this, this);

        pagerAdapter.addFragment(homeFragment);
        pagerAdapter.addFragment(createFragment);
        pagerAdapter.addFragment(guidesFragment);

        viewPager.setAdapter(pagerAdapter);

        toolbar         = findViewById(R.id.Toolbar);

        // Botón para cerrar sesión
        BTN_Logout      = navigationMenu.findViewById(R.id.BTN_Logout);

        View header     = navigationMenu.getHeaderView(0);

        CIV_Photo       = header.findViewById(R.id.CIV_Photo);
        TV_Name         = header.findViewById(R.id.TV_Name);

        //mainController.loadGuides();

        // Toolbar
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //mainController.requestPhoto();

        //TV_Name.setText(mainController.getName());
    }

    @Override
    public void initListeners() {
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position){
                    case 0: // Pantalla Home
                        if (!wasSelected){
                            viewPager.setCurrentItem(position);
                        }else{

                        }
                        return true;
                    case 1: // Pantalla Crear Guia
                        if (!wasSelected){
                            viewPager.setCurrentItem(position);
                        }else{

                        }
                        return true;
                    case 2: // Pantalla Tus Guias
                        if (!wasSelected){
                            viewPager.setCurrentItem(position);
                        }else{

                        }
                        return true;
                }
                return false;
            }
        });

        BTN_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User();
                user.logOut();
            }
        });
    }

    public void onNewQuestionDialog(Guide guide) {
        dialogFragment = new NewQuestionFragment(this, this, guide);
        dialogFragment.show(getSupportFragmentManager(), "New Question");
    }

    /**
     * Método para inicializar Fragment para la pregunta de tipo Opción muultiple y mostrarlo.
     * @param guide
     */
    public void onNewMultipleChoiceQuestionDialog(Guide guide) {
        dialogFragment = new NewMultipleChoiceQuestionFragment(this, this, guide);
        dialogFragment.show(getSupportFragmentManager(), "New Multiple Choice");
    }

    /**
     * Método para inicializar Fragment para la pregunta de tipo Respuesta abierta y mostrarlo.
     * @param guide
     */
    public void onNewOpenAnswerQuestionDialog(Guide guide) {
        dialogFragment = new NewOpenAnswerQuestionFragment(this, this, guide);
        dialogFragment.show(getSupportFragmentManager(), "New Open Answer");
    }

    /**
     * Método para inicializar Fragment para la pregunta de tipo Verdadero/Falso y mostrarlo.
     * @param guide
     */
    public void onNewTrueFalseQuestionDialog(Guide guide) {
        dialogFragment = new NewTrueFalseQuestionFragment(this, this, guide);
        dialogFragment.show(getSupportFragmentManager(), "New TrueFalse");
    }

    /**
     * Método para cerrar Fragments según su tag.
     * @param tag
     */
    public void onCloseFragment(String tag) {
        switch (tag) {
            case "New Guide":
                getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("New Guide"))).commit();
                break;
            case "New Question":
                getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("New Question"))).commit();
                break;
            case "Menu Guide":
                getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("Menu Guide"))).commit();
                break;
            case "New Multiple Choice":
                getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("New Multiple Choice"))).commit();
                break;
            case "New TrueFalse":
                getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("New TrueFalse"))).commit();
                break;
            case "New Open Answer":
                getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("New Open Answer"))).commit();
                break;
        }
    }

    public void onClickCategoryListener(View view) {
        createFragment.onClickCategoryListener(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSuccessSaveQuestion() {
        Toasty.success(this, R.string.message_save_question_success).show();
    }

    public void onErrorSaveQuestion() {
        Toasty.error(this, R.string.message_save_question_error).show();
    }

    public void onEmptyField() {
        Toasty.warning(this, R.string.message_emptyfields_error).show();
    }

    public void onUnselectedAnswer() {
        Toasty.warning(this, R.string.message_unselected_answer_warning).show();
    }

    public void setUserPhoto(Bitmap photo){
        CIV_Photo.setImageBitmap(photo);
    }

    public void setDefaultUserPhoto(){
        CIV_Photo.setImageResource(R.drawable.default_photo);
    }

    public void refreshGuides(){
        Log.d("testeo","refresh");
        if(guidesFragment!=null&&selectedFragment==guidesFragment){
            Log.d("testeo","selcted");
            guidesFragment.onBackFragment();
        }
    }

    /**
     *
     */
    abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        // Remueve el Fragment cuando está inactivo.
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        // Regresa el Fragment de la pocisión especificada si está instanciado.
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    /**
     *
     */
    public class BottomBarAdapter extends SmartFragmentStatePagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        public BottomBarAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }


        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    /**
     * ViewPager personalizado para desactivar el swipe.
     */

}
