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
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Shaun Christensen on 2016.09.21.
 */
public class ViewCanvas extends View
{
    // fields

    private final ArrayList<Pair<Integer, ArrayList<PointF>>> arrayListPair;
    private ArrayList<PointF> arrayListPointF;
    private int intColor;
    private final Paint paint;
    private Path path;
    private PointF pointF;

    // constructors

    public ViewCanvas(Context context)
    {
        super(context);

        arrayListPair = new ArrayList<Pair<Integer, ArrayList<PointF>>>();
        arrayListPointF = null;
        intColor = Color.RED;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = null;
        pointF = null;
    }

    // methods

    public void setColor(int color)
    {
        intColor = color;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        paint.setStrokeWidth((getHeight() < getWidth() ? getHeight() : getWidth()) * .01f);
        paint.setStyle(Paint.Style.STROKE);

        for (int i = 0; i < arrayListPair.size(); i++)
        {
            paint.setColor(arrayListPair.get(i).first);
            arrayListPointF = arrayListPair.get(i).second;

            path = new Path();

            for (int j = 0; j < arrayListPointF.size(); j++)
            {
                pointF = arrayListPointF.get(j);

                if (j > 0)
                {
                    path.lineTo(pointF.x, pointF.y);
                }
                else
                {
                    path.moveTo(pointF.x, pointF.y);
                }
            }

            canvas.drawPath(path, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            arrayListPair.add(new Pair(intColor, new ArrayList<PointF>()));
        }

        arrayListPair.get(arrayListPair.size() - 1).second.add(new PointF(event.getX(), event.getY()));

        invalidate();

        return true;
    }
}