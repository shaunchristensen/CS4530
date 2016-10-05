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
import android.view.MotionEvent;
import android.view.View;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.getMode;
import static android.view.View.MeasureSpec.getSize;
import static java.lang.Math.*;

/**
 * Created by Shaun Christensen on 2016.09.30
 */
public class ViewKnob extends View
{
    // fields

    private final float floatTau;
    private float floatTheta;
    private float floatX;
    private float floatY;
    private final int intColor;
    private int intValue;
    private OnViewKnobTouchListener onViewKnobTouchListener;

    // constructors

    public ViewKnob(Context context, int color)
    {
        super(context);

        floatTau = (float)(PI * 2);
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
        setAngle((float)atan2(y - floatY, x - floatX) + floatTau * .25f);
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
        float radius = (height < width ? height : width) / 2;
        float y = (height - radius * 2 < radius / 2 ? height - radius * 2 : radius) / 2;

        floatX = width / 2 + getPaddingLeft();
        floatY = height - ((height - (radius * 2) - y) / 2) - radius + getPaddingTop();

        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setColor(intColor);
        paint.setStrokeWidth((int)radius / 5);

        canvas.drawLine(floatX, floatY, floatX, floatY - radius - y, paint);

        paint.setColor(BLACK);
        paint.setStyle(FILL);

        canvas.drawCircle(floatX, floatY, radius, paint);

        paint.setARGB(intValue, red(intColor), green(intColor), blue(intColor));

        canvas.drawCircle(floatX, floatY, radius, paint);

        paint.setColor(WHITE);
        paint.setStyle(STROKE);

        canvas.drawLine(floatX + (radius * .5f) * (float)cos((double)floatTheta - floatTau * .25f), floatY + (radius * .5f) * (float)sin((double)floatTheta - floatTau * .25f), floatX + radius * (float)cos((double)floatTheta - floatTau * .25f), floatY + radius * (float)sin((double)floatTheta - floatTau * .25f), paint);
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

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        setAngle(event.getX(), event.getY());
        onViewKnobTouchListener.onViewKnobTouch();

        return true;
    }
}