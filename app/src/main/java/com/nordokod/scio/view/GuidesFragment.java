package com.nordokod.scio.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nordokod.scio.R;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.NoGuidesException;
import com.nordokod.scio.entity.OperationCanceledException;
import com.nordokod.scio.model.User;
import com.nordokod.scio.process.UserMessage;

import java.util.ArrayList;
import java.util.Objects;

import static com.nordokod.scio.R.attr.iconNormalColor;
import static com.nordokod.scio.R.attr.iconSelectedColor;

public class GuidesFragment extends Fragment implements BasicFragment {

    private Context context;
    private MainActivity mainActivity;
    private com.nordokod.scio.model.Guide guideModel;
    private User userModel;
    private RecyclerView RV_Guides;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ConstraintLayout CL_Exacts, CL_Socials, CL_Sports, CL_Art, CL_Tech, CL_Entertainment, CL_Others;
    private LinearLayout LL_Categories;
    private AppCompatTextView TV_Topic, TV_Cateoory, TV_Days;

    private ArrayList<Guide> guides;

    private int preview_Category_View_Selected = 0;

    public GuidesFragment() { }

    @SuppressLint("ValidFragment")
    public GuidesFragment(Context context, MainActivity mainActivity) {
        this.context = context;
        this.mainActivity = mainActivity;
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

        guideModel = new com.nordokod.scio.model.Guide();
        userModel = new User();

        getAllGuides();

        RV_Guides = view.findViewById(R.id.FGuides_RV_Guides);

        layoutManager = new LinearLayoutManager(getContext());
        RV_Guides.setLayoutManager(layoutManager);
    }

    @Override
    public void initListeners() {
        CL_Exacts.setOnClickListener(v -> onClickCategoryListener(v, 1));
        CL_Socials.setOnClickListener(v -> onClickCategoryListener(v, 2));
        CL_Sports.setOnClickListener(v -> onClickCategoryListener(v, 3));
        CL_Art.setOnClickListener(v -> onClickCategoryListener(v, 4));
        CL_Tech.setOnClickListener(v -> onClickCategoryListener(v, 5));
        CL_Entertainment.setOnClickListener(v -> onClickCategoryListener(v, 6));
        CL_Others.setOnClickListener(v -> onClickCategoryListener(v, 7));

        onBackFragment();
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

            // A la categoria anterior se le devuelve su color de icono normal
            if(preview_Category_View_Selected != 0) {
                categoryIcon = LL_Categories.findViewById(getCategoryImageViewId(preview_Category_View_Selected));

                TypedValue typedValue = new TypedValue();
                Objects.requireNonNull(getActivity()).getTheme().resolveAttribute(iconNormalColor, typedValue, true);

                categoryIcon.setColorFilter(typedValue.data);
            }

            preview_Category_View_Selected = view.getId();

            try {
                mAdapter = new GuidesRecyclerViewAdapter(getListOfGuides(category), category, context, mainActivity);
                ((GuidesRecyclerViewAdapter) mAdapter).configAdapter(guideModel);
                RV_Guides.setAdapter(mAdapter);
            } catch (Exception e) {
                showError(e);
            }

        }


    }

    public void getAllGuides() {
        guides = new ArrayList<>();

        guideModel.getAllGuides()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots.isEmpty()){
                        showError(new NoGuidesException());
                    }else{
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Guide guide = null;
                            try {
                                guide = new Guide(
                                        Integer.parseInt(Objects.requireNonNull(document.getData().get(Guide.KEY_CATEGORY)).toString()),
                                        document.getId(),
                                        (String)    document.getData().get(Guide.KEY_TOPIC),
                                        userModel.getBasicUserInfo().getUid(),
                                        (boolean)   document.getData().get(Guide.KEY_ONLINE),
                                        (boolean)   document.getData().get(Guide.KEY_ACTIVATED),
                                        (boolean)   document.getData().get(Guide.KEY_PERSONAL),
                                        Objects.requireNonNull(document.getTimestamp(Guide.KEY_DATETIME)).toDate()
                                );
                            } catch (Exception e) {
                                showError(e);
                            }
                            guides.add(guide);
                        }
                    }
                })
                .addOnCanceledListener(() -> showError(new OperationCanceledException()))
                .addOnFailureListener(this::showError);
    }

    private void showError(Exception e) {
        UserMessage userMessage = new UserMessage();
        userMessage.showErrorMessage(getContext(),userMessage.categorizeException(e));
    }

    private ArrayList<Guide> getListOfGuides(int category) throws NoGuidesException{
        ArrayList<Guide> guidesAux = new ArrayList<>();
        for (Guide guide : guides) {
            if (guide.getCategory() == category)
                guidesAux.add(guide);
        }
        if(guidesAux.isEmpty())throw new NoGuidesException();
        return guidesAux;
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

    public void onBackFragment() {
        switch (preview_Category_View_Selected) {
            case R.id.CL_Socials:
                preview_Category_View_Selected = 0;
                CL_Socials.performClick();
                break;
            case R.id.CL_Sports:
                preview_Category_View_Selected = 0;
                CL_Sports.performClick();
                break;
            case R.id.CL_Art:
                preview_Category_View_Selected = 0;
                CL_Art.performClick();
                break;
            case R.id.CL_Tech:
                preview_Category_View_Selected = 0;
                CL_Tech.performClick();
                break;
            case R.id.CL_Entertainment:
                preview_Category_View_Selected = 0;
                CL_Entertainment.performClick();
                break;
            case R.id.CL_Others:
                preview_Category_View_Selected = 0;
                CL_Others.performClick();
                break;
            default:
                preview_Category_View_Selected = 0;
                CL_Exacts.performClick();
                break;
        }
    }
}
