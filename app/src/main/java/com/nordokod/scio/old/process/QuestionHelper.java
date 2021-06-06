package com.nordokod.scio.old.process;

import android.content.Context;

import com.google.firebase.firestore.DocumentSnapshot;
import com.nordokod.scio.old.constants.KindOfQuestion;
import com.nordokod.scio.old.entity.Guide;
import com.nordokod.scio.old.entity.MultipleChoiceQuestion;
import com.nordokod.scio.old.entity.OpenQuestion;
import com.nordokod.scio.old.entity.Question;
import com.nordokod.scio.old.entity.TrueFalseQuestion;
import com.nordokod.scio.old.view.MultipleChoiceQuestionDialog;
import com.nordokod.scio.old.view.OpenQuestionDialog;
import com.nordokod.scio.old.view.TrueFalseQuestionDialog;

import java.util.Random;

public class QuestionHelper {
    private static Guide guide;
    private static Question question;

    public void showRandomQuestion(Context context){
        com.nordokod.scio.old.model.Guide guideModel = new com.nordokod.scio.old.model.Guide();
        guideModel.getAllActivatedGuides().addOnSuccessListener(queryDocumentSnapshots -> {
            if(!queryDocumentSnapshots.getDocuments().isEmpty()){
                int n = queryDocumentSnapshots.getDocuments().size();
                Random random = new Random();
                int index = random.nextInt(n);
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(index);
                guide = guideModel.getGuideFromDocument(documentSnapshot);
                com.nordokod.scio.old.model.Question questionModel = new com.nordokod.scio.old.model.Question();
                guide.setQuestions(questionModel.getQuestionsFromGuide(guide));
                if(!guide.getQuestions().isEmpty()){
                    int nx = guide.getQuestions().size();
                    Random randomx = new Random();
                    int indexx = randomx.nextInt(nx);
                    question = guide.getQuestions().get(indexx);
                    if(question.getKindOfQuestion()==KindOfQuestion.MULTIPLE_CHOICE.getCode()){
                        MultipleChoiceQuestionDialog multipleChoiceQuestionDialog = new MultipleChoiceQuestionDialog(context);
                        multipleChoiceQuestionDialog.setQuestion((MultipleChoiceQuestion)question,guide);
                    }else if(question.getKindOfQuestion()==KindOfQuestion.TRUE_FALSE.getCode()){
                        TrueFalseQuestionDialog trueFalseQuestionDialog = new TrueFalseQuestionDialog(context);
                        trueFalseQuestionDialog.setQuestion((TrueFalseQuestion)question,guide);
                    }else {
                        OpenQuestionDialog openQuestionDialog = new OpenQuestionDialog(context);
                        openQuestionDialog.setQuestion((OpenQuestion) question, guide);
                    }
                }
            }
        });






    }
}
