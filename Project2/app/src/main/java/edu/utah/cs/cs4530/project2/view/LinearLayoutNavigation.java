/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import static android.graphics.Color.rgb;
import static android.view.View.*;
import static edu.utah.cs.cs4530.project2.controller.R.drawable.*;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class LinearLayoutNavigation extends LinearLayout implements OnClickListener
{
    // fields

    private boolean booleanAnimationIsStarted;
    private final Button buttonNext;
    private final Button buttonPlayStop;
    private final Button buttonPrevious;
    private OnButtonNextClickListener onButtonNextClickListener;
    private OnButtonPlayStopClickListener onButtonPlayStopClickListener;
    private OnButtonPreviousClickListener onButtonPreviousClickListener;
    private OnViewPaintClickListener onViewPaintClickListener;
    private ViewPaint viewPaint;

    // constructors

    public LinearLayoutNavigation(Context context, int color)
    {
        super(context);

        buttonNext = new Button(context);
        buttonNext.setBackgroundResource(next);
        buttonNext.setEnabled(false);
        buttonNext.setOnClickListener(this);

        buttonPlayStop = new Button(context);
        buttonPlayStop.setBackgroundResource(play);
        buttonPlayStop.setEnabled(false);
        buttonPlayStop.setOnClickListener(this);

        buttonPrevious = new Button(context);
        buttonPrevious.setBackgroundResource(previous);
        buttonPrevious.setEnabled(false);
        buttonPrevious.setOnClickListener(this);

        onButtonNextClickListener = null;
        onButtonPlayStopClickListener = null;
        onButtonPreviousClickListener = null;
        onViewPaintClickListener = null;

        viewPaint = new ViewPaint(getContext(), color);
        viewPaint.setActive(true);
        viewPaint.setOnClickListener(this);

        LayoutParams layoutParams = new LayoutParams(200, 200);

        addView(buttonPrevious, layoutParams);
        addView(buttonNext, layoutParams);
        addView(buttonPlayStop, layoutParams);
        addView(viewPaint, layoutParams);

        setBackgroundColor(rgb(192, 192, 192));
    }

    // interfaces

    public interface OnButtonNextClickListener
    {
        void onButtonNextClick();
    }

    public interface OnButtonPlayStopClickListener
    {
        void onButtonPlayStopClick();
    }

    public interface OnButtonPreviousClickListener
    {
        void onButtonPreviousClick();
    }

    public interface OnViewPaintClickListener
    {
        void onViewPaintClick();
    }

    // methods

    public void setAnimationIsStarted(boolean animationIsStarted)
    {
        booleanAnimationIsStarted = animationIsStarted;
    }

    public void setButtonNextEnabled(boolean enabled)
    {
        buttonNext.setEnabled(enabled);
    }

    public void setButtonPlayStopBackgroundResource()
    {
        if (booleanAnimationIsStarted)
        {
            buttonPlayStop.setBackgroundResource(stop);
        }
        else
        {
            buttonPlayStop.setBackgroundResource(play);
        }
    }

    public void setButtonPlayStopEnabled(boolean enabled)
    {
        buttonPlayStop.setEnabled(enabled);
    }

    public void setButtonPreviousEnabled(boolean enabled)
    {
        buttonPrevious.setEnabled(enabled);
    }

    public void setOnButtonNextClickListener(OnButtonNextClickListener listener)
    {
        onButtonNextClickListener = listener;
    }

    public void setOnButtonPlayStopClickListener(OnButtonPlayStopClickListener listener)
    {
        onButtonPlayStopClickListener = listener;
    }

    public void setOnButtonPreviousClickListener(OnButtonPreviousClickListener listener)
    {
        onButtonPreviousClickListener = listener;
    }

    public void setOnViewPaintClickListener(OnViewPaintClickListener listener)
    {
        onViewPaintClickListener = listener;
    }

    public void setViewPaint(int color)
    {
        viewPaint.setOnClickListener(null);
        removeView(viewPaint);

        viewPaint = new ViewPaint(getContext(), color);
        viewPaint.setActive(true);
        viewPaint.setOnClickListener(this);

        addView(viewPaint, new LayoutParams(200, 200));
        requestLayout();
    }

    @Override
    public void onClick(View view)
    {
        if (view == buttonNext && onButtonNextClickListener != null)
        {
            onButtonNextClickListener.onButtonNextClick();
        }
        if (view == buttonPlayStop && onButtonPlayStopClickListener != null)
        {
            onButtonPlayStopClickListener.onButtonPlayStopClick();
        }
        else if (view == buttonPrevious && onButtonPreviousClickListener != null)
        {
            onButtonPreviousClickListener.onButtonPreviousClick();
        }
        else if (view == viewPaint && onViewPaintClickListener != null && !booleanAnimationIsStarted)
        {
            onViewPaintClickListener.onViewPaintClick();
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3)
    {
        int left, right;

        View view;

        for (int index = 0; index < getChildCount(); index++)
        {
            view = getChildAt(index);

            if (index < getChildCount() - 1)
            {
                left = (getPaddingTop() + 200) * index + getPaddingTop();
                right = (getPaddingTop() + 200) * (index + 1);
            }
            else
            {
                left = getWidth() - getPaddingTop() - 200;
                right = getWidth() - getPaddingTop();
            }

            view.layout(left, getPaddingTop(), right, getHeight() - getPaddingBottom());
        }
    }
}