/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by Shaun Christensen on 2016.09.30
 */
public class ViewKnob extends View
{
    // fields

    final private float floatTau;
    private float floatTheta;
    private float floatX;
    private float floatY;
    final private int intColor;
    private int intValue;
    private OnViewKnobTouchListener onViewKnobTouchListener;

    // constructors

    public ViewKnob(Context context, int color)
    {
        super(context);

        floatTau = (float)(Math.PI * 2);
        floatTheta = floatX = floatY = 0;
        intColor = color;
        intValue = 255;
        onViewKnobTouchListener = null;
    }

    // interfaces

    public interface OnViewKnobTouchListener
    {
        void onViewKnobTouch();
    }

    // methods

    public float getAngle()
    {
        return floatTheta;
    }

    public int getValue()
    {
        return intValue;
    }

    public void setOnViewKnobTouchListener(OnViewKnobTouchListener listener)
    {
        onViewKnobTouchListener = listener;
    }

    public void setAngle(float theta)
    {
        floatTheta = theta;
        setValue();

        invalidate();
    }

    public void setAngle(double x, double y)
    {
        setAngle((float)Math.atan2(y - floatY, x - floatX) + floatTau * .25f);
    }

    public void setValue()
    {
        intValue = (int)(255 * (1 - floatTheta / floatTau));
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        float height = getHeight() - getPaddingBottom() - getPaddingTop();
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float radius = (height < width ? height : width) * .5f;
        float y = (height - radius * 2 < radius * .5f ? height - radius * 2 : radius) * .5f;

        floatX = width * .5f + getPaddingLeft();
        floatY = height - ((height - (radius * 2) - y) * .5f) - radius + getPaddingTop();

        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setColor(intColor);
        paint.setStrokeWidth((int)radius / 5);

        canvas.drawLine(floatX, floatY, floatX, floatY - radius - y, paint);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(floatX, floatY, radius, paint);

        paint.setARGB(intValue, red(intColor), green(intColor), blue(intColor));

        canvas.drawCircle(floatX, floatY, radius, paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawLine(floatX + (radius * .5f) * (float)Math.cos((double)floatTheta - floatTau * .25f), floatY + (radius * .5f) * (float)Math.sin((double)floatTheta - floatTau * .25f), floatX + radius * (float)Math.cos((double)floatTheta - floatTau * .25f), floatY + radius * (float)Math.sin((double)floatTheta - floatTau * .25f), paint);
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
        setAngle(event.getX(), event.getY());
        onViewKnobTouchListener.onViewKnobTouch();

        return true;
    }
}