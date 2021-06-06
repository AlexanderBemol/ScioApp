package com.nordokod.scio.old.constants;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.animation.Animation;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.nordokod.scio.R;

import java.util.Objects;

public class QuestionUtils {
    /**
     *
     *
     * @param context               -> {Context}
     * @param amountOfStarsEarned   -> {int}
     * @param IV_Star_1             -> {AppCompatImageView}
     * @param IV_Star_2             -> {AppCompatImageView}
     * @param IV_Star_3             -> {AppCompatImageView}
     * @param starEarnedAnimation   -> {Animation}
     */
    public static void fillStarsEarned(Context context, int amountOfStarsEarned, AppCompatImageView IV_Star_1, AppCompatImageView IV_Star_2, AppCompatImageView IV_Star_3, Animation starEarnedAnimation) {
        switch (amountOfStarsEarned) {
            case 1:
                IV_Star_1.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
                IV_Star_1.startAnimation(starEarnedAnimation);
                break;
            case 2:
                IV_Star_1.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
                IV_Star_1.startAnimation(starEarnedAnimation);

                IV_Star_2.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
                IV_Star_2.startAnimation(starEarnedAnimation);
                break;
            case 3:
                IV_Star_1.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
                IV_Star_1.startAnimation(starEarnedAnimation);

                IV_Star_2.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
                IV_Star_2.startAnimation(starEarnedAnimation);

                IV_Star_3.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
                IV_Star_3.startAnimation(starEarnedAnimation);
                break;
            default: break;
        }
    }

    public static void showCorrectAnswerCard(Context context, AppCompatTextView TV_Correct_Answer) {
        Objects.requireNonNull(TV_Correct_Answer);

        TV_Correct_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_correct_answer));
        TV_Correct_Answer.setTextAppearance(context, R.style.correctAnswer);
    }

    public static void showIncorrectAnswerCard(Context context, AppCompatTextView TV_Wrong_Answer) {
        Objects.requireNonNull(TV_Wrong_Answer);

        TV_Wrong_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_wrong_answer));
        TV_Wrong_Answer.setTextAppearance(context, R.style.wrongAnswer);
    }
}
