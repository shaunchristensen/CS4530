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
public class LinearLayoutColor extends LinearLayout implements View.OnClickListener, ViewKnob.OnValueChangeListener
{
    // fields

    private final Button buttonColorAdd;
    private final Button buttonColorRemove;
    private int intBlue;
    private int intGreen;
    private int intRed;
    private OnColorAddClickListener onColorAddClickListener;
    private OnColorRemoveClickListener onColorRemoveClickListener;
    private final ViewKnob viewKnobBlue;
    private final ViewKnob viewKnobGreen;
    private final ViewKnob viewKnobRed;

    // constructors

    public LinearLayoutColor(Context context)
    {
        super(context);

        buttonColorAdd = new Button(context);
        buttonColorAdd.setOnClickListener(this);
        buttonColorAdd.setText("+");

        buttonColorRemove = new Button(context);
        buttonColorRemove.setOnClickListener(this);
        buttonColorRemove.setText("-");

        intBlue = intGreen = intRed = 255;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        onColorAddClickListener = null;
        onColorRemoveClickListener = null;

        viewKnobBlue = new ViewKnob(context, Color.BLUE);
        viewKnobBlue.setOnValueChangeListener(this);

        viewKnobGreen = new ViewKnob(context, Color.GREEN);
        viewKnobGreen.setOnValueChangeListener(this);

        viewKnobRed = new ViewKnob(context, Color.RED);
        viewKnobRed.setOnValueChangeListener(this);

        linearLayout.addView(buttonColorAdd, layoutParams);
        linearLayout.addView(buttonColorRemove, layoutParams);

        layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

        this.addView(viewKnobRed, layoutParams);
        this.addView(viewKnobGreen, layoutParams);
        this.addView(viewKnobBlue, layoutParams);
        this.addView(linearLayout);
    }

    // interfaces

    public interface OnColorAddClickListener
    {
        void onColorAddClick(int color);
    }

    public interface OnColorRemoveClickListener
    {
        void onColorRemoveClick();
    }

    // methods

    public void setEnabled(boolean enabled)
    {
        buttonColorRemove.setEnabled(enabled);
    }

    public void setOnColorAddClickListener(OnColorAddClickListener listener)
    {
        buttonColorRemove.setEnabled(true);
        onColorAddClickListener = listener;
    }

    public void setOnColorRemoveClickListener(OnColorRemoveClickListener listener)
    {
        onColorRemoveClickListener = listener;
    }

    @Override
    public void onClick(View view)
    {
        if (view == buttonColorAdd)
        {
            onColorAddClickListener.onColorAddClick(Color.argb(255, intRed, intBlue, intGreen));
        }
        else if (view == buttonColorRemove)
        {
            onColorRemoveClickListener.onColorRemoveClick();
        }
    }

    @Override
    public void onValueChange()
    {
        intBlue = viewKnobBlue.getValue();
        intGreen = viewKnobGreen.getValue();
        intRed = viewKnobRed.getValue();
    }
}