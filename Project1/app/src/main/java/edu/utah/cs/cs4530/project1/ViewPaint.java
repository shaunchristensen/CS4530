package edu.utah.cs.cs4530.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Shaun Christensen on 2016.09.20.
 */
public class ViewPaint extends View
{
    // fields

    private boolean booleanActive;
    private final int intColor;
    private OnActiveChangeListener onActiveChangeListener;

    // constructors

   public ViewPaint(Context context, int color)
   {
        super(context);

        booleanActive = false;
        intColor = color;
        onActiveChangeListener = null;
   }

    // interfaces

    public interface OnActiveChangeListener
    {
        void onActiveChange(ViewPaint viewPaint);
    }

    // methods

    public int getColor()
    {
        return intColor;
    }

    public void setActive(boolean active)
    {
        if (booleanActive != active)
        {
            booleanActive = active;

            invalidate();
        }
    }

    public void setOnActiveChangeListener(OnActiveChangeListener listener)
    {
        onActiveChangeListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(intColor);
        paint.setStyle(Paint.Style.FILL);

        RectF rectF = new RectF();
        rectF.bottom = getHeight() * .8f;
        rectF.left = getWidth() * .2f;
        rectF.right = getWidth() * .8f;
        rectF.top = getHeight() * .2f;

        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() * .3f, paint);

        if (booleanActive)
        {
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(rectF.width() * .1f);
            paint.setStyle(Paint.Style.STROKE);

            canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() * .4f, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int suggestedMinimumHeight = getSuggestedMinimumHeight();
        int suggestedMinimumWidth = getSuggestedMinimumWidth();

        if (modeHeight == MeasureSpec.EXACTLY)
        {
            suggestedMinimumHeight = suggestedMinimumWidth = sizeHeight;
        }

        if (modeWidth == MeasureSpec.EXACTLY)
        {
            suggestedMinimumHeight = suggestedMinimumWidth = sizeWidth;
        }

        setMeasuredDimension(resolveSize(suggestedMinimumWidth, widthMeasureSpec), resolveSize(suggestedMinimumHeight, heightMeasureSpec));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        setActive(true);
        onActiveChangeListener.onActiveChange(this);

        return true;
    }
}