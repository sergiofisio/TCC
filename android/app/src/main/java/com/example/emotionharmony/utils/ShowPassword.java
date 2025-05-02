package com.example.emotionharmony.utils;

import android.text.InputType;
import android.widget.EditText;

public class ShowPassword {
    public static void ChangeShowPassword(EditText editText, boolean isChecked){
        if (isChecked) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        editText.setSelection(editText.getText().length());
    }
}
