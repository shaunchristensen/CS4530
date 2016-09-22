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
    private final ArrayList<PointF> arrayListPointF;
    private int intColor;

    // constructors

    public ViewCanvas(Context context)
    {
        super(context);

        arrayListPair = new ArrayList<Pair<Integer, ArrayList<PointF>>>();
        arrayListPointF = new ArrayList<PointF>();
        intColor = Color.RED;
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

        ArrayList<PointF> arrayList;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth((getHeight() < getWidth() ? getHeight() : getWidth()) * .01f);
        paint.setStyle(Paint.Style.STROKE);

        Path path;
        PointF pointF;

        for (int i = 0; i < arrayListPair.size(); i++)
        {
            path = new Path();

            paint.setColor(arrayListPair.get(i).first);
            arrayList = arrayListPair.get(i).second;

            for (int j = 0; j < arrayList.size(); j++)
            {
                pointF = arrayList.get(j);

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
        arrayListPointF.add(new PointF(event.getX(), event.getY()));

        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            arrayListPair.add(new Pair(intColor, new ArrayList<PointF>(arrayListPointF)));
            arrayListPointF.clear();
        }

        invalidate();

        return true;
    }
}