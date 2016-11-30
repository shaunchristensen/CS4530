/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.26
 * Assignment: Project F - Lights Out
 */

package edu.utah.cs.cs4530.lightsout.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import edu.utah.cs.cs4530.lightsout.R;

public class ButtonControlSelect extends ButtonControl
{
    public ButtonControlSelect(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet, context.getResources().getDrawable(R.drawable.button, null).mutate(), new int[] {Color.parseColor("#4b0082"), Color.parseColor("#8e4585")}, new int[] {Color.parseColor("#3d006a"), Color.parseColor("#b583b5")});
    }
}