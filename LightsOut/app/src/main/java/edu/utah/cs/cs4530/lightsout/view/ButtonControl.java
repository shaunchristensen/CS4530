/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.26
 * Assignment: Project F - Lights Out
 */

package edu.utah.cs.cs4530.lightsout.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.StateSet;
import android.widget.Button;

import edu.utah.cs.cs4530.lightsout.R;

public class ButtonControl extends Button
{
    // fields

    private final LayerDrawable layerDrawable;

    // constructors

    public ButtonControl(Context context, AttributeSet attributeSet)
    {
        this(context, attributeSet, context.getResources().getDrawable(R.drawable.button_control, null), new int[] {Color.parseColor("#d1b000"), Color.parseColor("#d1d100")}, new int[] {Color.parseColor("#ffd700"), Color.parseColor("#ffff00")});
    }

    public ButtonControl(Context context, AttributeSet attributeSet, Drawable drawable, int[] colors0, int[] colors1)
    {
        super(context, attributeSet);

        layerDrawable = (LayerDrawable)drawable;

        ((GradientDrawable)layerDrawable.getDrawable(0)).setColors(colors0);
        ((GradientDrawable)layerDrawable.getDrawable(1)).setColors(colors1);
    }

    // methods

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getHeight();

        ((GradientDrawable)layerDrawable.getDrawable(0)).setCornerRadius(height);
        ((GradientDrawable)layerDrawable.getDrawable(1)).setCornerRadius(height);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, layerDrawable.getDrawable(0));
        stateListDrawable.addState(StateSet.WILD_CARD, layerDrawable.getDrawable(1));

        setBackground(stateListDrawable);
    }
}