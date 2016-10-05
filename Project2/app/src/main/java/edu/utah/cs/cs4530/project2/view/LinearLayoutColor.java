/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.rgb;
import static android.view.View.*;
import static android.widget.LinearLayout.LayoutParams.*;
import static edu.utah.cs.cs4530.project2.view.ViewKnob.*;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class LinearLayoutColor extends LinearLayout implements OnClickListener, OnViewKnobTouchListener
{
    // fields

    private final Button buttonColorAdd;
    private final Button buttonColorRemove;
    private final Button buttonOK;
    private int intBlue;
    private int intGreen;
    private int intRed;
    private OnButtonColorAddClickListener onButtonColorAddClickListener;
    private OnButtonColorRemoveClickListener onButtonColorRemoveClickListener;
    private OnButtonOKClickListener onButtonOKClickListener;
    private OnSetAngleListener onSetAngleListener;
    private final ViewKnob viewKnobBlue;
    private final ViewKnob viewKnobGreen;
    private final ViewKnob viewKnobRed;

    // constructors

    public LinearLayoutColor(Context context, float thetaRed, float thetaGreen, float thetaBlue)
    {
        super(context);

        buttonColorAdd = new Button(context);
        buttonColorAdd.setOnClickListener(this);
        buttonColorAdd.setText("+");

        buttonColorRemove = new Button(context);
        buttonColorRemove.setOnClickListener(this);
        buttonColorRemove.setText("-");

        buttonOK = new Button(context);
        buttonOK.setOnClickListener(this);
        buttonOK.setText("OK");

        onButtonColorAddClickListener = null;
        onButtonColorRemoveClickListener = null;
        onButtonOKClickListener = null;
        onSetAngleListener = null;

        viewKnobBlue = new ViewKnob(context, BLUE);
        viewKnobBlue.setAngle(thetaBlue);
        viewKnobBlue.setOnViewKnobTouchListener(this);

        viewKnobGreen = new ViewKnob(context, GREEN);
        viewKnobGreen.setAngle(thetaGreen);
        viewKnobGreen.setOnViewKnobTouchListener(this);

        viewKnobRed = new ViewKnob(context, RED);
        viewKnobRed.setAngle(thetaRed);
        viewKnobRed.setOnViewKnobTouchListener(this);

        intBlue = viewKnobBlue.getValue();
        intGreen = viewKnobGreen.getValue();
        intRed = viewKnobRed.getValue();

        LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, 0, 1);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.addView(buttonColorAdd, layoutParams);
        linearLayout.addView(buttonColorRemove, layoutParams);
        linearLayout.setOrientation(VERTICAL);

        layoutParams = new LayoutParams(0, MATCH_PARENT, 1);

        addView(viewKnobRed, layoutParams);
        addView(viewKnobGreen, layoutParams);
        addView(viewKnobBlue, layoutParams);
        addView(linearLayout, layoutParams);
        addView(buttonOK, layoutParams);
    }

    // interfaces

    public interface OnButtonColorAddClickListener
    {
        void onButtonColorAddClick(int color);
    }

    public interface OnButtonColorRemoveClickListener
    {
        void onButtonColorRemoveClick();
    }

    public interface OnButtonOKClickListener
    {
        void onButtonOKClick();
    }

    public interface OnSetAngleListener
    {
        void onSetAngle(float thetaRed, float thetaGreen, float thetaBlue);
    }

    // methods

    public void setButtonColorRemoveEnabled(boolean enabled)
    {
        buttonColorRemove.setEnabled(enabled);
    }

    public void setOnButtonColorAddClickListener(OnButtonColorAddClickListener listener)
    {
        onButtonColorAddClickListener = listener;
    }

    public void setOnButtonColorRemoveClickListener(OnButtonColorRemoveClickListener listener)
    {
        onButtonColorRemoveClickListener = listener;
    }

    public void setOnButtonOKClickListener(OnButtonOKClickListener listener)
    {
        onButtonOKClickListener = listener;
    }

    public void setOnSetAngleListener(OnSetAngleListener listener)
    {
        onSetAngleListener = listener;
    }

    @Override
    public void onClick(View view)
    {
        if (view == buttonColorAdd && onButtonColorAddClickListener != null)
        {
            onButtonColorAddClickListener.onButtonColorAddClick(rgb(intRed, intGreen, intBlue));
        }
        else if (view == buttonColorRemove && onButtonColorRemoveClickListener != null)
        {
            onButtonColorRemoveClickListener.onButtonColorRemoveClick();
        }
        else if (view == buttonOK && onButtonOKClickListener != null)
        {
            onButtonOKClickListener.onButtonOKClick();
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3)
    {
        float width = (getWidth() - getPaddingTop() * 6) / 5;

        View view;

        for (int index = 0; index < getChildCount(); index++)
        {
            view = getChildAt(index);
            view.layout((int)((getPaddingTop() + width) * index + getPaddingTop()), getPaddingTop(), (int)((getPaddingTop() + width) * (index + 1)), getHeight() - getPaddingBottom());
        }
    }

    @Override
    public void onViewKnobTouch()
    {
        intBlue = viewKnobBlue.getValue();
        intGreen = viewKnobGreen.getValue();
        intRed = viewKnobRed.getValue();

        onSetAngleListener.onSetAngle(viewKnobRed.getAngle(), viewKnobGreen.getAngle(), viewKnobBlue.getAngle());
    }
}