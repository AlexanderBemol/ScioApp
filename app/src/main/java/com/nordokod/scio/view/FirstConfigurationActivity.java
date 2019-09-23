package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import android.widget.Gallery;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.nordokod.scio.constants.RequestCode;
import com.nordokod.scio.constants.Utilities;
import com.nordokod.scio.controller.FirstConfigurationController;
import com.nordokod.scio.entity.AppConstants;
import com.nordokod.scio.entity.Error;
import com.nordokod.scio.R;
//import com.soundcloud.android.crop.Crop;
import com.nordokod.scio.entity.InputDataException;
import com.nordokod.scio.entity.OperationCanceledException;
import com.nordokod.scio.model.User;
import com.nordokod.scio.process.ExceptionManager;
import com.soundcloud.android.crop.Crop;
import com.victor.loading.newton.NewtonCradleLoading;

import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.util.logging.ErrorManager;

import me.relex.circleindicator.CircleIndicator;

public class FirstConfigurationActivity extends AppCompatActivity implements BasicActivity {
    //==============================================================================================
    // VARIABLES Y OBJETOS
    //==============================================================================================
    private final int NUMBER_PAGES = 5;
    private boolean isActivityCreated = false;

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
    //private FirstConfigurationAppBlockFragment      appBlockFragment;
    private FirstConfigurationCompleteFragment      completeFragment;

    private NewtonCradleLoading loading;
    /**
     * Obejeto del controlador perteneciente a esta Activity.
     */
    //private FirstConfigurationController firstConfigurationController;
    private User userModel;

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
        //appBlockFragment    = new FirstConfigurtionAppBlockFragment();
        completeFragment    = new FirstConfigurationCompleteFragment();

        viewPager.setAdapter(fragmentStatePagerAdapter);
        circleIndicator.setViewPager(viewPager);

        photoFragment.configFragment(this);
        nameFragment.configFragment(this);
        //birthdayFragment.configFragment(firstConfigurationController);
        //educationFragment.configFragment(firstConfigurationController);
        //appBlockFragment.configAdapter(firstConfigurationController,this);
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

                int screen_number = viewPager.getCurrentItem();
                switch (screen_number) {
                    case 0:
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        break;
                    /*case 4:
                        isAppBlockSkip = true;
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        break;
                    case 5:
                        saveConfiguration();
                        break;*/
                    default:
                        break;
                }
            }
        });

        BTN_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int screen_number = viewPager.getCurrentItem();
                if(screen_number==2)
                    nameFragment.updateName();

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
                    /*case 4: // Pantalla para las apps bloqueadas.
                        BTN_Next.setText(R.string.txt_btnDone);

                        BTN_Skip.setVisibility(View.VISIBLE);

                        break;
                    case 5: // Pantalla para finalizar.
                        BTN_Next.setVisibility(View.GONE);
                        if (validateFields(nameFragment.getName(), birthdayFragment.getBirthday(), educationFragment.getEducation()))
                            completeFragment.onComplete();
                        else
                            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                        BTN_Skip.setText(R.string.txt_btnDone);
                        break;*/
                    case 4:
                        BTN_Next.setVisibility(View.GONE);
                        //validar
                        break;
                    default:
                        BTN_Skip.setText(R.string.txt_BTN_Skip);
                        BTN_Skip.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case 2:
                        if (validateUsername(nameFragment.getName()))
                            nameFragment.updateName();
                        else
                            showError(new InputDataException(InputDataException.Code.INVALID_USERNAME));
                        break;
                    case 3:

                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }


    public void showErrorNoticeDialog(Error error) {

    }

    public void validatePage(int index){

    }


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
                case 4:     return completeFragment;
                //case 4:     return appBlockFragment;
                //case 5:     return completeFragment;
                default:    return null;
            }
        }

        @Override
        public int getCount() {
            return NUMBER_PAGES;
        }
    };

    /**
     * Validar el nombre de usuario
     * @param username nombre de usuario
     * @return true=valido
     */
    private boolean validateUsername(String username){
        return Utilities.USER_REGULAR_EXPRESSION.matches(username);
    }

    private boolean validateBirthday(Date date){
        return date.before(new Date());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestCode.RC_GALLERY.getCode()) {
                try{
                    Uri selectedImage = data.getData();
                    String pictureFile = "userPhoto-" +userModel.getBasicUserInfo().getUid()+".jpg";
                    File storageDir = getFilesDir();
                    File file=new File(storageDir,pictureFile);    //se crea archivo local para almacenar foto del resultado
                    if(file.exists()){
                        file.delete();
                    }
                    Crop.of(selectedImage,Uri.fromFile(file)).asSquare().withMaxSize(512,512) .start(this);
                }catch (Exception e){
                    showError(e);
                }
            } else if (requestCode == Crop.REQUEST_CROP) {
                try{
                    Uri resultUri = Crop.getOutput(data);
                    userModel.uploadProfilePhoto(resultUri,this).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            photoFragment.setDefaultPhoto();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            showError(new OperationCanceledException());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showError(e);
                        }
                    });
                }catch (Exception e){
                    showError(e);
                }
            }
        }
    }

    public void showError(Exception e){
        ExceptionManager exceptionManager=new ExceptionManager();
        exceptionManager.showErrorMessage(this,exceptionManager.categorizeException(e),noticeDialog);
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private void dismissProgressDialog() {
        if (noticeDialog != null && noticeDialog.isShowing()) {
            noticeDialog.dismiss();
        }
    }

}
