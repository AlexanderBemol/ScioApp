package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.nordokod.scio.entity.Error;
import com.nordokod.scio.R;

import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;

public class FirstConfigurationActivity extends AppCompatActivity implements BasicActivity {
    //==============================================================================================
    // VARIABLES Y OBJETOS
    //==============================================================================================
    private final int NUMBER_PAGES = 6;
    private boolean isActivityCreated = false;
    private boolean isPhotoSkip = false;
    private boolean isAppBlockSkip = false;

    private AppCompatButton BTN_Skip, BTN_Next;

    private Dialog noticeDialog;

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;

    /**
     * Objetos de los Fragment de cada pantalla.
     */
    private FirstConfigurationPhotoFragment         photoFragment;
    private FirstConfigurationNameFragment          nameFragment;
    private FirstConfigurationBirthdayFragment      birthdayFragment;
    private FirstConfigurationEducationFragment     educationFragment;
    private FirstConfigurationAppBlockFragment      appBlockFragment;
    private FirstConfigurationCompleteFragment      completeFragment;

    /**
     * Obejeto del controlador perteneciente a esta Activity.
     */
    //private FirstConfigurationController firstConfigurationController;

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
    @Override
    public void initComponents(){
        viewPager           = findViewById(R.id.viewPager);
        circleIndicator     = findViewById(R.id.pageIndicator);
        BTN_Skip            = findViewById(R.id.BTN_Skip);
        BTN_Next            = findViewById(R.id.BTN_Next);

        photoFragment       = new FirstConfigurationPhotoFragment();
        nameFragment        = new FirstConfigurationNameFragment();
        birthdayFragment    = new FirstConfigurationBirthdayFragment();
        educationFragment   = new FirstConfigurationEducationFragment();
        appBlockFragment    = new FirstConfigurationAppBlockFragment();
        completeFragment    = new FirstConfigurationCompleteFragment();

        viewPager.setAdapter(fragmentStatePagerAdapter);
        circleIndicator.setViewPager(viewPager);

        //firstConfigurationController = new FirstConfigurationController();
        //firstConfigurationController.configController(this, this, this);

        //photoFragment.configFragment(firstConfigurationController);
        //birthdayFragment.configFragment(firstConfigurationController);
        //educationFragment.configFragment(firstConfigurationController);
        //appBlockFragment.configAdapter(firstConfigurationController, this);
        //appBlockFragment.configAdapter(firstConfigurationController, this);

        isActivityCreated = true;
    }

    //==============================================================================================
    // LISTENERS
    //==============================================================================================
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initListeners(){
        /*
          Listener de BTN_Skip que según la pantalla en la que se encuentre omitirá o mandará
          a verificar los datos.
         */
        BTN_Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                int screen_number = viewPager.getCurrentItem();
                switch (screen_number) {
                    case 0:
                        isPhotoSkip = true;
                        photoFragment.skip();
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        break;
                    case 4:
                        isAppBlockSkip = true;
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        break;
                    case 5:
                        saveConfiguration();
                        break;
                    default:
                        break;
                }
            }
        });

        BTN_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int screen_number = viewPager.getCurrentItem();
                if (screen_number == 5)
                    saveConfiguration();

                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

        /*
          Listener para detectar cuál pantalla se está viendo y en base a eso cambiar el texto del
          botón, y ocultarlo o mostrarlo.
         */
        circleIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                BTN_Next.setVisibility(View.VISIBLE);
                BTN_Skip.setText(R.string.txt_BTN_Skip);
                BTN_Next.setText(R.string.txt_BTN_Next);

                switch (position) {
                    case 0: // Pantalla para la foto.
                        BTN_Skip.setVisibility(View.VISIBLE);
                        break;
                    case 4: // Pantalla para las apps bloqueadas.
                        BTN_Next.setText(R.string.txt_BTN_Done);

                        BTN_Skip.setVisibility(View.VISIBLE);
                        break;
                    case 5: // Pantalla para finalizar.
                        BTN_Next.setVisibility(View.GONE);

                        BTN_Skip.setText(R.string.txt_BTN_Done);
                        break;
                    default:
                        BTN_Skip.setText(R.string.txt_BTN_Skip);
                        BTN_Skip.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 4)
                    if (validateFields(nameFragment.getName(), birthdayFragment.getBirthday(), educationFragment.getEducation()))
                        completeFragment.onComplete();
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    @Override
    public void showErrorNoticeDialog(Error error) {
        if (noticeDialog == null)
            noticeDialog = new Dialog(this);
        else if (noticeDialog.isShowing()) {
            noticeDialog.dismiss();
        }

        noticeDialog.setContentView(R.layout.dialog_error);
        Objects.requireNonNull(noticeDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        noticeDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        noticeDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        noticeDialog.getWindow().getAttributes().windowAnimations = R.style.NoticeDialogAnimation;

        AppCompatTextView errorMessage  = noticeDialog.findViewById(R.id.TV_Message);
        AppCompatImageView image        = noticeDialog.findViewById(R.id.IV_Error);

        if (error.getDescriptionResource() != 0) {
            AppCompatTextView errorDescription = noticeDialog.findViewById(R.id.TV_Description);
            errorDescription.setVisibility(View.VISIBLE);
            errorDescription.setText(error.getDescriptionResource());
        }

        switch (error.getType()) {
            case Error.GENERAL:
                image.setVisibility(View.GONE);
                errorMessage.setText(R.string.message_error);
                break;
            case Error.EMPTY_FIELD:
                image.setVisibility(View.GONE);
                errorMessage.setText(R.string.message_emptyfields_error);
                break;
            case Error.GUY_FROM_THE_FUTURE:
                image.setVisibility(View.GONE);
                errorMessage.setText(R.string.message_from_the_future_error);
                break;
            case Error.WHEN_SAVING_ON_DEVICE:
                errorMessage.setText(R.string.message_save_error);
                break;
            case Error.WHEN_SAVING_ON_DATABASE:
                errorMessage.setText(R.string.message_save_error);
                break;
            case Error.CONNECTION:
                errorMessage.setText(R.string.message_connection_error);
                break;
            case Error.MAXIMUM_NUMBER_OF_APPS_REACHED:
                errorMessage.setText(R.string.message_max_apps_reached_error);
                break;

            default:
                image.setVisibility(View.GONE);
                errorMessage.setText(R.string.message_error);
                break;
        }

        noticeDialog.show();

        Handler handler;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                if (noticeDialog != null) {
                    noticeDialog.cancel();
                    noticeDialog.dismiss();
                    noticeDialog = null;
                }
            }
        }, 1500);

        handler = null;
    }

    @Override
    public void showSuccessNoticeDialog(String task) {
        if (noticeDialog == null)
            noticeDialog = new Dialog(this);
        else if (noticeDialog.isShowing()) {
            noticeDialog.dismiss();
        }

        noticeDialog.setContentView(R.layout.dialog_success);
        noticeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        noticeDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        noticeDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        noticeDialog.getWindow().getAttributes().windowAnimations = R.style.NoticeDialogAnimation;

        AppCompatTextView message = noticeDialog.findViewById(R.id.TV_Message);

        switch (task) {
            case "SAVE": message.setText(R.string.message_save_success); break;
            case "SET_PHOTO": message.setText(R.string.message_set_photo_success); break;
            default: message.setText(R.string.message_save_success); break;
        }

        noticeDialog.show();

        completeFragment.onComplete();

        Handler handler;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                noticeDialog.cancel();
                noticeDialog.dismiss();
                noticeDialog = null;

                finish();
            }
        }, 2000);

        handler = null;
    }

    //==============================================================================================
    // PAGE ADAPTER
    // ------------
    // Return de la pantalla según la posición que tenga en ese momento el adaptador.
    //==============================================================================================
    private FragmentStatePagerAdapter fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:     return photoFragment;
                case 1:     return nameFragment;
                case 2:     return birthdayFragment;
                case 3:     return educationFragment;
                case 4:     return appBlockFragment;
                case 5:     return completeFragment;
                default:    return null;
            }
        }

        @Override
        public int getCount() {
            return NUMBER_PAGES;
        }
    };

    /**
     *  Método que manda a guardar los datos del usuario.
     */
    private void saveConfiguration() {
        /*
        if (validateFields(nameFragment.getName(), birthdayFragment.getBirthday(), educationFragment.getEducation())) {
            if (isPhotoSkip && isAppBlockSkip)
                //firstConfigurationController.saveConfiguration(nameFragment.getName(), birthdayFragment.getBirthday(), educationFragment.getEducation());
            else if (isPhotoSkip)
                //firstConfigurationController.saveConfiguration(nameFragment.getName(), birthdayFragment.getBirthday(), educationFragment.getEducation(), true);
            else if (isAppBlockSkip)
                //firstConfigurationController.saveConfiguration(true, nameFragment.getName(), birthdayFragment.getBirthday(), educationFragment.getEducation());
            else
                //firstConfigurationController.saveConfiguration(true, nameFragment.getName(), birthdayFragment.getBirthday(), educationFragment.getEducation(), true);
        }
        */
    }

    /**
     * Método que válida si el usuario introdujo los datos necesarios.
     *
     * @param name          String - Nombre tecleado por el usuario en la pantalla 1.
     * @param birthday      String - Fecha elegida por el usuario en la pantalla 2.
     * @param education     Object - Escolaridad elegida por el usuario en la pantalla 3.
     *
     * @return  true    = Todos los datos son válidos.
     *          false   = Al menos un dato no fué válido.
     */
    private boolean validateFields(String name, String birthday, Object education) {
        //return firstConfigurationController.validateFields(name, birthday, img_education);
        return true;
    }

    /**
     * Método que cambia el estado si el usuario no pusó foto o si lo hizo.
     *
     * @param state
     */
    protected void isPhotoSkip(boolean state) {
        isPhotoSkip = state;
    }

    /**
     * Método que cambia el estado si el usuaio eligió aplicaciones para bloquear o no lo hizo.
     *
     * @param state
     */
    protected void isAppBlockSkip(boolean state) {
        isAppBlockSkip = state;
    }

}
