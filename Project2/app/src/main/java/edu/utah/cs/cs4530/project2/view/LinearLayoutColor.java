/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class LinearLayoutColor extends LinearLayout implements View.OnClickListener, ViewKnob.OnValueChangeListener
{
    // fields

    private final Button buttonColorAdd;
    private final Button buttonColorRemove;
    private final Button buttonColorSave;
    private int intBlue;
    private int intGreen;
    private int intRed;
    private OnColorAddClickListener onColorAddClickListener;
    private OnColorRemoveClickListener onColorRemoveClickListener;
    private OnColorSaveClickListener onColorSaveClickListener;
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

        buttonColorSave = new Button(context);
        buttonColorSave.setOnClickListener(this);
        buttonColorSave.setText("Save");

        intBlue = intGreen = intRed = 255;

        onColorAddClickListener = null;
        onColorRemoveClickListener = null;
        onColorSaveClickListener = null;

        viewKnobBlue = new ViewKnob(context, Color.BLUE);
        viewKnobBlue.setOnValueChangeListener(this);

        viewKnobGreen = new ViewKnob(context, Color.GREEN);
        viewKnobGreen.setOnValueChangeListener(this);

        viewKnobRed = new ViewKnob(context, Color.RED);
        viewKnobRed.setOnValueChangeListener(this);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.addView(buttonColorAdd, new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
        linearLayout.addView(buttonColorRemove, new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        addView(viewKnobRed, new LayoutParams(0, LayoutParams.MATCH_PARENT, 2));
        addView(viewKnobGreen, new LayoutParams(0, LayoutParams.MATCH_PARENT, 2));
        addView(viewKnobBlue, new LayoutParams(0, LayoutParams.MATCH_PARENT, 2));
        addView(linearLayout, new LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
        addView(buttonColorSave, new LayoutParams(0, LayoutParams.MATCH_PARENT, 2));
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

    public interface OnColorSaveClickListener
    {
        void onColorSaveClick();
    }

    // methods

    public void setColorRemoveEnabled(boolean enabled)
    {
        buttonColorRemove.setEnabled(enabled);
    }

    public void setOnColorAddClickListener(OnColorAddClickListener listener)
    {
        onColorAddClickListener = listener;
    }

    public void setOnColorRemoveClickListener(OnColorRemoveClickListener listener)
    {
        onColorRemoveClickListener = listener;
    }

    public void setOnColorSaveClickListener(OnColorSaveClickListener listener)
    {
        onColorSaveClickListener = listener;
    }

    @Override
    public void onClick(View view)
    {
        if (view == buttonColorAdd)
        {
            onColorAddClickListener.onColorAddClick(Color.argb(255, intRed, intGreen, intBlue));
        }
        else if (view == buttonColorRemove)
        {
            onColorRemoveClickListener.onColorRemoveClick();
        }
        else if (view == buttonColorSave)
        {
            onColorSaveClickListener.onColorSaveClick();
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3)
    {
        Rect rect;
        View view;

        for (int index = 0; index < getChildCount(); index++)
        {
            rect = new Rect();
            rect.bottom = getHeight();

/*            if (index < getChildCount() - 1)
            {
                rect.left = (int)(index * getWidth() * 2 / 7);
                rect.right = (int)((index + 1) * getWidth() * 2 / 7);
            }
            else
            {
                rect.left = (int)(getWidth() * 6 / 7);
                rect.right = (int)getWidth();
            }*/

            rect.left = (int)(index * getWidth() * 5);
            rect.right = (int)((index + 1) * getWidth() * 5);

            rect.top = 0;

            view = getChildAt(index);
            view.layout(rect.left, rect.top, rect.right, rect.bottom);
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