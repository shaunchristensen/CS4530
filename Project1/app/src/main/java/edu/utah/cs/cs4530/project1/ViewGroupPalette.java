package edu.utah.cs.cs4530.project1;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Shaun Christensen on 2016.09.21.
 */
public class ViewGroupPalette extends ViewGroup
{
    public ViewGroupPalette(Context context)
    {
        super(context);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3)
    {
        int radius = getHeight() / 20;
        float theta = (float)(2 * Math.PI / getChildCount());
        PointF pointF;
        Rect rect;

        for (int index = 0; index < getChildCount(); index++)
        {
            pointF = new PointF();
            pointF.x = (getWidth() * .9f - radius) * (float)Math.cos(theta + Math.PI);
            pointF.y = (getHeight() * .9f - radius) * (float)Math.sin(theta + Math.PI);

/*            childCenter.x = getWidth() * 0.5f + (getWidth() * 0.5f - childWidth * 0.5f) * (float)Math.cos(theta);
            childCenter.y = getHeight() * 0.5f + (getHeight() * 0.5f - childHeight * 0.5f) * (float)Math.sin(theta);*/

            rect = new Rect();
            rect.bottom = (int)(pointF.y + radius);
            rect.left = (int)(pointF.x - radius);
            rect.right = (int)(pointF.x + radius);
            rect.top = (int)(pointF.y - radius);

            View view = getChildAt(index);
            view.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}