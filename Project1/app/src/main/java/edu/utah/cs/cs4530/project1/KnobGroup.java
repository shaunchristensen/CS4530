package edu.utah.cs.cs4530.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Shaun Christensen on 2016.09.20.
 */
public class KnobGroup extends LinearLayout implements Knob.OnKnobAngleChangedListener, View.OnClickListener
{
    private Button buttonAdd;
    private Button buttonRemove;
    private final Knob knobRed;
    private final Knob knobGreen;
    private final Knob knobBlue;
    private LinearLayout linearLayoutHorizontal;
    private LinearLayout linearLayoutVertical;

    public KnobGroup(Context context)
    {
        super(context);

        buttonAdd = new Button(context);
        buttonRemove = new Button(context);

        knobRed = new Knob(context, Color.RED);
        knobGreen = new Knob(context, Color.GREEN);
        knobBlue = new Knob(context, Color.BLUE);

        linearLayoutHorizontal = new LinearLayout(context);
        linearLayoutVertical = new LinearLayout(context);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        buttonAdd.setOnClickListener(this);
        buttonAdd.setText("+");

        buttonRemove.setOnClickListener(this);
        buttonRemove.setText("-");

        knobRed.setOnKnobAngleChangedListener(this);
        knobGreen.setOnKnobAngleChangedListener(this);
        knobBlue.setOnKnobAngleChangedListener(this);

        linearLayoutVertical.setOrientation(LinearLayout.VERTICAL);
        linearLayoutVertical.addView(buttonAdd, new LinearLayout.LayoutParams(150, 150));
        linearLayoutVertical.addView(buttonRemove, new LinearLayout.LayoutParams(150, 150));

        linearLayoutHorizontal.addView(knobRed, new LinearLayout.LayoutParams(250, 300));
        linearLayoutHorizontal.addView(knobGreen, new LinearLayout.LayoutParams(250, 300));
        linearLayoutHorizontal.addView(knobBlue, new LinearLayout.LayoutParams(250, 300));
        linearLayoutHorizontal.addView(linearLayoutVertical);
    }

    public void onKnobAngleChanged(float theta)
    {
        Log.i("Tag", "Knob angle changed to " + theta);
    }

    @Override
    public void onClick(View view)
    {
//        buttonRemove.setEnabled(false);
    }
}