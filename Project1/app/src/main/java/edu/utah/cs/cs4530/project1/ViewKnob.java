package edu.utah.cs.cs4530.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

/**
 * Created by Shaun Christensen on 2016.09.17.
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
    private OnValueChangeListener onValueChangeListener;

    // constructors

    public ViewKnob(Context context, int color)
    {
        super(context);

        floatTau = (float)(Math.PI * 2);
        floatTheta = floatX = floatY = 0;
        intColor = color;
        intValue = 255;
        onValueChangeListener = null;
    }

    // interfaces

    public interface OnValueChangeListener
    {
        void onValueChange();
    }

    // methods

    public int getValue()
    {
        return intValue;
    }

    public void setValue(double x, double y)
    {
        floatTheta = (float)Math.atan2(y - floatY, x - floatX) + floatTau / 4;
        intValue = (int)(255 * (1 - floatTheta / floatTau));

        invalidate();
    }

    public void setOnValueChangeListener(OnValueChangeListener listener)
    {
        onValueChangeListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        RectF rectF = new RectF();
        rectF.bottom = getHeight() * 11 / 12;
        rectF.left = getWidth() / 10;
        rectF.right = getWidth() * .9f;
        rectF.top = getHeight() / 4;

        floatX = rectF.centerX();
        floatY = rectF.centerY();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(intColor);
        paint.setStrokeWidth(rectF.width() / 10);

        canvas.drawLine(rectF.centerX(), getHeight() / 12, rectF.centerX(), getHeight() / 4, paint);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2, paint);

        paint.setARGB(intValue, red(intColor), green(intColor), blue(intColor));

        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2, paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawLine(rectF.centerX() + (rectF.width() / 4) * (float)Math.cos((double)floatTheta - floatTau / 4), rectF.centerY() + (rectF.height() / 4) * (float)Math.sin((double)floatTheta - floatTau / 4), rectF.centerX() + (rectF.width() / 2) * (float)Math.cos((double)floatTheta - floatTau / 4), rectF.centerY() + (rectF.height() / 2) * (float)Math.sin((double)floatTheta - floatTau / 4), paint);
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
        setValue(event.getX(), event.getY());
        onValueChangeListener.onValueChange();

        return true;
    }
}