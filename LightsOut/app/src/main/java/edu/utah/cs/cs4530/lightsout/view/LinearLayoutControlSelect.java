/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.26
 * Assignment: Project F - Lights Out
 */

package edu.utah.cs.cs4530.lightsout.view;

import android.content.Context;
import android.util.AttributeSet;

import edu.utah.cs.cs4530.lightsout.R;

public class LinearLayoutControlSelect extends LinearLayoutControl
{
    // constructors

    public LinearLayoutControlSelect(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet, context.getResources().getDrawable(R.drawable.gradient_radial, null).mutate());
    }
}