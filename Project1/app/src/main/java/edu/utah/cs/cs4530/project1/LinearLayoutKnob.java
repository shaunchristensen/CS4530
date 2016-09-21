package edu.utah.cs.cs4530.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Shaun Christensen on 2016.09.20.
 */
public class LinearLayoutKnob extends LinearLayout implements View.OnClickListener, ViewKnob.OnKnobAngleChangedListener
{
    // fields

    private Button buttonAddColor;
    private Button buttonRemoveColor;
    private int intBlue;
    private int intGreen;
    private int intRed;
    private LinearLayout linearLayoutHorizontal;
    private LinearLayout linearLayoutVertical;
    private final ViewKnob viewKnobRed;
    private final ViewKnob viewKnobGreen;
    private final ViewKnob viewKnobBlue;

    // constructors

    public LinearLayoutKnob(Context context)
    {
        super(context);

        buttonAddColor = new Button(context);
        buttonAddColor.setOnClickListener(this);
        buttonAddColor.setText("+");

        buttonRemoveColor = new Button(context);
        buttonRemoveColor.setOnClickListener(this);
        buttonRemoveColor.setText("-");

        intBlue = intGreen = intRed = 255;

        linearLayoutHorizontal = new LinearLayout(context);
        linearLayoutVertical = new LinearLayout(context);

        viewKnobBlue = new ViewKnob(context, Color.BLUE);
        viewKnobBlue.setOnKnobAngleChangedListener(this);

        viewKnobGreen = new ViewKnob(context, Color.GREEN);
        viewKnobGreen.setOnKnobAngleChangedListener(this);

        viewKnobRed = new ViewKnob(context, Color.RED);
        viewKnobRed.setOnKnobAngleChangedListener(this);
    }

    // methods

    public void onKnobAngleChanged()
    {
        intBlue = viewKnobBlue.getValue();
        intGreen = viewKnobGreen.getValue();
        intRed = viewKnobRed.getValue();
    }

    @Override
    public void onClick(View view)
    {

        if (view == buttonAddColor)
        {
            buttonAddColor.setEnabled(false);
        }
        else if (view == buttonRemoveColor)
        {
            buttonRemoveColor.setEnabled(false);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

        linearLayoutVertical.addView(buttonAddColor, layoutParams);
        linearLayoutVertical.addView(buttonRemoveColor, layoutParams);
        linearLayoutVertical.setOrientation(LinearLayout.VERTICAL);

        layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

        linearLayoutHorizontal.addView(viewKnobRed, layoutParams);
        linearLayoutHorizontal.addView(viewKnobGreen, layoutParams);
        linearLayoutHorizontal.addView(viewKnobBlue, layoutParams);
        linearLayoutHorizontal.addView(linearLayoutVertical);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getSuggestedMinimumHeight();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
        int width = getSuggestedMinimumWidth();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSpec;
            width = height;
        }

        if (widthMode == MeasureSpec.EXACTLY)
        {
            height = width;
            width = widthSpec;
        }

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }
}