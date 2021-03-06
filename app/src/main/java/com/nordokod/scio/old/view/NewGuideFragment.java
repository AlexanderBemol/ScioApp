package com.nordokod.scio.old.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import com.nordokod.scio.R;
import com.nordokod.scio.old.constants.UserOperations;
import com.nordokod.scio.old.constants.Utilities;
import com.nordokod.scio.old.entity.Guide;
import com.nordokod.scio.old.entity.InputDataException;
import com.nordokod.scio.old.entity.OperationCanceledException;
import com.nordokod.scio.old.process.UserMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.nordokod.scio.R.attr.iconNormalColor;
import static com.nordokod.scio.R.attr.iconSelectedColor;

public class NewGuideFragment extends BottomSheetDialogFragment implements BasicFragment {

    private  MainActivity mainActivity;
    private Context context;
    //private MainController mainController;
    private AppCompatEditText ET_Topic;
    private LinearLayout LL_Date, LL_Time, LL_Categories;
    private AppCompatTextView TV_Month, TV_Day, TV_Time, TV_Hour;
    private AppCompatButton BTN_Cancel, BTN_Create;
    private int preview_Category_View_Selected = 0, category_selected_id;
    //private String date_selected, time_selected;
    private Date date_selected;
    // Animations
    private Animation press;
    private ConstraintLayout CL_Exacts, CL_Socials, CL_Sports, CL_Art, CL_Tech, CL_Entertainment, CL_Others;

    public NewGuideFragment() { }

    // TODO: Agregar al constructor el parametro del controllador
    @SuppressLint("ValidFragment")
    public NewGuideFragment(Context context, MainActivity mainActivity) {
        this.context = context;
        this.mainActivity = mainActivity;
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
        LL_Date             = view.findViewById(R.id.FNewGuide_LL_Date);
        LL_Time             = view.findViewById(R.id.FNewGuide_LL_Time);
        LL_Categories       = view.findViewById(R.id.LL_Categories);

        TV_Day              = view.findViewById(R.id.FNewGuide_TV_Day);
        TV_Month            = view.findViewById(R.id.FNewGuide_TV_Month);
        TV_Time             = view.findViewById(R.id.FNewGuide_TV_Time);
        TV_Hour             = view.findViewById(R.id.FNewGuide_TV_Hour);

        BTN_Cancel          = view.findViewById(R.id.FNewGuide_BTN_Cancel);
        BTN_Create          = view.findViewById(R.id.FNewGuide_BTN_Create);

        ET_Topic            = view.findViewById(R.id.FNewGuide_ET_Topic);

        CL_Exacts           = view.findViewById(R.id.CL_Exacts);
        CL_Socials          = view.findViewById(R.id.CL_Socials);
        CL_Sports           = view.findViewById(R.id.CL_Sports);
        CL_Art              = view.findViewById(R.id.CL_Art);
        CL_Tech             = view.findViewById(R.id.CL_Tech);
        CL_Entertainment    = view.findViewById(R.id.CL_Entertainment);
        CL_Others           = view.findViewById(R.id.CL_Others);
    }

    @Override
    public void initListeners() {
        LL_Date.setOnClickListener(v -> showDatePickerDialog());

        LL_Time.setOnClickListener(v -> showTimePickerDialog());

        BTN_Cancel.setOnClickListener(v -> {
            BTN_Cancel.startAnimation(press);
            ET_Topic.setText("");
            Objects.requireNonNull(getFragmentManager()).beginTransaction().remove(NewGuideFragment.this).commit();
        });

        BTN_Create.setOnClickListener(v -> {
            BTN_Create.startAnimation(press);
            if (category_selected_id == 0) {
                showError(new InputDataException(InputDataException.Code.EMPTY_FIELD)); // ===== No seleccion?? ninguna categor??a
            } else {
                if (Objects.requireNonNull(ET_Topic.getText()).length()==0) {
                    showError(new InputDataException(InputDataException.Code.EMPTY_FIELD)); // = No escribi?? el tema de la gu??a
                } else {
                    Date date = date_selected;
                    Date dateToday = new Date();

                    if (Objects.requireNonNull(date).before(dateToday)){
                        showError(new InputDataException(InputDataException.Code.DATETIME_BEFORE)); // La fecha elegida es del pasado. ??Oh por Dios, Doc, viajamos al pasado!
                    } else {
                        Guide guide = new Guide(category_selected_id, "", ET_Topic.getText().toString(), "", false, true, date,new Date(),new Date(),"","");
                        com.nordokod.scio.old.model.Guide guideModel = new com.nordokod.scio.old.model.Guide();
                        guideModel.createGuide(guide)
                                .addOnSuccessListener(documentReference -> {
                                    showSuccessfulMessage();
                                    mainActivity.refreshGuides();
                                    mainActivity.onCloseFragment("New Guide");
                                    ET_Topic.setText("");
                                })
                                .addOnCanceledListener(() -> showError(new OperationCanceledException()))
                                .addOnFailureListener(this::showError);
                    }
                }
            }
        });

        CL_Exacts.setOnClickListener(this::onClickCategoryListener);

        CL_Socials.setOnClickListener(this::onClickCategoryListener);

        CL_Sports.setOnClickListener(this::onClickCategoryListener);

        CL_Art.setOnClickListener(this::onClickCategoryListener);

        CL_Tech.setOnClickListener(this::onClickCategoryListener);

        CL_Entertainment.setOnClickListener(this::onClickCategoryListener);

        CL_Others.setOnClickListener(this::onClickCategoryListener);
    }

    @Override
    public void onStart() {
        ET_Topic.setText("");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        TV_Month.setText(Utilities.getMonthNameFromDate(date));
        TV_Day.setText(Utilities.getTwoDigitsFromDate(date,Calendar.DAY_OF_MONTH));

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat12 = new SimpleDateFormat("hh:mm");
        TV_Hour.setText(simpleDateFormat12.format(date));
        TV_Time.setText((calendar.get(Calendar.AM_PM) < 1) ? "AM" : "PM");
        date_selected = date;

        super.onStart();
    }

    @SuppressLint("ResourceType")
    private void onClickCategoryListener(View view) {
        if (preview_Category_View_Selected != view.getId()) {
            AppCompatImageView categoryIcon = view.findViewById(Utilities.getCategoryImageViewId(view.getId()));

            if(categoryIcon != null) {
                TypedValue typedValue = new TypedValue();
                Objects.requireNonNull(getActivity()).getTheme().resolveAttribute(iconSelectedColor, typedValue, true);

                categoryIcon.setColorFilter(typedValue.data);
            }

            category_selected_id = Utilities.getCategoryId(view.getId());

            // A la categoria anterior se le devuelve su color de icono normal
            if(preview_Category_View_Selected != 0) {
                categoryIcon = LL_Categories.findViewById(Utilities.getCategoryImageViewId(preview_Category_View_Selected));

                TypedValue typedValue = new TypedValue();
                Objects.requireNonNull(getActivity()).getTheme().resolveAttribute(iconNormalColor, typedValue, true);

                categoryIcon.setColorFilter(typedValue.data);
            }

            preview_Category_View_Selected = view.getId();
        }
    }

    private void showError(Exception exception){
        Log.d("testing","Aqui "+ exception.getMessage());
        UserMessage userMessage = new UserMessage();
        userMessage.showErrorMessage(context, userMessage.categorizeException(exception));
    }
    private void showSuccessfulMessage(){
        UserMessage userMessage = new UserMessage();
        userMessage.showSuccessfulOperationMessage(getContext(), UserOperations.CREATE_GUIDE);
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);
            Date date = calendar.getTime();
            TV_Month.setText(Utilities.getMonthNameFromDate(date));
            TV_Day.setText(Utilities.getTwoDigitsFromDate(date,Calendar.DAY_OF_MONTH));
            date_selected = date;
        });
        datePickerFragment.show(Objects.requireNonNull(getActivity()).getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        TimePickerFragment timePickerFragment = TimePickerFragment.newInstance((view, hourOfDay, minute) -> {
            TV_Time.setText((hourOfDay < 12) ? "AM" : "PM");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date_selected);
            calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat12 = new SimpleDateFormat("hh:mm");

            date_selected = calendar.getTime();
            String hour = simpleDateFormat12.format(date_selected);
            TV_Hour.setText(hour);
        });

        timePickerFragment.show(Objects.requireNonNull(getActivity()).getFragmentManager(), "timePicker");
    }

    @Override
    public void initAnimations(){
        press = AnimationUtils.loadAnimation(context, R.anim.press);
    }
}
