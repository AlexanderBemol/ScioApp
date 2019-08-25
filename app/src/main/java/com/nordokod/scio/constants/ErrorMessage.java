package com.nordokod.scio.constants;

import com.nordokod.scio.R;
import android.app.Application;

public enum ErrorMessage {
    E_01(R.string.txt_error_title_E01, R.string.txt_error_message_E01),
    E_02(R.string.txt_error_title_E01, R.string.txt_error_message_E02),
    E_03(R.string.txt_error_title_E01, R.string.txt_error_message_E03),
    E_04(R.string.txt_error_title_E04, R.string.txt_error_message_E04),
    E_05(R.string.txt_error_title_E05, R.string.txt_error_message_E05),
    E_06(R.string.txt_error_title_E05, R.string.txt_error_message_E06),
    E_07(R.string.txt_error_title_E05, R.string.txt_error_message_E07),
    E_08(R.string.txt_error_title_E05, R.string.txt_error_message_E08),
    E_09(R.string.txt_error_title_E05, R.string.txt_error_message_E09),
    E_10(R.string.txt_error_title_E05, R.string.txt_error_message_E10),
    E_11(R.string.txt_error_title_E11, R.string.txt_error_message_E11),
    E_12(R.string.txt_error_title_E12, R.string.txt_error_message_E12),
    E_13(R.string.txt_error_title_E01,R.string.txt_error_message_E13);

    private int title;
    private int message;
    ErrorMessage(int title, int message) {
        this.message = message;
        this.title = title;
    }
}