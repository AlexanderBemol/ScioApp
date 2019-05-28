package com.nordokod.scio.view;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.widget.DatePicker;

import com.nordokod.scio.R;
import com.nordokod.scio.entity.Error;

public class EditProfileView extends AppCompatActivity implements BasicActivity{
    private AppCompatEditText  ProfileView_ET_birthday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_view);
    }

    @Override
    public void initComponents() {
        ProfileView_ET_birthday = findViewById(R.id.EditProfile_ET_Birthday);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void showErrorNoticeDialog(Error error) {

    }

    @Override
    public void showSuccessNoticeDialog(String task) {

    }
    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final String selectedDate = twoDigits(dayOfMonth) + "/" + twoDigits(month + 1) + "/" + year;

                ProfileView_ET_birthday.setText(selectedDate);
            }
        });

        datePickerFragment.show(getFragmentManager(), "datePicker");
    }
    private String twoDigits(int number) {
        return (number < 10) ? ("0" + String.valueOf(number)) : String.valueOf(number);
    }
}
