package com.nordokod.scio.process;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nordokod.scio.entity.MultipleChoiceQuestion;
import com.nordokod.scio.entity.TrueFalseQuestion;
import com.nordokod.scio.view.MultipleChoiceQuestionDialog;
import com.nordokod.scio.view.TrueFalseQuestionDialog;

import java.sql.Date;
import java.util.ArrayList;

public class QuestionLauncherProcess extends BroadcastReceiver {
    private Context currentContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.currentContext=context;
        Log.d("testeo","Question");
        launchQuestionTrueFalse();
    }

    public void launchQuestionTrueFalse(){
        TrueFalseQuestion tfQuestion= new TrueFalseQuestion("La estrella Sirio está a 8.6 años luz de la tierra","Astronomía",1,true);
        TrueFalseQuestionDialog tfDialog=new TrueFalseQuestionDialog();
        tfDialog.config(currentContext);
        tfDialog.setQuestion(tfQuestion);
        tfDialog.showDialog();
    }
    public void launchQuestionMulti(){
        ArrayList<String> respuestas=new ArrayList<>();
        respuestas.add("Hidrógeno");
        respuestas.add("Helio");
        respuestas.add("Nitrógeno");
        respuestas.add("Carbono");
        MultipleChoiceQuestion mcQuestion=new MultipleChoiceQuestion("¿Cual elemento usa como combustible en su primera etapa de combustión?","Astrofísica",1,respuestas,0);
        MultipleChoiceQuestionDialog mcDialog=new MultipleChoiceQuestionDialog();
        mcDialog.onReceive(currentContext,null);
        mcDialog.setQuestion(mcQuestion);
        mcDialog.showDialog();



    }


}
