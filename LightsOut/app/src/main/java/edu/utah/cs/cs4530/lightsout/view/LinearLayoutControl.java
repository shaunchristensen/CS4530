/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.26
 * Assignment: Project F - Lights Out
 */

package edu.utah.cs.cs4530.lightsout.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import edu.utah.cs.cs4530.lightsout.R;

public class LinearLayoutControl extends LinearLayout
{
    // fields

    private final LayerDrawable layerDrawable;

    // constructors

    public LinearLayoutControl(Context context, AttributeSet attributeSet)
    {
        this(context, attributeSet, context.getResources().getDrawable(R.drawable.gradient_radial, null));
    }

    public LinearLayoutControl(Context context, AttributeSet attributeSet, Drawable drawable)
    {
        super(context, attributeSet);

        layerDrawable = (LayerDrawable)drawable;

        setBackground(layerDrawable);
    }

    // methods

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getHeight();
        int width = getWidth();

        ((GradientDrawable)layerDrawable.getDrawable(0)).setCornerRadius(height);
        ((GradientDrawable)layerDrawable.getDrawable(1)).setCornerRadii(new float[] {height, height, 0, 0, 0, 0, height, height});
        ((GradientDrawable)layerDrawable.getDrawable(2)).setCornerRadii(new float[] {0, 0, height, height, height, height, 0, 0});

        layerDrawable.getDrawable(1).setBounds(0, 0, (int)(height / 2f), height);
        layerDrawable.getDrawable(2).setBounds((int)(width - height / 2f), 0, width, height);
    }
}