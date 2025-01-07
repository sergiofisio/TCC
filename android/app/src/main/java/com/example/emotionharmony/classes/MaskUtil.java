package com.example.emotionharmony.classes;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MaskUtil {

    public static TextWatcher applyMask(final EditText editText, final String mask) {
        return new TextWatcher() {
            boolean isUpdating;
            String oldText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentText = unmask(s.toString());
                StringBuilder formattedText = new StringBuilder();

                if (isUpdating) {
                    oldText = currentText;
                    isUpdating = false;
                    return;
                }

                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && currentText.length() > oldText.length()) {
                        formattedText.append(m);
                        continue;
                    }
                    try {
                        formattedText.append(currentText.charAt(i));
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }

                isUpdating = true;
                editText.setText(formattedText.toString());
                editText.setSelection(formattedText.length());
            }

            @Override
            public void afterTextChanged(Editable s) {}

            private String unmask(String s) {
                return s.replaceAll("[^\\d]", "");
            }
        };
    }
}

