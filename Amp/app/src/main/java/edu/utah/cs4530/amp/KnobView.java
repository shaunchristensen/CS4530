package edu.utah.cs4530.amp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Shaun on 2016.09.17.
 */
public class KnobView extends View
{
//    Gravity gravity = 0;

    public interface OnAngleChangedListener
    {
        void onAngleChanged(float theta);
    }

    float floatTheta = 0.0f;
    OnAngleChangedListener angleChangedListener = null;
    RectF knobRect = new RectF();

    public KnobView(Context context)
    {
        super(context);
    }

//    public void setGravity(Gravity gravity)
//    {
//        this.gravity = gravity;
//    }

    public float getTheta()
    {
        return floatTheta;
    }

    public void setTheta(float theta)
    {
        floatTheta = theta;
        invalidate();
    }

    public void setOnAngleChangedListener(OnAngleChangedListener listener)
    {
        angleChangedListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        PointF touchPoint = new PointF();
        touchPoint.x = event.getX();
        touchPoint.y = event.getY();

        float theta = (float)Math.atan2((double)touchPoint.y - knobRect.centerY(), (double)touchPoint.x - knobRect.centerX());
        setTheta(theta);
        angleChangedListener.onAngleChanged(theta);

        return true;//super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        knobRect.left = getPaddingLeft();
        knobRect.top = getPaddingTop();
        knobRect.right = getWidth() - getPaddingRight();
        knobRect.bottom = knobRect.width();

        float floatOffset = (getHeight() - knobRect.height()) * 0.5f;
        knobRect.top += floatOffset;
        knobRect.bottom += floatOffset;

        float radius = knobRect.width() * 0.35f;
//        float theta = (float)Math.PI * 0.5f;

        PointF nibCenter = new PointF();
        nibCenter.x = knobRect.centerX() + radius * (float)Math.cos((double)floatTheta);
        nibCenter.y = knobRect.centerY() + radius * (float)Math.sin((double)floatTheta);

        float nibRadius = radius * 0.2f;

        RectF nibRect = new RectF();
        nibRect.left = nibCenter.x - nibRadius;
        nibRect.top = nibCenter.y - nibRadius;
        nibRect.right = nibCenter.x + nibRadius;
        nibRect.bottom = nibCenter.y + nibRadius;

        Paint knobPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        knobPaint.setColor(Color.GREEN);

        Paint nibPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nibPaint.setColor(Color.YELLOW);

        canvas.drawOval(knobRect, knobPaint);
        canvas.drawOval(nibRect, nibPaint);
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

        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSpec;
            height = width;
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSpec;
            width = height;
        }

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }
}