package edu.utah.cs4530.amp;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by Shaun Christensen on 2016.09.19.
 */
public class KnobGroup extends LinearLayout implements Knob.OnAngleChangedListener
{
    public KnobGroup(Context context)
    {
        super(context);
    }

    public void onAngleChanged(float theta)
    {
    }
}