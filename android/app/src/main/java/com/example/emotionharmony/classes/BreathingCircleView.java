package com.example.emotionharmony.classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BreathingCircleView extends View {

    private Paint borderPaint;
    private RectF circleBounds;
    private float progress = 0;

    public BreathingCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(30);
        borderPaint.setColor(0xFF00796B);
        borderPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float padding = borderPaint.getStrokeWidth() / 2;
        circleBounds = new RectF(padding, padding, w - padding, h - padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(circleBounds, -90, progress, false, borderPaint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }
}

