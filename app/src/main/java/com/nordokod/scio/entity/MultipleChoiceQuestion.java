package com.nordokod.scio.entity;

import java.util.ArrayList;

public class MultipleChoiceQuestion extends Question {

    private ArrayList<Option> answers;

    public MultipleChoiceQuestion(int id, String question, String topic, int category) {
        super(id, question, topic, category);
    }

    public void setAnswer(String option, boolean is_correct) {
        answers.add(new Option(option, is_correct));
    }

    public ArrayList<Option> getAnswers() {
        return answers;
    }

    public boolean isAnswerCorrect(int index) {
        return answers.get(index).is_correct();
    }

    public class Option {
        private String option;
        private boolean is_correct;

        Option(String option, boolean is_correct) {
            this.option = option;
            this.is_correct = is_correct;
        }

        public String getOption() {
            return option;
        }

        public boolean is_correct() {
            return is_correct;
        }
    }
}
