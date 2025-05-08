// Pacote onde essa classe está localizada
package com.example.emotionharmony.classes;

// Importações necessárias para trabalhar com views customizadas
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Classe customizada que desenha um círculo animado de respiração.
 * A ideia é representar graficamente o "progresso" da respiração.
 */
public class BreathingCircleView extends View {

    private Paint borderPaint;
    private RectF circleBounds;
    private float progress = 0;

    // Construtor padrão para ser usado em XML
    public BreathingCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Método para configurar o pincel (Paint)
    private void init() {
        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(30);
        borderPaint.setColor(0xFF00796B);
        borderPaint.setAntiAlias(true);
    }

    // Este método é chamado sempre que o tamanho da View muda
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Define os limites do círculo levando em conta a largura da borda
        float padding = borderPaint.getStrokeWidth() / 2;
        circleBounds = new RectF(padding, padding, w - padding, h - padding);
    }

    // Método responsável por desenhar o círculo na tela
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Desenha um arco com base no progresso atual
        // Começa no topo (-90°) e vai até o ângulo 'progress'
        canvas.drawArc(circleBounds, -90, progress, false, borderPaint);
    }

    /**
     * Atualiza o progresso do círculo (0 a 360 graus) e força o redrawing.
     * @param progress valor do progresso (em graus)
     */
    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }
}
