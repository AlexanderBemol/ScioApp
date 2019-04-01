package com.nordokod.scio.View;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.nordokod.scio.R;

public class FirstConfigurationBirthdayFragment extends Fragment implements BasicFragment{

    private AppCompatEditText ET_Birthday;

    //private FirstConfigurationController firstConfigurationController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle s) {
        View view = inflater.inflate(R.layout.birthday_first_configuration, container,false);
        initComponents(view);
        initListeners();

        return view;
    }

    @Override
    public void initComponents(View view) {
        ET_Birthday = view.findViewById(R.id.ET_Birthday);
    }

    @Override
    public void initListeners() {
        ET_Birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }
/*
    /**
     * Método para configurar este Fragment con el objeto del controlador usado por el Activity.
     * /
    protected void configFragment(FirstConfigurationController controller) {
        this.firstConfigurationController = controller;
    }
*/
    /**
     * Método que actualiza la fecha de nacimineto del usuario en Firebase.
     */
    protected void updateBirthday() {
        //firstConfigurationController.updateBirthday(ET_Birthday.getText().toString());
    }

    /**
     * Método para mostrar el DatePickerDialog y cargar la fecha elegida en el ET_Birthday
     */
    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final String selectedDate = twoDigits(dayOfMonth) + "/" + twoDigits(month + 1) + "/" + year;
                ET_Birthday.setText(selectedDate);
            }
        });

        datePickerFragment.show(getActivity().getFragmentManager(), "datePicker");
    }

    /**
     * Método para agregar un 0 en caso de ser número de un digito.
     *
     * @param number
     * @return el número con un '0' al inicio o son modificar.
     */
    private String twoDigits(int number) {
        return (number < 10) ? ("0" + String.valueOf(number)) : String.valueOf(number);
    }

    public String getBirthday() {
        return ET_Birthday.getText().toString();
    }
}
