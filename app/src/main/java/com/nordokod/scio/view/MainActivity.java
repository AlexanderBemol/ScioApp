package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.nordokod.scio.R;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.OperationCanceledException;
import com.nordokod.scio.model.User;
import com.nordokod.scio.process.DownloadImageProcess;
import com.nordokod.scio.process.MediaProcess;
import com.nordokod.scio.process.UserMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements BasicActivity {

    private DrawerLayout drawerLayout;

    private Menu MenuItems;

    // Objetos para el menú de navegación inferior
    private AHBottomNavigation bottomNavigation;
    private AHBottomNavigationItem itemHome, itemCreate, itemGuides;
    private NoSwipePager viewPager;
    private BottomBarAdapter pagerAdapter;

    private CircleImageView CIV_Photo;
    private AppCompatTextView TV_Name, BTN_Logout;

    private HomeFragment homeFragment;
    private CreateFragment createFragment;
    private NewGuideFragment newGuideFragment;
    private GuidesFragment guidesFragment;
    private DialogFragment dialogFragment;
    private Fragment selectedFragment = null;

    private User userModel;
    private com.nordokod.scio.entity.User actualUserEntity;
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
        NavigationView navigationMenu = findViewById(R.id.NAV_Menu);

        //Navigation menu
        MenuItems = navigationMenu.getMenu();

        // Bottom Navigation Bar
        itemHome            = new AHBottomNavigationItem(R.string.navbar_title_home, R.drawable.ic_home, R.color.onSurfaceColor);
        itemCreate          = new AHBottomNavigationItem(R.string.navbar_title_create, R.drawable.ic_new, R.color.onSurfaceColor);
        itemGuides          = new AHBottomNavigationItem(R.string.navbar_title_guides, R.drawable.ic_my_guides, R.color.onSurfaceColor);

        bottomNavigation    = findViewById(R.id.NAV_Bar);
        bottomNavigation.addItem(itemHome);
        bottomNavigation.addItem(itemCreate);
        bottomNavigation.addItem(itemGuides);
        //bottomNavigation.setTranslucentNavigationEnabled(true);
        //bottomNavigation.setCurrentItem(0);
        //bottomNavigation.setColored(true);
        //bottomNavigation.setDefaultBackgroundResource(R.color.backgroundColor);
        //bottomNavigation.setColoredModeColors(Color.WHITE, R.color.iconNormalColor);
        //bottomNavigation.setForceTint(true);
        bottomNavigation.setAccentColor(R.color.iconSelectedColor);
        //bottomNavigation.setInactiveColor(R.color.iconNormalColor);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        viewPager       = findViewById(R.id.VP_Main);
        viewPager.setPagingEnabled(false);
        pagerAdapter    = new BottomBarAdapter(getSupportFragmentManager());

        homeFragment    = new HomeFragment(this, this);
        pagerAdapter.addFragment(homeFragment);
        createFragment  = new CreateFragment(this, this);
        pagerAdapter.addFragment(createFragment);
        guidesFragment  = new GuidesFragment(this, this);
        pagerAdapter.addFragment(guidesFragment);

        newGuideFragment = new NewGuideFragment(this, this);

        viewPager.setAdapter(pagerAdapter);

        Toolbar toolbar = findViewById(R.id.Toolbar);

        // Botón para cerrar sesión
        BTN_Logout      = navigationMenu.findViewById(R.id.BTN_Logout);

        View header     = navigationMenu.getHeaderView(0);

        CIV_Photo       = header.findViewById(R.id.CIV_Photo);
        TV_Name         = header.findViewById(R.id.TV_Name);

        //mainController.loadGuides();

        // Toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        userModel = new User();
        if(!userModel.isUserLogged())goToLoginActivity();

        actualUserEntity = new com.nordokod.scio.entity.User();
        try {
            actualUserEntity = userModel.getBasicUserInfo();
            userModel.getUserInformation(actualUserEntity).addOnSuccessListener(documentSnapshot -> actualUserEntity = userModel.getUserFromDocument(documentSnapshot))
                    .addOnCanceledListener(() -> showError(new OperationCanceledException()))
                    .addOnFailureListener(this::showError);
        } catch (Exception e) {
            showError(e);
        }

        //link dinámico
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(
                    pendingDynamicLinkData -> {
                        if(pendingDynamicLinkData!=null){
                            com.nordokod.scio.model.Guide guideModel = new com.nordokod.scio.model.Guide();
                            guideModel.getPublicGuide(pendingDynamicLinkData).addOnSuccessListener(documentSnapshot -> {
                                Guide guide = guideModel.getGuideFromDocument(documentSnapshot);
                                ImportGuideDialog importGuideDialog = new ImportGuideDialog(getApplicationContext());
                                importGuideDialog.showDialog(guide,documentSnapshot);
                            });
                        }
                    }
                );

        //obtener foto de usuario
        Bitmap localPhoto = userModel.getLocalProfilePhoto(getApplicationContext(),actualUserEntity);
        if(localPhoto==null) {
            switch (userModel.getProfilePhotoHost(actualUserEntity)) {
                case GOOGLE_OR_FACEBOOK_STORAGE:
                    userModel.getExternalProfilePhoto(new DownloadImageProcess.CustomListener() {
                        @Override
                        public void onCompleted(Bitmap photo) {
                            try {
                                userModel.saveProfilePhotoInLocal(MainActivity.this, photo, actualUserEntity);
                                setUserPhoto(photo);
                            } catch (Exception e) {
                                setDefaultUserPhoto();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            setDefaultUserPhoto();
                        }
                    }, actualUserEntity);
                    break;
                case FIREBASE_STORAGE:
                    try {
                        userModel.getFirebaseProfilePhoto(actualUserEntity).addOnSuccessListener(bytes -> {
                            try{
                                Bitmap userPhoto = new MediaProcess().createBitmapWithBytes(bytes);
                                userModel.saveProfilePhotoInLocal(MainActivity.this, userPhoto,actualUserEntity);
                                setUserPhoto(userPhoto);
                            }catch (Exception e){
                                setDefaultUserPhoto();
                            }
                        }).addOnCanceledListener(this::setDefaultUserPhoto).addOnFailureListener(ex -> setDefaultUserPhoto());
                    } catch (Exception e) {
                        setDefaultUserPhoto();
                        //showError(e);
                    }
            }
        }
        else{
            setUserPhoto(localPhoto);
        }
        try {
            TV_Name.setText(userModel.getBasicUserInfo().getUsername());
        } catch (Exception e) {
            showError(e);
        }
    }


    @Override
    public void initListeners() {
        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case 0:
                    if (!wasSelected) {
                        viewPager.setCurrentItem(position);
                    } else {

                    }
                    return true;
                case 1:
                    viewPager.setCurrentItem(position);

                    dialogFragment = newGuideFragment;

                    dialogFragment.show(getSupportFragmentManager(), "New Guide");
                    return true;
                case 2:
                    if (!wasSelected) {
                        viewPager.setCurrentItem(position);
                    }
                    return true;
            }
            return false;
        });

        BTN_Logout.setOnClickListener(v -> {
            userModel.logOut();
            goToLoginActivity();
        });

        //listener de items de menú
        /*MenuItems.findItem(R.id.Menu_Acount).setOnMenuItemClickListener(item -> {

        });
        MenuItems.findItem(R.id.Menu_Settings).setOnMenuItemClickListener(item -> {

        });
        MenuItems.findItem(R.id.Menu_Security).setOnMenuItemClickListener(item -> {

        });*/
        MenuItems.findItem(R.id.Menu_Apps).setOnMenuItemClickListener(item -> {
                Intent intent = new Intent(MainActivity.this,LockedAppsOptionActivity.class);
                startActivity(intent);
                return true;
        });
        /*MenuItems.findItem(R.id.Menu_About).setOnMenuItemClickListener(item -> {

        });*/


    }

    public void onNewQuestionDialog(Guide guide) {
        dialogFragment = new NewQuestionFragment(this, this, guide);
        dialogFragment.show(getSupportFragmentManager(), "New Question");
    }

    /**
     * Método para inicializar Fragment para la pregunta de tipo Opción muultiple y mostrarlo.
     * @param guide entidad de la guía
     */
    public void onNewMultipleChoiceQuestionDialog(Guide guide) {
        dialogFragment = new NewMultipleChoiceQuestionFragment(this, this, guide);
        dialogFragment.show(getSupportFragmentManager(), "New Multiple Choice");
    }

    /**
     * Método para inicializar Fragment para la pregunta de tipo Respuesta abierta y mostrarlo.
     * @param guide entidad de la guía
     */
    public void onNewOpenAnswerQuestionDialog(Guide guide) {
        dialogFragment = new NewOpenAnswerQuestionFragment(this, this, guide);
        dialogFragment.show(getSupportFragmentManager(), "New Open Answer");
    }

    /**
     * Método para inicializar Fragment para la pregunta de tipo Verdadero/Falso y mostrarlo.
     * @param guide entidad de la guía
     */
    public void onNewTrueFalseQuestionDialog(Guide guide) {
        dialogFragment = new NewTrueFalseQuestionFragment(this, this, guide);
        dialogFragment.show(getSupportFragmentManager(), "New TrueFalse");
    }

    /**
     * Método para cerrar Fragments según su tag.
     * @param tag tag
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
        //createFragment.onClickCategoryListener(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        if(guidesFragment!=null&&selectedFragment==guidesFragment){
            guidesFragment.onBackFragment();
            guidesFragment.getAllGuides();
        }
    }

    private void goToLoginActivity(){
        Intent loginIntent = new Intent(this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void showError(Exception e) {
        UserMessage userMessage = new UserMessage();
        userMessage.showErrorMessage(this,userMessage.categorizeException(e));
    }

    /**
     *
     */
    abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        private SparseArray<Fragment> registeredFragments = new SparseArray<>(3);

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
