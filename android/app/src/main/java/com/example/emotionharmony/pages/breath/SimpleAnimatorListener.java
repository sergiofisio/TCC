package com.example.emotionharmony.pages.breath;

import android.animation.Animator;

public abstract class SimpleAnimatorListener implements Animator.AnimatorListener {
    @Override public void onAnimationStart(Animator animation) {}
    @Override public void onAnimationCancel(Animator animation) {}
    @Override public void onAnimationRepeat(Animator animation) {}
}
