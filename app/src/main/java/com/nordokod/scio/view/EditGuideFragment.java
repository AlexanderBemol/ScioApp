package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nordokod.scio.R;
import com.nordokod.scio.constants.UserOperations;
import com.nordokod.scio.constants.Utilities;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.process.UserMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.nordokod.scio.R.attr.iconNormalColor;
import static com.nordokod.scio.R.attr.iconSelectedColor;

public class EditGuideFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private MainActivity mainActivity;
    private Guide guide;
    private Animation press_button;
    private Date date_selected;
    private int preview_Category_View_Selected = 0, category_selected_id;
    private AppCompatTextView FEditGuide_TV_Month;
    private AppCompatTextView FEditGuide_TV_Day;
    private AppCompatTextView FEditGuide_TV_Time;
    private AppCompatTextView FEditGuide_TV_Hour;
    private AppCompatEditText FEditGuide_ET_Topic;
    private AppCompatButton FEditGuide_BTN_Save;
    private AppCompatButton FEditGuide_BTN_Cancel;
    private LinearLayout FEditGuide_LL_Categories;
    private LinearLayout FEditGuide_LL_Date;
    private LinearLayout FEditGuide_LL_Time;
    private ConstraintLayout FEditGuide_CL_Exacts;
    private ConstraintLayout FEditGuide_CL_Socials;
    private ConstraintLayout FEditGuide_CL_Sports;
    private ConstraintLayout FEditGuide_CL_Art;
    private ConstraintLayout FEditGuide_CL_Tech;
    private ConstraintLayout FEditGuide_CL_Entertainment;
    private ConstraintLayout FEditGuide_CL_Others;

    public EditGuideFragment() {
        // Required empty public constructor
    }

    public EditGuideFragment(Context context, MainActivity mainActivity, Guide guide) {
        this.context = context;
        this.mainActivity = mainActivity;
        this.guide = guide;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_guide, container, false);

        initComponents(view);
        initListeners();
        initAnimations();

        return view;
    }

    @Override
    public void initComponents(View view) {
        FEditGuide_TV_Month         = view.findViewById(R.id.FEditGuide_TV_Month);
        FEditGuide_TV_Day           = view.findViewById(R.id.FEditGuide_TV_Day);
        FEditGuide_TV_Time          = view.findViewById(R.id.FEditGuide_TV_Time);
        FEditGuide_TV_Hour          = view.findViewById(R.id.FEditGuide_TV_Hour);

        FEditGuide_ET_Topic         = view.findViewById(R.id.FEditGuide_ET_Topic);

        FEditGuide_BTN_Save         = view.findViewById(R.id.FEditGuide_BTN_Save);
        FEditGuide_BTN_Cancel       = view.findViewById(R.id.FEditGuide_BTN_Cancel);

        FEditGuide_LL_Date          = view.findViewById(R.id.FEditGuide_LL_Date);
        FEditGuide_LL_Time          = view.findViewById(R.id.FEditGuide_LL_Time);
        FEditGuide_LL_Categories    = view.findViewById(R.id.LL_Categories);
        FEditGuide_CL_Exacts        = view.findViewById(R.id.CL_Exacts);
        FEditGuide_CL_Socials       = view.findViewById(R.id.CL_Socials);
        FEditGuide_CL_Sports        = view.findViewById(R.id.CL_Sports);
        FEditGuide_CL_Art           = view.findViewById(R.id.CL_Art);
        FEditGuide_CL_Tech          = view.findViewById(R.id.CL_Tech);
        FEditGuide_CL_Entertainment = view.findViewById(R.id.CL_Entertainment);
        FEditGuide_CL_Others        = view.findViewById(R.id.CL_Others);
    }

    @Override
    public void initListeners() {
        FEditGuide_LL_Date.setOnClickListener(v -> showDatePickerDialog());
        FEditGuide_LL_Time.setOnClickListener(v -> showTimePickerDialog());

        FEditGuide_BTN_Save.setOnClickListener(v -> {
            FEditGuide_BTN_Save.startAnimation(press_button);
            //TODO: Agregar la logica
        });

        FEditGuide_BTN_Cancel.setOnClickListener(v -> {
            FEditGuide_BTN_Cancel.startAnimation(press_button);
            Objects.requireNonNull(getFragmentManager()).beginTransaction().remove(EditGuideFragment.this).commit();
        });

        FEditGuide_CL_Exacts.setOnClickListener(this::onClickCategoryListener);
        FEditGuide_CL_Socials.setOnClickListener(this::onClickCategoryListener);
        FEditGuide_CL_Sports.setOnClickListener(this::onClickCategoryListener);
        FEditGuide_CL_Art.setOnClickListener(this::onClickCategoryListener);
        FEditGuide_CL_Tech.setOnClickListener(this::onClickCategoryListener);
        FEditGuide_CL_Entertainment.setOnClickListener(this::onClickCategoryListener);
        FEditGuide_CL_Others.setOnClickListener(this::onClickCategoryListener);
    }

    private void initAnimations(){
        press_button = AnimationUtils.loadAnimation(context, R.anim.press);
    }

    @Override
    public void onStart() {
        setGuideCategorySelected(guide.getCategory());
        FEditGuide_ET_Topic.setText(guide.getTopic());

        date_selected = guide.getTestDatetime();
        FEditGuide_TV_Month.setText(Utilities.getMonthNameFromDate(date_selected));
        FEditGuide_TV_Day.setText(Utilities.getTwoDigitsFromDate(date_selected, Calendar.DAY_OF_MONTH));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date_selected);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat12 = new SimpleDateFormat("hh:mm");
        FEditGuide_TV_Hour.setText(simpleDateFormat12.format(date_selected));
        FEditGuide_TV_Time.setText((calendar.get(Calendar.AM_PM) < 1) ? "AM" : "PM");

        super.onStart();
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);
            Date date = calendar.getTime();
            FEditGuide_TV_Month.setText(Utilities.getMonthNameFromDate(date));
            FEditGuide_TV_Day.setText(Utilities.getTwoDigitsFromDate(date,Calendar.DAY_OF_MONTH));
            date_selected = date;
        });
        datePickerFragment.show(Objects.requireNonNull(getActivity()).getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        TimePickerFragment timePickerFragment = TimePickerFragment.newInstance((view, hourOfDay, minute) -> {
            FEditGuide_TV_Time.setText((hourOfDay < 12) ? "AM" : "PM");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date_selected);
            calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat12 = new SimpleDateFormat("hh:mm");

            date_selected = calendar.getTime();
            String hour = simpleDateFormat12.format(date_selected);
            FEditGuide_TV_Hour.setText(hour);
        });

        timePickerFragment.show(Objects.requireNonNull(getActivity()).getFragmentManager(), "timePicker");
    }

    @SuppressLint("ResourceType")
    private void onClickCategoryListener(View view) {
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
                categoryIcon = FEditGuide_LL_Categories.findViewById(getCategoryImageViewId(preview_Category_View_Selected));

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

    private void setGuideCategorySelected(int guide_category_selected_id) {
        switch(guide_category_selected_id) {
            case 1: FEditGuide_CL_Exacts.performClick();        break;
            case 2: FEditGuide_CL_Socials.performClick();       break;
            case 3: FEditGuide_CL_Sports.performClick();        break;
            case 4: FEditGuide_CL_Art.performClick();           break;
            case 5: FEditGuide_CL_Tech.performClick();          break;
            case 6: FEditGuide_CL_Entertainment.performClick(); break;
            default: FEditGuide_CL_Others.performClick();       break;
        }

        category_selected_id = guide_category_selected_id;
    }

    private void showError(Exception exception){
        UserMessage userMessage = new UserMessage();
        userMessage.showErrorMessage(context, userMessage.categorizeException(exception));
    }

    private void showSuccessfullMessage(){
        UserMessage userMessage = new UserMessage();
        userMessage.showSuccessfulOperationMessage(getContext(), UserOperations.UPDATE_GUIDE);
    }
}
