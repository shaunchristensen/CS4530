/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.animation.Animator.*;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.Paint.Style.STROKE;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class ViewCanvas extends View implements AnimatorListener, AnimatorUpdateListener
{
    // fields

    private int intColor;
    private int intStrokeIndex;
    private List<Pair<Integer, List<PointF>>> listStrokes;
    private List<PointF> listPoints;
    private OnAnimationToggleListener onAnimationToggleListener;
    private OnViewCanvasTouchListener onViewCanvasTouchListener;
    private final Paint paint;
    private PointF pointF1;
    private PointF pointF2;
    private final ValueAnimator valueAnimator;

    // constructors

    public ViewCanvas(Context context, int color, List<Pair<Integer, List<PointF>>> strokes, long duration)
    {
        super(context);

        intColor = color;
        listPoints = null;
        onAnimationToggleListener = null;
        onViewCanvasTouchListener = null;
        paint = new Paint(ANTI_ALIAS_FLAG);
        pointF1 = pointF2 = null;

        valueAnimator = new ValueAnimator();
        valueAnimator.addListener(this);
        valueAnimator.addUpdateListener(this);
        valueAnimator.setDuration(duration);

        setStrokes(strokes);
    }

    // interfaces

    public interface OnAnimationToggleListener
    {
        void onAnimationToggle(boolean animationIsStarted);
    }

    public interface OnViewCanvasTouchListener
    {
        void onViewCanvasTouch();
        void onViewCanvasTouch(int color, List<PointF> points);
    }

    // methods

    public void setColor(int color)
    {
        intColor = color;
    }

    public void setOnAnimationToggleListener(OnAnimationToggleListener listener)
    {
        onAnimationToggleListener = listener;
    }

    public void setOnViewCanvasTouchListener(OnViewCanvasTouchListener listener)
    {
        onViewCanvasTouchListener = listener;
    }

    public void setStrokes(List<Pair<Integer, List<PointF>>> strokes)
    {
        listStrokes = strokes;
        intStrokeIndex = listStrokes.size();

        invalidate();
    }

    public void toggleAnimation()
    {
        if (valueAnimator.isStarted())
        {
            valueAnimator.cancel();

            intStrokeIndex = listStrokes.size();
        }
        else
        {
            intStrokeIndex = 0;

            ValueAnimator.setFrameDelay(valueAnimator.getDuration() / (listStrokes.size() > 0 ? listStrokes.size() : 1));

            valueAnimator.setIntValues(0, listStrokes.size());
            valueAnimator.start();
        }

        invalidate();
    }

    @Override
    public void onAnimationCancel(Animator animation)
    {
        onAnimationEnd(animation);
    }

    @Override
    public void onAnimationEnd(Animator animation)
    {
        if (onAnimationToggleListener != null)
        {
            onAnimationToggleListener.onAnimationToggle(false);
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation)
    {
        onAnimationStart(animation);
    }

    @Override
    public void onAnimationStart(Animator animation)
    {
        if (onAnimationToggleListener != null)
        {
            onAnimationToggleListener.onAnimationToggle(true);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation)
    {
        if ((int)animation.getAnimatedValue() != intStrokeIndex)
        {
            intStrokeIndex = (int)animation.getAnimatedValue();

            invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        paint.setStrokeWidth((getHeight() < getWidth() ? getHeight() : getWidth()) / 100);
        paint.setStyle(STROKE);

        for (int i = 0; i < intStrokeIndex; i++)
        {
            paint.setColor(listStrokes.get(i).first);
            listPoints = listStrokes.get(i).second;

            for (int j = 1; j < listPoints.size(); j++)
            {
                pointF1 = listPoints.get(j - 1);
                pointF2 = listPoints.get(j);

                canvas.drawLine(pointF1.x, pointF1.y, pointF2.x, pointF2.y, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (!valueAnimator.isStarted())
        {
            if (event.getActionMasked() == ACTION_DOWN)
            {
                listStrokes.add(new Pair(intColor, new ArrayList<PointF>()));
                intStrokeIndex = listStrokes.size();

                if (onViewCanvasTouchListener != null)
                {
                    onViewCanvasTouchListener.onViewCanvasTouch();
                }
            }
            else if (event.getActionMasked() == ACTION_UP && onViewCanvasTouchListener != null)
            {
                onViewCanvasTouchListener.onViewCanvasTouch(listStrokes.get(listStrokes.size() - 1).first, listStrokes.get(listStrokes.size() - 1).second);
            }

            listStrokes.get(listStrokes.size() - 1).second.add(new PointF(event.getX(), event.getY()));

            invalidate();

            return true;
        }
        else
        {
            return false;
        }
    }
}