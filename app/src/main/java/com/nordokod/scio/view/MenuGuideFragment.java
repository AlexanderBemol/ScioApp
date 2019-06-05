package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.R;
import com.nordokod.scio.controller.MainController;
import com.nordokod.scio.entity.Guide;

public class MenuGuideFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private MainController mainController;
    private Guide guide;
    private AppCompatTextView TV_Study_Guide, TV_Edit_Guide, TV_Delete_Guide, TV_Share_Guide, TV_Add_Question, TV_Edit_Question, TV_Delete_Question, TV_Topic;
    private AppCompatImageView IV_Icon;

    public MenuGuideFragment() { }

    @SuppressLint("ValidFragment")
    public MenuGuideFragment(Context context, MainController mainController, Guide guide) {
        this.context = context;
        this.mainController = mainController;
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

        return view;
    }

    @Override
    public void initComponents(View view) {
        TV_Topic            = view.findViewById(R.id.FMGuide_TV_Topic);
        IV_Icon             = view.findViewById(R.id.FMGuide_IV_Icon);
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
        TV_Study_Guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Aqui abrimos el Activity para estudiar la guia.
            }
        });

        TV_Edit_Guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Aqui abrimos el Fragment para editar la guia.
            }
        });

        TV_Delete_Guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guideController.deleteGuide(guide.getId());
            }
        });

        TV_Share_Guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guideController.shareGuide(guide.getId());
            }
        });

        TV_Add_Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.onCloseFragment("Menu Guide");
                mainController.onNewQuestionDialog(guide);
            }
        });

        TV_Edit_Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TV_Delete_Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private int getCategoryIcon(int category) {
        switch (category) {
            case 1:     return R.drawable.ic_flask;
            case 2:     return R.drawable.ic_globe;
            case 3:     return R.drawable.ic_ball;
            case 4:     return R.drawable.ic_palette;
            case 5:     return R.drawable.ic_code;
            case 6:     return R.drawable.ic_theatre;
            case 7:     return R.drawable.ic_scio_face;
            default:    return R.drawable.ic_scio_face;
        }
    }
}
