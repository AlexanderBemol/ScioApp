package com.nordokod.scio.process;

import android.content.Context;

import com.google.firebase.firestore.DocumentSnapshot;
import com.nordokod.scio.constants.KindOfQuestion;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.MultipleChoiceQuestion;
import com.nordokod.scio.entity.OpenQuestion;
import com.nordokod.scio.entity.Question;
import com.nordokod.scio.entity.TrueFalseQuestion;
import com.nordokod.scio.view.MultipleChoiceQuestionDialog;
import com.nordokod.scio.view.OpenQuestionDialog;
import com.nordokod.scio.view.TrueFalseQuestionDialog;

import java.util.ArrayList;
import java.util.Random;

public class QuestionHelper {
    private static Guide guide;
    private static Question question;

    public void showRandomQuestion(Context context) throws InterruptedException{
        com.nordokod.scio.model.Guide guideModel = new com.nordokod.scio.model.Guide();
        guideModel.getAllGuides().addOnSuccessListener(queryDocumentSnapshots -> {
            if(!queryDocumentSnapshots.getDocuments().isEmpty()){
                int n = queryDocumentSnapshots.getDocuments().size();
                Random random = new Random();
                int index = random.nextInt(n);
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(index);
                guide = guideModel.getGuideFromDocument(documentSnapshot);
                com.nordokod.scio.model.Question questionModel = new com.nordokod.scio.model.Question();
                questionModel.getSnapshotQuestionsOfGuide(guide).addOnSuccessListener(queryDocumentSnapshotsx -> {
                    if(!queryDocumentSnapshotsx.getDocuments().isEmpty()){
                        int nx = queryDocumentSnapshotsx.getDocuments().size();
                        Random randomx = new Random();
                        int indexx = randomx.nextInt(nx);
                        try {
                            ArrayList<Question> questions = questionModel.getQuestionsFromSnapshot(queryDocumentSnapshotsx);
                            question = questions.get(indexx);
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

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });






    }
}
