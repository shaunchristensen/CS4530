/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.22
 * Assignment: Project 1 - Palette Paint
 */

package edu.utah.cs.cs4530.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Shaun Christensen on 2016.09.21.
 */
public class ViewGroupPalette extends ViewGroup implements ViewPaint.OnActiveChangeListener
{
    // fields

    private final ArrayList<ViewPaint> arrayListViewPaint;
    private int intIndex;
    private OnColorChangeListener onColorChangeListener;

    // constructors

    public ViewGroupPalette(Context context)
    {
        super(context);

        arrayListViewPaint = new ArrayList<ViewPaint>();
        arrayListViewPaint.add(new ViewPaint(context, Color.RED));
        arrayListViewPaint.add(new ViewPaint(context, Color.BLUE));
        arrayListViewPaint.add(new ViewPaint(context, Color.GREEN));
        arrayListViewPaint.add(new ViewPaint(context, Color.YELLOW));
        arrayListViewPaint.add(new ViewPaint(context, Color.argb(255, 128, 0, 128)));

        ((ViewPaint)arrayListViewPaint.get(0)).setActive(true);

        for (ViewPaint v: arrayListViewPaint)
        {
            this.addView(v);
            v.setOnActiveChangeListener(this);
        }

        intIndex = 0;
        onColorChangeListener = null;

        setWillNotDraw(false);
    }

    // interfaces

    public interface OnColorChangeListener
    {
        void onColorChange(int color);
    }

    // methods

    public void addColor(int color)
    {
        ViewPaint viewPaint = new ViewPaint(this.getContext(), color);
        viewPaint.setActive(true);
        viewPaint.setOnActiveChangeListener(this);

        arrayListViewPaint.add(viewPaint);

        this.addView(viewPaint);

        onActiveChange(viewPaint);
        requestLayout();
    }

    public int removeColor()
    {
        try
        {
            ViewPaint viewPaint = ((ViewPaint)arrayListViewPaint.get(intIndex));
            viewPaint.setOnActiveChangeListener(null);

            arrayListViewPaint.remove(intIndex);

            this.removeView(viewPaint);

            if (intIndex > 0)
            {
                viewPaint = ((ViewPaint)arrayListViewPaint.get(intIndex - 1));
            }
            else
            {
                viewPaint = ((ViewPaint)arrayListViewPaint.get(0));
            }

            viewPaint.setActive(true);

            onActiveChange(viewPaint);
            requestLayout();
        }
        catch (Exception e)
        {
            Log.e("ViewGroupPalette.removeColor", "Error: Unable to remove the color." + e.getMessage());
        }

        return arrayListViewPaint.size();
    }

    public void setOnColorChangeListener(OnColorChangeListener listener)
    {
        onColorChangeListener = listener;
    }

    @Override
    public void onActiveChange(ViewPaint viewPaint)
    {
        for (int i = 0; i < arrayListViewPaint.size(); i++)
        {
            if (((ViewPaint)arrayListViewPaint.get(i)) == viewPaint)
            {
                intIndex = i;
            }
            else
            {
                ((ViewPaint)arrayListViewPaint.get(i)).setActive(false);
            }
        }

        onColorChangeListener.onColorChange(viewPaint.getColor());
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.argb(255, 222, 184, 135));
        paint.setStyle(Paint.Style.FILL);

        RectF rectF = new RectF();
        rectF.bottom = getHeight() * .99f;
        rectF.left = getWidth() * .06f;
        rectF.right = getWidth() * .94f;
        rectF.top = getHeight() * .01f;

        canvas.drawOval(rectF, paint);

        paint.setColor(Color.argb(255, 139, 115, 85));
        paint.setStrokeWidth(rectF.width() * .01f);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawOval(rectF, paint);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3)
    {
        int radius = (int)((getHeight() < getWidth() ? getHeight() : getWidth()) / (3 + Math.sqrt(arrayListViewPaint.size())));
        float theta = (float)(2 * Math.PI / getChildCount());

        PointF pointF;
        Rect rect;
        View view;

        for (int index = 0; index < getChildCount(); index++)
        {
            pointF = new PointF();
            pointF.x = getWidth() * .5f - (getWidth() * .45f - radius) * (float)Math.cos(index * theta);
            pointF.y = getHeight() * .5f - (getHeight() * .5f - radius) * (float)Math.sin(index * theta);

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