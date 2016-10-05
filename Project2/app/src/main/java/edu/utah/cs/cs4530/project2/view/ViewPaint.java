/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import static android.graphics.Color.BLACK;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.getMode;
import static android.view.View.MeasureSpec.getSize;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class ViewPaint extends View
{
    // fields

    private boolean booleanActive;
    private final int intColor;

    // constructors

    public ViewPaint(Context context, int color)
    {
        super(context);

        booleanActive = false;
        intColor = color;
    }

    // methods

    public void setActive(boolean active)
    {
        if (booleanActive != active)
        {
            booleanActive = active;

            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setColor(intColor);
        paint.setStyle(FILL);

        RectF rectF = new RectF();
        rectF.bottom = getHeight() * .8f - getPaddingBottom();
        rectF.left = getWidth() * .2f + getPaddingLeft();
        rectF.right = getWidth() * .8f - getPaddingRight();
        rectF.top = getHeight() * .2f + getPaddingTop();

        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() * .3f, paint);

        if (booleanActive)
        {
            paint.setColor(BLACK);
            paint.setStrokeWidth(rectF.width() / 10);
            paint.setStyle(STROKE);

            canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() * .4f, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int modeHeight = getMode(heightMeasureSpec);
        int modeWidth = getMode(widthMeasureSpec);
        int sizeHeight = getSize(heightMeasureSpec);
        int sizeWidth = getSize(widthMeasureSpec);
        int suggestedMinimumHeight = getSuggestedMinimumHeight();
        int suggestedMinimumWidth = getSuggestedMinimumWidth();

        if (modeHeight == EXACTLY)
        {
            suggestedMinimumHeight = suggestedMinimumWidth = sizeHeight;
        }

        if (modeWidth == EXACTLY)
        {
            suggestedMinimumHeight = suggestedMinimumWidth = sizeWidth;
        }

        setMeasuredDimension(resolveSize(suggestedMinimumWidth, widthMeasureSpec), resolveSize(suggestedMinimumHeight, heightMeasureSpec));
    }
}