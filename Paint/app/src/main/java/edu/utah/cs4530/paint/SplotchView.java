package edu.utah.cs4530.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.view.View;

/**
 * Created by Shaun Christensen on 2016.09.19.
 */
public class SplotchView extends View
{
    boolean splotchHighlighted = false;
    int splotchColor = Color.WHITE;

    public boolean isSplotchHighlighted()
    {
        return splotchHighlighted;
    }

    public int getSplotchColor()
    {
        return splotchColor;
    }

    public void setSplotchHighlighted(boolean splotchHighlighted)
    {
        this.splotchHighlighted = splotchHighlighted;
        invalidate();
    }

    public void setSplotchColor(int splotchColor)
    {
        this.splotchColor = splotchColor;
        invalidate();
    }

    public SplotchView(Context context)
    {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        RectF splotchRect = new RectF();
        splotchRect.left = getPaddingLeft();
        splotchRect.top = getPaddingTop();
        splotchRect.right = getWidth() - getPaddingRight();
        splotchRect.bottom = getHeight() - getPaddingBottom();

        Paint splotchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        splotchPaint.setColor(splotchColor);

        canvas.drawOval(splotchRect, splotchPaint);

        if (splotchHighlighted)
        {
            Paint highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // // TODO: 2016.09.19 Match sure highlight doesn't match paint color
            highlightPaint.setColor(Color.YELLOW);
            highlightPaint.setStyle(Paint.Style.STROKE);
            highlightPaint.setStrokeWidth(splotchRect.height() * 0.1f);

            canvas.drawOval(splotchRect, highlightPaint);
        }
    }
}