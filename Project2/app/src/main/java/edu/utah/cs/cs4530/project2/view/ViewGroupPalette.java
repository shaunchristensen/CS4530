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
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.rgb;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;
import static java.lang.Math.*;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class ViewGroupPalette extends ViewGroup implements OnClickListener
{
    // fields

    private int intColorIndex;
    private final List<ViewPaint> listColors;
    private OnSetColorListener onSetColorListener;

    private ViewPaint viewPaint;

    // constructors

    public ViewGroupPalette(Context context, int colorIndex, List<Integer> colors)
    {
        super(context);

        intColorIndex = colorIndex;
        listColors = new ArrayList<ViewPaint>();

        for (int color : colors)
        {
            viewPaint = new ViewPaint(context, color);
            viewPaint.setOnClickListener(this);

            addView(viewPaint);
            listColors.add(viewPaint);
        }

        listColors.get(intColorIndex).setActive(true);

        onSetColorListener = null;

        setWillNotDraw(false);
    }

    // interfaces

    public interface OnSetColorListener
    {
        void onSetColor(int colorIndex);
    }

    // methods

    public void addColor(int color)
    {
        viewPaint = new ViewPaint(getContext(), color);
        viewPaint.setOnClickListener(this);

        addView(viewPaint);
        listColors.add(viewPaint);
        requestLayout();
        setViewPaintActive(viewPaint);
    }

    public void removeColor()
    {
        if (intColorIndex >= 0 && intColorIndex < listColors.size())
        {
            viewPaint = listColors.get(intColorIndex);
            viewPaint.setOnClickListener(null);

            listColors.remove(intColorIndex);
            removeView(viewPaint);

            if (intColorIndex > 0)
            {
                viewPaint = listColors.get(intColorIndex - 1);
            }
            else
            {
                viewPaint = listColors.get(0);
            }

            requestLayout();
            setViewPaintActive(viewPaint);
        }
    }

    public void setOnSetColorListener(OnSetColorListener listener)
    {
        onSetColorListener = listener;
    }

    private void setViewPaintActive(ViewPaint viewPaint)
    {
        for (int i = 0; i < listColors.size(); i++)
        {
            if (viewPaint == listColors.get(i))
            {
                intColorIndex = i;
                listColors.get(intColorIndex).setActive(true);
                onSetColorListener.onSetColor(intColorIndex);
            }
            else
            {
                listColors.get(i).setActive(false);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v instanceof ViewPaint)
        {
            setViewPaintActive((ViewPaint)v);
        }
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setColor(rgb(222, 184, 135));
        paint.setStyle(FILL);

        float strokeWidth = (getHeight() < getWidth() ? getHeight() : getWidth()) / 100;

        canvas.drawOval(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom(), paint);

        paint.setColor(rgb(139, 115, 85));
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(STROKE);

        canvas.drawOval(getPaddingLeft() + strokeWidth, getPaddingTop() + strokeWidth, getWidth() - getPaddingRight() - strokeWidth, getHeight() - getPaddingBottom() - strokeWidth, paint);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3)
    {
        int radius = (int)((getHeight() < getWidth() ? getHeight() : getWidth()) / (10 + sqrt(listColors.size())));
        float theta = (float)(2 * PI / getChildCount());

        PointF pointF;
        Rect rect;
        View view;

        for (int index = 0; index < getChildCount(); index++)
        {
            pointF = new PointF();
            pointF.x = getWidth() / 2 - (getWidth() / 2 - radius) * (float)cos(index * theta);
            pointF.y = getHeight() / 2 - (getHeight() / 2 - radius) * (float)sin(index * theta);

            rect = new Rect();
            rect.bottom = (int)(pointF.y + radius);
            rect.left = (int)(pointF.x - radius);
            rect.right = (int)(pointF.x + radius);
            rect.top = (int)(pointF.y - radius) + getPaddingTop();

            view = getChildAt(index);
            view.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }
}