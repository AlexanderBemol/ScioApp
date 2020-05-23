package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentTransaction;

import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.nordokod.scio.R;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.OperationCanceledException;
import com.nordokod.scio.model.User;
import com.nordokod.scio.process.DownloadImageProcess;
import com.nordokod.scio.process.MediaProcess;
import com.nordokod.scio.process.UpdateCheck;
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
    private BottomNavigationView bottomNavigation;
    private NavigationView navigationMenu;

    private CircleImageView CIV_Photo;
    private AppCompatTextView TV_Name, BTN_Logout;

    private NewGuideFragment newGuideFragment;
    private GuidesFragment guidesFragment;
    private DialogFragment dialogFragment;
    private Fragment selectedFragment = null;
    private User userModel;
    private com.nordokod.scio.entity.User actualUserEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.NightTheme);
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
        MenuItems           = navigationMenu.getMenu();

        bottomNavigation    = findViewById(R.id.NAV_Bar);

        BTN_Logout      = navigationMenu.findViewById(R.id.BTN_Logout);

        View header     = navigationMenu.getHeaderView(0);

        CIV_Photo       = header.findViewById(R.id.CIV_Photo);
        TV_Name         = header.findViewById(R.id.TV_Name);


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

        //verificar update
        
        UpdateCheck updateCheck = new UpdateCheck();
        updateCheck.checkUpdateAvailability(this,this);

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
        Bitmap localPhoto = userModel.getLocalProfilePhoto(getApplicationContext(), actualUserEntity);
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
            com.nordokod.scio.entity.User user = new com.nordokod.scio.entity.User();
            user = userModel.getBasicUserInfo();
            userModel.getUserInformation(user)
                    .addOnSuccessListener(documentSnapshot -> TV_Name.setText(userModel.getUserFromDocument(documentSnapshot).getUsername()))
                    .addOnFailureListener(this::showError);
        } catch (Exception e) {
            showError(e);
        }

        openFragmentOfBottomNavigationBar(new HomeFragment(this, this));
    }

    @Override
    public void initListeners() {
        bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.Menu_New_Guide:
                    openFragmentOfBottomNavigationBar(new CreateFragment());
                    showFragmentToCreateNewGuide();
                    return true;
                case R.id.Menu_Guides:
                    openFragmentOfBottomNavigationBar(new GuidesFragment(this, this));
                    return true;
                case R.id.Menu_Home: default:
                    openFragmentOfBottomNavigationBar(new HomeFragment(this, this));
                    return true;
            }
        });

        bottomNavigation.setOnNavigationItemReselectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.Menu_New_Guide) {
                openFragmentOfBottomNavigationBar(new CreateFragment());
                showFragmentToCreateNewGuide();
            }
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
                Intent intent = new Intent(MainActivity.this, LockedAppsOptionActivity.class);
                startActivity(intent);
                finish();
                return true;
        });
        /*MenuItems.findItem(R.id.Menu_About).setOnMenuItemClickListener(item -> {

        });*/
    }

    /**
     * Método para mostrar el fragment de la opción seleccionada en la barra de navegación principal.
     * @param fragment
     */
    private void openFragmentOfBottomNavigationBar(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.VP_Main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Método para mostrar el Fragment para crear una nueva guia.
     */
    private void showFragmentToCreateNewGuide() {
        dialogFragment = new NewGuideFragment(this, this);
        dialogFragment.show(getSupportFragmentManager(), "New Guide");
    }

    /**
     * Método para mostrar el Fragment para editar una guia.
     * @param guide entidad de la guia que se quiere editar.
     */
    protected void showFragmentToEditGuide(Guide guide) {
        dialogFragment = new EditGuideFragment(this, this, guide);
        dialogFragment.show(getSupportFragmentManager(), "Edit Guide");
    }

    /**
     * Método para mostrar el Fragment para elegir el tipo de pregunta que se quiere agregar.
     * @param guide entidad de la guia a la que se quiere agregar la pregunta.
     */
    public void showNewQuestionDialog(Guide guide) {
        dialogFragment = new NewQuestionFragment(this, this, guide);
        dialogFragment.show(getSupportFragmentManager(), "New Question");
    }

    /**
     * Método para inicializar Fragment para la pregunta de tipo Opción muultiple y mostrarlo.
     * @param guide entidad de la guía
     */
    public void showNewMultipleChoiceQuestionDialog(Guide guide) {
        dialogFragment = new NewMultipleChoiceQuestionFragment(this, this, guide);
        dialogFragment.show(getSupportFragmentManager(), "New Multiple Choice");
    }

    /**
     * Método para inicializar Fragment para la pregunta de tipo Respuesta abierta y mostrarlo.
     * @param guide entidad de la guía
     */
    public void showNewOpenAnswerQuestionDialog(Guide guide) {
        dialogFragment = new NewOpenAnswerQuestionFragment(this, this, guide);
        dialogFragment.show(getSupportFragmentManager(), "New Open Answer");
    }

    /**
     * Método para inicializar Fragment para la pregunta de tipo Verdadero/Falso y mostrarlo.
     * @param guide entidad de la guía
     */
    public void showNewTrueFalseQuestionDialog(Guide guide) {
        dialogFragment = new NewTrueFalseQuestionFragment(this, this, guide);
        dialogFragment.show(getSupportFragmentManager(), "New TrueFalse");
    }

    /**
     * Método para cerrar Fragments según su tag.
     * @param tag tag
     */
    public void onCloseFragment(String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(Objects
                        .requireNonNull(getSupportFragmentManager()
                                .findFragmentByTag(tag)))
                .commit();
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
            //guidesFragment.onBackFragment();
            guidesFragment.getAllGuides();
        }
    }

    private void goToLoginActivity(){
        Intent loginIntent = new Intent(this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    public void showError(Exception e) {
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

        BottomBarAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        void addFragment(Fragment fragment) {
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

    @Override
    public void onBackPressed() {
        // Empty
    }
}
