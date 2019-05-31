package com.nordokod.scio.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nordokod.scio.R;
import com.nordokod.scio.entity.Guide;

import java.util.ArrayList;
import java.util.Objects;

import static com.nordokod.scio.R.attr.iconNormalColor;
import static com.nordokod.scio.R.attr.iconSelectedColor;

public class GuidesFragment extends Fragment implements BasicFragment {

    private Context context;
    private Activity activity;
    private RecyclerView RV_Guides;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    //private GuidesController guidesController;

    private ConstraintLayout CL_Exacts, CL_Socials, CL_Sports, CL_Art, CL_Tech, CL_Entertainment, CL_Others;
    private LinearLayout LL_Categories;
    private AppCompatTextView TV_Topic, TV_Cateoory, TV_Days;
    private AppCompatImageView IV_Icon;
    private SwitchCompat Switch_State;

    private int preview_Category_View_Selected = 0, category_selected_id;

    public GuidesFragment() { }

    @SuppressLint("ValidFragment")
    public GuidesFragment(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guides, container, false);

        initComponents(view);
        initListeners();

        return view;
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initComponents(View view) {
        TV_Topic            = view.findViewById(R.id.TV_Topic);
        TV_Cateoory         = view.findViewById(R.id.TV_Category);
        TV_Days             = view.findViewById(R.id.TV_Days);

        LL_Categories       = view.findViewById(R.id.LL_Categories);

        CL_Exacts           = view.findViewById(R.id.CL_Exacts);
        CL_Socials          = view.findViewById(R.id.CL_Socials);
        CL_Sports           = view.findViewById(R.id.CL_Sports);
        CL_Art              = view.findViewById(R.id.CL_Art);
        CL_Tech             = view.findViewById(R.id.CL_Tech);
        CL_Entertainment    = view.findViewById(R.id.CL_Entertainment);
        CL_Others           = view.findViewById(R.id.CL_Others);

        //guidesController =  new GuidesController(context, activity);
        //guidesController.getListOfGuides();

        RV_Guides = view.findViewById(R.id.FGuides_RV_Guides);

        layoutManager = new LinearLayoutManager(getContext());
        RV_Guides.setLayoutManager(layoutManager);

    }

    @Override
    public void initListeners() {

        CL_Exacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v, 1);
            }
        });
        CL_Socials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v, 2);
            }
        });
        CL_Sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v, 3);
            }
        });
        CL_Art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v, 4);
            }
        });
        CL_Tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v, 5);
            }
        });
        CL_Entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v, 6);
            }
        });
        CL_Others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener(v, 7);
            }
        });
    }

    @SuppressLint("ResourceType")
    public void onClickCategoryListener(View view, int category) {
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

            mAdapter = new GuidesRecyclerViewAdapter(getListOfGuides(category), category, context);
            //((GuidesRecyclerViewAdapter) mAdapter).configAdapter(guidesController);

            RV_Guides.setAdapter(mAdapter);
        }
    }

    private ArrayList<Guide> getListOfGuides(int category) {
        //return guidesController.getListOfGuides(category);
        return null;
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
}
