package com.nordokod.scio.constants;

import com.nordokod.scio.R;

import java.util.Calendar;
import java.util.Date;

public class Utilities {
    public static String USER_REGULAR_EXPRESSION="([A-Za-zÁÉÍÓÚáéíóúÄËÏÖÜäëïöüÑñ]+)([\\s]([A-Za-zÁÉÍÓÚáéíóúÄËÏÖÜäëïöüÑñ])+)*";
    public static String EMAIL_REGULAR_EXPRESSION="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    /**
     * Obtener el nombre del mes de una fecha
     * @param date Date de la fecha
     * @return value del recurso con el nombre del mes
     */
    public static int getMonthNameFromDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        switch (month){
            case Calendar.JANUARY: return R.string.month_january;
            case Calendar.FEBRUARY: return R.string.month_february;
            case Calendar.MARCH: return R.string.month_march;
            case Calendar.APRIL: return R.string.month_april;
            case Calendar.MAY: return R.string.month_may;
            case Calendar.JUNE: return R.string.month_june;
            case Calendar.JULY: return R.string.month_july;
            case Calendar.AUGUST: return R.string.month_august;
            case Calendar.SEPTEMBER: return R.string.month_september;
            case Calendar.OCTOBER: return R.string.month_october;
            case Calendar.NOVEMBER: return R.string.month_november;
            case Calendar.DECEMBER: return R.string.month_december;
        }
        return R.string.month_january;
    }

    /**
     * Obtener número con dos dígitos de una fecha
     * @param date fecha
     * @param calendarInt entero de Calendar a obtener
     * @return cadena con dos dígitos de fecha
     */
    public static String getTwoDigitsFromDate(Date date,int calendarInt){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int number = calendar.get(calendarInt);
        return number < 10 ? "0"+ number : String.valueOf(number);
    }

    /**
     * Obtener el res de la categoría
     * @param category int
     * @return int de res
     */
    public static int getStringFromCategory(int category){
        switch (category) {
            case 1:return R.string.category_exact_sciences;
            case 2:return R.string.category_social_sciences;
            case 3:return R.string.category_sports;
            case 4:return R.string.category_art;
            case 5:return R.string.category_tech;
            case 6:return R.string.category_entertainment;
            case 7:return R.string.category_others;
            default: return 0;
        }
    }
}
