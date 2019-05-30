package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nordokod.scio.R;
import com.nordokod.scio.controller.MainController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.nordokod.scio.R.attr.iconNormalColor;
import static com.nordokod.scio.R.attr.iconSelectedColor;

public class NewGuideFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private MainController mainController;
    AppCompatEditText ET_Topic;
    private LinearLayout LL_Date, LL_Time, LL_Categories;
    private AppCompatTextView TV_Month, TV_Day, TV_Time, TV_Hour;
    private AppCompatButton BTN_Cancel, BTN_Create;
    private int preview_Category_View_Selected = 0, category_selected_id;
    private String date_selected, time_selected;
    // Animations
    private Animation press;
    private ConstraintLayout CL_Exacts,CL_Socials,CL_Sports,CL_Art,CL_Tech,CL_Entertainment,CL_Others;

    public NewGuideFragment() { }

    // TODO: Agregar al constructor el parametro del controllador
    @SuppressLint("ValidFragment")
    public NewGuideFragment(Context context, MainController mc) {
        this.context = context;
        this.mainController=mc;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_guide, container, false);

        initComponents(view);
        initListeners();
        initAnimations();

        return view;
    }

    @Override
    public void initComponents(View view) {
        LL_Date         = view.findViewById(R.id.LL_Date);
        LL_Time         = view.findViewById(R.id.LL_Time);
        LL_Categories   = view.findViewById(R.id.LL_Categories);
        TV_Day          = view.findViewById(R.id.TV_Day);
        TV_Month        = view.findViewById(R.id.TV_Month);
        TV_Time         = view.findViewById(R.id.TV_Time);
        TV_Hour         = view.findViewById(R.id.TV_Hour);
        BTN_Cancel      = view.findViewById(R.id.BTN_Cancel);
        BTN_Create      = view.findViewById(R.id.BTN_Create);
        ET_Topic        = view.findViewById(R.id.ET_Topic);
        ET_Topic.setText("");
        //
        CL_Exacts = view.findViewById(R.id.CL_Exacts);
        CL_Socials = view.findViewById(R.id.CL_Socials);
        CL_Sports = view.findViewById(R.id.CL_Sports);
        CL_Art = view.findViewById(R.id.CL_Art);
        CL_Tech = view.findViewById(R.id.CL_Tech);
        CL_Entertainment = view.findViewById(R.id.CL_Entertainment);
        CL_Others = view.findViewById(R.id.CL_Others);
        //
        Calendar calendar = Calendar.getInstance();
        TV_Month.setText(getMonthName(calendar.get(Calendar.MONTH)));
        TV_Day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        date_selected = twoDigits(calendar.get(Calendar.DAY_OF_MONTH))+"/"+twoDigits(calendar.get(Calendar.MONTH)+1)+"/"+twoDigits(calendar.get(Calendar.YEAR));
        //
        int minute = Integer.parseInt(twoDigits(calendar.get(Calendar.MINUTE)));
        String hour = twoDigits(calendar.get(Calendar.HOUR)) + ":" + twoDigits(minute);
        TV_Hour.setText(hour);
        TV_Time.setText((calendar.get(Calendar.AM_PM) < 1) ? "AM" : "PM");
        time_selected = hour;
    }

    @Override
    public void initListeners() {
        LL_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        LL_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        BTN_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Cancel.startAnimation(press);
                Objects.requireNonNull(getFragmentManager()).beginTransaction().remove(NewGuideFragment.this).commit();

            }
        });

        BTN_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Create.startAnimation(press);
                mainController.createGuide(category_selected_id, ET_Topic.getText(), date_selected, time_selected);
            }
        });
        //
        CL_Exacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v);
            }
        });
        CL_Socials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v);
            }
        });
        CL_Sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v);
            }
        });
        CL_Art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v);
            }
        });
        CL_Tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v);
            }
        });
        CL_Entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v);
            }
        });
        CL_Others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v);
            }
        });
    }

    @SuppressLint("ResourceType")
    public void onClickCategoryListener(View view) {
        AppCompatImageView categoryIcon = view.findViewById(getCategoryImageViewId(view.getId()));

        if (preview_Category_View_Selected != view.getId()) {
            if(categoryIcon != null) {
                TypedValue typedValue = new TypedValue();
                Objects.requireNonNull(getActivity()).getTheme().resolveAttribute(iconSelectedColor, typedValue, true);

                categoryIcon.setColorFilter(typedValue.data);
            }

            category_selected_id = getCategoryId(view.getId());

            // A la categoria anterior se le devuelve su color de icono normal
            if(preview_Category_View_Selected != 0) {
                categoryIcon = LL_Categories.findViewById(getCategoryImageViewId(preview_Category_View_Selected));

                TypedValue typedValue = new TypedValue();
                Objects.requireNonNull(getActivity()).getTheme().resolveAttribute(iconNormalColor, typedValue, true);

                categoryIcon.setColorFilter(typedValue.data);
            }

            preview_Category_View_Selected = view.getId();
        }
    }

    private int getCategoryId(int view_selected_id) {
        switch (view_selected_id) {
            case R.id.CL_Exacts:        return 1;
            case R.id.CL_Socials:       return 2;
            case R.id.CL_Sports:        return 3;
            case R.id.CL_Art:           return 4;
            case R.id.CL_Tech:          return 5;
            case R.id.CL_Entertainment: return 6;
            case R.id.CL_Others:        return 7;
            default:                    return 7;
        }
    }

    private int getCategoryImageViewId(int view_id) {
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

    /**
     * Método para agregar un 0 en caso de ser número de un digito.
     *
     * @param number
     * @return el número con un '0' al inicio o son modificar.
     */
    private String twoDigits(int number) {
        return (number < 10) ? ("0" + String.valueOf(number)) : String.valueOf(number);
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                TV_Month.setText(getMonthName(month));
                TV_Day.setText(twoDigits(dayOfMonth));

                date_selected = twoDigits(dayOfMonth) + "/" + twoDigits(month + 1) + "/" + year;
            }
        });

        datePickerFragment.show(Objects.requireNonNull(getActivity()).getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TV_Time.setText((hourOfDay < 12) ? "AM" : "PM");
                time_selected = twoDigits(hourOfDay) + ":" +twoDigits(minute);

                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat12 = new SimpleDateFormat("h:mm");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat24 = new SimpleDateFormat("HH:mm");
                Date date = null;
                try {
                    date = simpleDateFormat24.parse(hourOfDay + ":" + minute);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String hour = simpleDateFormat12.format(date);
                TV_Hour.setText(hour);
            }
        });

        timePickerFragment.show(Objects.requireNonNull(getActivity()).getFragmentManager(), "timePicker");
    }

    private int getMonthName(int month) {
        switch (month) {
            case 0:     return R.string.month_january;
            case 1:     return R.string.month_february;
            case 2:     return R.string.month_march;
            case 3:     return R.string.month_april;
            case 4:     return R.string.month_may;
            case 5:     return R.string.month_june;
            case 6:     return R.string.month_july;
            case 7:     return R.string.month_august;
            case 8:     return R.string.month_september;
            case 9:     return R.string.month_october;
            case 10:    return R.string.month_november;
            case 11:    return R.string.month_december;
            default:    return R.string.month_january;
        }
    }

    private void initAnimations(){
        press = AnimationUtils.loadAnimation(context, R.anim.press);
    }
}
