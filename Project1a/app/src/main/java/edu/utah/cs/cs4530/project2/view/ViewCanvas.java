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

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class ViewCanvas extends View implements AnimatorListener, AnimatorUpdateListener
{
    // fields

    private int intColor;
    private int intStrokeIndex;
    private final List<Pair<Integer, List<PointF>>> listStrokes;
    private List<PointF> listPoints;
    private OnAnimationStartListener onAnimationStartListener;
    private OnAnimationStopListener onAnimationStopListener;
    private OnStrokeStartListener onStrokeStartListener;
    private OnStrokeStopListener onStrokeStopListener;
    private final Paint paint;
    private PointF pointF1;
    private PointF pointF2;
    private final ValueAnimator valueAnimator;

    // constructors

    public ViewCanvas(Context context, int color, long duration)
    {
        super(context);

        intColor = color;
        listPoints = null;
        listStrokes = new ArrayList<Pair<Integer, List<PointF>>>();
        onAnimationStartListener = null;
        onAnimationStopListener = null;
        onStrokeStartListener = null;
        onStrokeStopListener = null;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointF1 = pointF2 = null;

        valueAnimator = new ValueAnimator();
        valueAnimator.addListener(this);
        valueAnimator.addUpdateListener(this);
        valueAnimator.setDuration(duration);
    }

    // interfaces

    public interface OnAnimationStartListener
    {
        void onAnimationStart();
    }

    public interface OnAnimationStopListener
    {
        void onAnimationStop();
    }

    public interface OnStrokeStartListener
    {
        void onStrokeStart();
    }

    public interface OnStrokeStopListener
    {
        void onStrokeStop(int color, List<PointF> points);
    }

    // methods

    public void addStroke(int color, List<PointF> stroke)
    {
        listStrokes.add(new Pair<Integer, List<PointF>>(color, stroke));
        intStrokeIndex = listStrokes.size();

        invalidate();
    }

    public void clearStrokes()
    {
        listStrokes.clear();
        intStrokeIndex = listStrokes.size();

        invalidate();
    }

    public void setColor(int color)
    {
        intColor = color;
    }

    public void setOnAnimationStartListener(OnAnimationStartListener listener)
    {
        onAnimationStartListener = listener;
    }

    public void setOnAnimationStopListener(OnAnimationStopListener listener)
    {
        onAnimationStopListener = listener;
    }

    public void setOnStrokeStartListener(OnStrokeStartListener listener)
    {
        onStrokeStartListener = listener;
    }

    public void setOnStrokeStopListener(OnStrokeStopListener listener)
    {
        onStrokeStopListener = listener;
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

            ValueAnimator.setFrameDelay(valueAnimator.getDuration() / listStrokes.size());

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
        if (onAnimationStopListener != null)
        {
            onAnimationStopListener.onAnimationStop();
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
        if (onAnimationStartListener != null)
        {
            onAnimationStartListener.onAnimationStart();
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation)
    {
        if (intStrokeIndex < listStrokes.size())
        {
            intStrokeIndex++;

            invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        paint.setStrokeWidth((getHeight() < getWidth() ? getHeight() : getWidth()) / 100);
        paint.setStyle(Paint.Style.STROKE);

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
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
            {
                listStrokes.add(new Pair(intColor, new ArrayList<PointF>()));
                intStrokeIndex = listStrokes.size();

                if (onStrokeStartListener != null)
                {
                    onStrokeStartListener.onStrokeStart();
                }
            }
            else if (event.getActionMasked() == MotionEvent.ACTION_UP && onStrokeStopListener != null)
            {
                onStrokeStopListener.onStrokeStop(listStrokes.get(listStrokes.size() - 1).first, listStrokes.get(listStrokes.size() - 1).second);
            }

            listStrokes.get(listStrokes.size() - 1).second.add(new PointF(event.getX(), event.getY()));

            invalidate();
        }

        return true;
    }
}