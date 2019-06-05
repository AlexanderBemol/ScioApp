package com.nordokod.scio.view;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.nordokod.scio.R;
import com.nordokod.scio.controller.MainController;
import com.nordokod.scio.entity.Error;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements BasicActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationMenu;
    private BottomNavigationView navigationBar;
    private FrameLayout frameLayout;
    private Toolbar toolbar;
    private ActionBar actionBar;

    private CircleImageView CIV_Photo;
    private AppCompatTextView TV_Name, BTN_Logout;

    private HomeFragment    homeFragment;
    private NewGuideFragment createFragment;
    private GuidesFragment  guidesFragment;
    private DialogFragment dialogFragment;
    private Fragment selectedFragment = null;


    private MainController mainController;

    private Dialog noticeDialog;
    private static final int NOTICE_DIALOG_TIME = 2000;

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

        mainController  = new MainController(this,this);
        homeFragment    = new HomeFragment(this, this);
        createFragment  = new NewGuideFragment(this,mainController );
        guidesFragment  = new GuidesFragment(this, mainController);

        BTN_Logout = navigationMenu.findViewById(R.id.BTN_Logout);

        View header=navigationMenu.getHeaderView(0);

        CIV_Photo = header.findViewById(R.id.CIV_Photo);
        TV_Name = header.findViewById(R.id.TV_Name);


        // Toolbar
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mainController.requestPhoto();

        TV_Name.setText(mainController.getName());
    }

    @Override
    public void initListeners() {
        navigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.NAV_Home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.NAV_Create:
                        dialogFragment = createFragment;

                        dialogFragment.show(getSupportFragmentManager(), "New");
                        break;
                    case R.id.NAV_Guides:
                        selectedFragment = guidesFragment;
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.FL_Main, selectedFragment).commit();

                return true;
            }
        });

        BTN_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.logOut();
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
        message.setText(R.string.message_save_success);

        noticeDialog.show();

        Handler handler;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                noticeDialog.cancel();
                noticeDialog.dismiss();
                noticeDialog = null;
            }
        }, 1000);

        handler = null;
        if(dialogFragment!=null)
            dialogFragment.dismiss();
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
        }else if (error.getDescriptionText() != null) {
            AppCompatTextView errorDescription = noticeDialog.findViewById(R.id.TV_Description);
            errorDescription.setVisibility(View.VISIBLE);
            errorDescription.setText(error.getDescriptionText());
        }

        switch (error.getType()) {
            case Error.EMPTY_FIELD:
                image.setVisibility(View.GONE);
                errorMessage.setText(R.string.message_emptyfields_error);
                break;
            case Error.CONNECTION:
                image.setVisibility(View.GONE);
                errorMessage.setText(R.string.message_connection_error);
                break;
            case  Error.GUY_FROM_THE_FUTURE:
                image.setVisibility(View.GONE);
                errorMessage.setText(R.string.message_from_the_future_error);
                break;
            case  Error.WHEN_SAVING_ON_DATABASE:
                image.setVisibility(View.GONE);
                errorMessage.setText("Error Guardando la guía");
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
        }, NOTICE_DIALOG_TIME);

        handler = null;
    }




    public void setUserPhoto(Bitmap photo){
        CIV_Photo.setImageBitmap(photo);
    }
    public void setDefaultUserPhoto(){
        CIV_Photo.setImageResource(R.drawable.default_photo);
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
