package com.expensemaster.Supporting;

import android.widget.EditText;

/**
 * Created by Ankit Kodalikar on 12/03/2017.
 */

public class Validator {
    private static final String REQUIRED_MSG = "Please enter value";

    public static boolean hasText(EditText editText) {
        String text = editText.getText().toString().trim();
        editText.setError(null);
        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }
        return true;
    }
}
