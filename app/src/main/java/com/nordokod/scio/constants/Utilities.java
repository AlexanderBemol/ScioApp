package com.nordokod.scio.constants;

import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.nordokod.scio.R;
import com.nordokod.scio.view.DatePickerFragment;
import com.nordokod.scio.view.MainActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.nordokod.scio.R.attr.iconNormalColor;
import static com.nordokod.scio.R.attr.iconSelectedColor;

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
    public static int getCategoryStringResource(int category){
        switch (category) {
            case 1: return R.string.category_exact_sciences;
            case 2: return R.string.category_social_sciences;
            case 3: return R.string.category_sports;
            case 4: return R.string.category_art;
            case 5: return R.string.category_tech;
            case 6: return R.string.category_entertainment;
            case 7: return R.string.category_others;
            default: return 0;
        }
    }

    /**
     *
     * @param view_selected_id
     * @return
     */
    public static int getCategoryId(int view_selected_id) {
        switch (view_selected_id) {
            case R.id.CL_Exacts:        return 1;
            case R.id.CL_Socials:       return 2;
            case R.id.CL_Sports:        return 3;
            case R.id.CL_Art:           return 4;
            case R.id.CL_Tech:          return 5;
            case R.id.CL_Entertainment: return 6;
            default:                    return 7;
        }
    }

    @SuppressLint("ResourceType")
    public static int onClickCategoryListener(View view, MainActivity mainActivity, int preview_Category_View_Selected, LinearLayout LL_Categories) {
        if (preview_Category_View_Selected != view.getId()) {
            AppCompatImageView categoryIcon = view.findViewById(getCategoryImageViewId(view.getId()));

            if(categoryIcon != null) {
                TypedValue typedValue = new TypedValue();
                Objects.requireNonNull(mainActivity).getTheme().resolveAttribute(iconSelectedColor, typedValue, true);

                categoryIcon.setColorFilter(typedValue.data);
            }

            // A la categoria anterior se le devuelve su color de icono normal
            if(preview_Category_View_Selected != 0) {
                categoryIcon = LL_Categories.findViewById(getCategoryImageViewId(preview_Category_View_Selected));

                TypedValue typedValue = new TypedValue();
                Objects.requireNonNull(mainActivity).getTheme().resolveAttribute(iconNormalColor, typedValue, true);

                categoryIcon.setColorFilter(typedValue.data);
            }

            preview_Category_View_Selected = view.getId();
        }
        return preview_Category_View_Selected;
    }

    public static int getCategoryImageViewId(int view_id) {
        switch (view_id) {
            case R.id.CL_Exacts:        return R.id.IV_Exacts;
            case R.id.CL_Socials:       return R.id.IV_Socials;
            case R.id.CL_Sports:        return R.id.IV_Sports;
            case R.id.CL_Art:           return R.id.IV_Art;
            case R.id.CL_Tech:          return R.id.IV_Tech;
            case R.id.CL_Entertainment: return R.id.IV_Entertainment;
            case R.id.CL_Others:        return R.id.IV_Others;
            default:                    return 0;
        }
    }
}
