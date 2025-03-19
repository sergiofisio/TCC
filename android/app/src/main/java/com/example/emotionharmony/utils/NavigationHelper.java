package com.example.emotionharmony.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.emotionharmony.R;

public class NavigationHelper {

    public static void navigateTo(Activity currentActivity, Class<?> targetActivity, boolean slideRight){
        TTSHelper.getInstance(currentActivity).stopSpeaking();

        hideKeyboard(currentActivity);

        Intent intent = new Intent(currentActivity, targetActivity);
        currentActivity.startActivity(intent);

        if(slideRight){
            currentActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else{
            currentActivity.overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }
    }

    public static void hideKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
