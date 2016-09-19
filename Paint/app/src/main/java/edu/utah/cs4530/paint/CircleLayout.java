/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.19
 * Assignment: Project 1 - Palette
 */

package edu.utah.cs4530.paint;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Shaun Christensen on 2016.09.19.
 */
public class CircleLayout extends ViewGroup
{
    // constructors

    public CircleLayout (Context context)
    {
        super(context);
    }

    // methods

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3)
    {
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            // TODO: 2016.09.19 Call measureChild to get the size the child would like to be.
            float density = getResources().getDisplayMetrics().density;
            float childWidth = 0.3f * 160.0f * density; // 300/1000 of an inch
            float childHeight = 0.25f * 160.0f * density; // 250/1000 of an inch

            float theta = (float)(2 * Math.PI / getChildCount() * childIndex);

            PointF childCenter = new PointF();
            childCenter.x = getWidth() * 0.5f + (getWidth() * 0.5f - childWidth * 0.5f) * (float)Math.cos(theta);
            childCenter.y = getHeight() * 0.5f + (getHeight() * 0.5f - childHeight * 0.5f) * (float)Math.sin(theta);

            Rect childRect = new Rect();
            childRect.left = (int)(childCenter.x - childWidth * 0.5f);
            childRect.right = (int)(childCenter.x + childWidth * 0.5f);
            childRect.top = (int)(childCenter.y - childHeight * 0.5f);
            childRect.bottom = (int)(childCenter.y + childHeight * 0.5f);

            View childView  = getChildAt(childIndex);
            childView.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}