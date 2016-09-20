package edu.utah.cs4530.amp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
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

    private float floatTau;
    private float floatTheta;
    private int intColor;
    private OnAngleChangedListener onAngleChangedListener;
    private Paint paint;
    private RectF rectF;

    // constructors

    public Knob(Context context, int color)
    {
        super(context);

        floatTau = (float)(Math.PI * 2);
        floatTheta = 0;
        intColor = color;
        onAngleChangedListener = null;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
    }

    // interfaces

    public interface OnAngleChangedListener
    {
        void onAngleChanged(float theta);
    }

    // methods

    public float getAngle()
    {
        return floatTheta;
    }

    public void setAngle(float theta)
    {
        floatTheta = theta;
        invalidate();
    }

    public void setOnAngleChangedListener(OnAngleChangedListener listener)
    {
        onAngleChangedListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        PointF pointF = new PointF();
        pointF.x = event.getX();
        pointF.y = event.getY();

        float theta = (float)Math.atan2((double)(pointF.y - rectF.centerY()), (double)(pointF.x - rectF.centerX())) + floatTau / 4;

        setAngle(theta);
        onAngleChangedListener.onAngleChanged(theta);

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

//        float offset = (getWidth() - rectF.width()) / 8;
//        float radius = rectF.width() / 2;

        rectF.bottom = getHeight() * .9f;
        rectF.left = getWidth() * .1f;
        rectF.right = getWidth() * .9f;
        rectF.top = getHeight() * .1f;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(rectF.width() / 40);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2, paint);

        paint.setARGB((int)(255f * (1 - floatTheta / floatTau)), red(intColor), green(intColor), blue(intColor));

        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2, paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2, paint);
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