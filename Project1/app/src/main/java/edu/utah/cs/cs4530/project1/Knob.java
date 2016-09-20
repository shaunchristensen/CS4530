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
public class Knob extends View
{
    // fields

    final private float floatTau;
    private float floatTheta;
    private int intAlpha;
    final private int intColor;
    private OnKnobAngleChangedListener onKnobAngleChangedListener;
    private Paint paint;
    private RectF rectF;

    // constructors

    public Knob(Context context, int color)
    {
        super(context);

        floatTau = (float)(Math.PI * 2);
        floatTheta = 0;
        intAlpha = 255;
        intColor = color;
        onKnobAngleChangedListener = null;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
    }

    // interfaces

    public interface OnKnobAngleChangedListener
    {
        void onKnobAngleChanged(float theta);
    }

    // methods

    public float getAngle()
    {
        return floatTheta;
    }

    public int getValue()
    {
        return intAlpha;
    }

    public void setAngle(double x, double y)
    {
        floatTheta = (float)Math.atan2(y - rectF.centerY(), x - rectF.centerX()) + floatTau / 4;
        setValue();
        invalidate();
    }

    public void setValue()
    {
        intAlpha = (int)(255 * (1 - floatTheta / floatTau));
    }

    public void setOnKnobAngleChangedListener(OnKnobAngleChangedListener listener)
    {
        onKnobAngleChangedListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        setAngle(event.getX(), event.getY());
        onKnobAngleChangedListener.onKnobAngleChanged(floatTheta);

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        rectF.bottom = getHeight() * 11 / 12;
        rectF.left = getWidth() / 10;
        rectF.right = getWidth() * 9 / 10;
        rectF.top = getHeight() * 3 / 12;

        paint.setColor(intColor);
        paint.setStrokeWidth(rectF.width() / 20);

        canvas.drawLine(rectF.centerX(), 25, rectF.centerX(), 75, paint);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2, paint);

        paint.setARGB(intAlpha, red(intColor), green(intColor), blue(intColor));

        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2, paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawLine(rectF.centerX() + (rectF.width() / 4) * (float)Math.cos((double)floatTheta - floatTau / 4), rectF.centerY() + (rectF.height() / 4) * (float)Math.sin((double)floatTheta - floatTau / 4), rectF.centerX() + (rectF.width() / 2) * (float)Math.cos((double)floatTheta - floatTau / 4), rectF.centerY() + (rectF.height() / 2) * (float)Math.sin((double)floatTheta - floatTau / 4), paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);

        int height = getSuggestedMinimumHeight();
        int width = getSuggestedMinimumWidth();

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSpec;
            width = height;
        }

        if (widthMode == MeasureSpec.EXACTLY)
        {
            height = width;
            width = widthSpec;
        }

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }
}