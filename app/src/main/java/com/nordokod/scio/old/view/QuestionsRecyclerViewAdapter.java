package com.nordokod.scio.old.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nordokod.scio.R;
import com.nordokod.scio.old.entity.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestionsRecyclerViewAdapter extends RecyclerView.Adapter<QuestionsRecyclerViewAdapter.ViewHolder> {
    private static final int MULTIPLE_CHOICE_QUESTION = 1;
    private static final int TRUE_FALSE_QUESTION = 2;
    private static final int OPEN_QUESTION = 3;

    private Context context;
    private DeleteQuestionsActivity deleteQuestionsActivity;
    private List<Question> questionList;
    private List<Integer> selectedQuestionIndexList = new ArrayList<>();
    private com.nordokod.scio.old.model.Question questionModel;

    QuestionsRecyclerViewAdapter(Context context, DeleteQuestionsActivity deleteQuestionsActivity, List<Question> questionList) {
        this.context = context;
        this.deleteQuestionsActivity = deleteQuestionsActivity;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_card, parent, false);

        return new QuestionsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionsRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        viewHolder.IV_Icon.setImageResource(getQuestionTypeImageResource(questionList.get(position).getKindOfQuestion()));
        viewHolder.TV_Question.setText(questionList.get(position).getQuestion());
        viewHolder.TV_Question_Type.setText(getQuestionTypeStringResource(questionList.get(position).getKindOfQuestion()));

        initListener(viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    /**
     *
     * @param viewHolder
     * @param position
     */
    private void initListener(QuestionsRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        viewHolder.CL_Question_Card.setOnClickListener(v -> {
            // TODO: Logica para seleccionar y deseleccionar la card

        });
    }

    /**
     *
     * @param kindOfQuestion
     * @return
     */
    private int getQuestionTypeImageResource(int kindOfQuestion) {
        switch (kindOfQuestion) {
            case MULTIPLE_CHOICE_QUESTION: return R.drawable.ic_multiple_question_art;
            case TRUE_FALSE_QUESTION: return R.drawable.ic_t_f_question_art;
            case OPEN_QUESTION: default: return R.drawable.ic_open_question_art;
        }
    }

    /**
     *
     * @return
     */
    List<Integer> getSelectedQuestionsList() {
        return Objects.requireNonNull(selectedQuestionIndexList);
    }

    /**
     *
     * @param kindOfQuestion
     * @return
     */
    private int getQuestionTypeStringResource(int kindOfQuestion) {
        switch (kindOfQuestion) {
            case MULTIPLE_CHOICE_QUESTION: return R.string.txt_title_question_type_multiple_choice;
            case TRUE_FALSE_QUESTION: return R.string.txt_title_question_type_true_false;
            case OPEN_QUESTION: default: return R.string.txt_title_question_type_open_answer;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout CL_Question_Card;
        AppCompatTextView TV_Question;
        AppCompatTextView TV_Question_Type;
        AppCompatImageView IV_Icon;

        ViewHolder(View view) {
            super(view);

            CL_Question_Card    = view.findViewById(R.id.QCard_CL_Question);
            TV_Question         = view.findViewById(R.id.QCard_TV_Question);
            TV_Question_Type    = view.findViewById(R.id.QCard_TV_Question_Type);
            IV_Icon             = view.findViewById(R.id.QCard_IV_Icon);
        }
    }
}
