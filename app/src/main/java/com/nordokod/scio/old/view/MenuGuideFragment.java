package com.nordokod.scio.old.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.old.entity.Guide;
import com.nordokod.scio.old.entity.NoQuestionsInGuide;

public class MenuGuideFragment extends BottomSheetDialogFragment implements BasicFragment {
    private MainActivity mainActivity;
    private Context context;
    private Guide guide;
    private com.nordokod.scio.old.model.Guide mGuide;
    private Animation button_pressed_animation;

    public MenuGuideFragment() { }

    @SuppressLint("ValidFragment")
    public MenuGuideFragment(Context context, MainActivity mainActivity, com.nordokod.scio.old.model.Guide mGuide, Guide guide) {
        this.mainActivity = mainActivity;
        this.context = context;
        this.mGuide = mGuide;
        this.guide = guide;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_guide, container, false);

        initComponents(view);
        initListeners();
        initAnimations();

        return view;
    }

    @Override
    public void initComponents(View view) {
        AppCompatTextView TV_Topic = view.findViewById(R.id.FMGuide_TV_Topic);
        AppCompatImageView IV_Icon = view.findViewById(R.id.FMGuide_IV_Icon);
        // Guia
        TV_Study_Guide      = view.findViewById(R.id.FMGuide_TV_StudyGuide);
        TV_Edit_Guide       = view.findViewById(R.id.FMGuide_TV_EditGuide);
        TV_Delete_Guide     = view.findViewById(R.id.FMGuide_TV_DeleteGuide);
        TV_Share_Guide      = view.findViewById(R.id.FMGuide_TV_ShareGuide);
        // Preguntas
        TV_Add_Question     = view.findViewById(R.id.FMGuide_TV_AddQuestion);
        TV_Edit_Question    = view.findViewById(R.id.FMGuide_TV_EditQuestion);
        TV_Delete_Question  = view.findViewById(R.id.FMGuide_TV_DeleteQuestion);

        TV_Topic.setText(guide.getTopic());
        IV_Icon.setImageResource(getCategoryIcon(guide.getCategory()));
    }

    @Override
    public void initListeners() {
        TV_Study_Guide.setOnClickListener(v -> {
            if(guide.getAuxQuestions().size()>0){
                Intent intent = new Intent(context, StudyGuideActivity.class);
                intent.putExtra("GUIDE", guide);
                startActivity(intent);
            }else
                mainActivity.showError(new NoQuestionsInGuide());

        });

        TV_Edit_Guide.setOnClickListener(v -> {
            TV_Edit_Guide.startAnimation(button_pressed_animation);
            mainActivity.onCloseFragment("Menu Guide");
            mainActivity.showFragmentToEditGuide(guide);
        });

        TV_Delete_Guide.setOnClickListener(v -> {
            TV_Delete_Guide.startAnimation(button_pressed_animation);
            // TODO: Agregar la logica para eliminar la guia.
        });

        TV_Share_Guide.setOnClickListener(v -> mGuide.generateGuideLink(guide).addOnSuccessListener(shortDynamicLink -> {
            TV_Share_Guide.startAnimation(button_pressed_animation);
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "GUIDE");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shortDynamicLink.getShortLink().toString());

            context.startActivity(sharingIntent);
        }));

        TV_Add_Question.setOnClickListener(v -> {
            TV_Add_Question.startAnimation(button_pressed_animation);
            mainActivity.onCloseFragment("Menu Guide");
            mainActivity.showNewQuestionDialog(guide);
        });

        TV_Edit_Question.setOnClickListener(v -> {
            TV_Edit_Question.startAnimation(button_pressed_animation);

        });

        TV_Delete_Question.setOnClickListener(v -> {
            TV_Delete_Question.startAnimation(button_pressed_animation);
            //Intent intent = new Intent(context, DeleteQuestionsActivity.class);
            //intent.putExtra("GUIDE", guide);
            //startActivity(intent);
        });
    }

    @Override
    public void initAnimations() {
        button_pressed_animation = AnimationUtils.loadAnimation(context, R.anim.press);
    }

    private int getCategoryIcon(int category) {
        switch (category) {
            case 1:     return R.drawable.ic_flask;
            case 2:     return R.drawable.ic_globe;
            case 3:     return R.drawable.ic_ball;
            case 4:     return R.drawable.ic_palette;
            case 5:     return R.drawable.ic_code;
            case 6:     return R.drawable.ic_theatre;
            default:    return R.drawable.ic_scio_face;
        }
    }

    ////////////////////////////////////////////////////////////////////////// Objects from the view
    private AppCompatTextView TV_Study_Guide;
    private AppCompatTextView TV_Edit_Guide;
    private AppCompatTextView TV_Delete_Guide;
    private AppCompatTextView TV_Share_Guide;
    private AppCompatTextView TV_Add_Question;
    private AppCompatTextView TV_Edit_Question;
    private AppCompatTextView TV_Delete_Question;
}
