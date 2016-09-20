package edu.utah.cs4530.amp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements Knob.OnKnobAngleChangedListener, View.OnClickListener
{
    Button buttonAdd;
    Button buttonRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayoutHorizontal = new LinearLayout(this);

        LinearLayout linearLayoutVertical = new LinearLayout(this);
        linearLayoutVertical.setOrientation(LinearLayout.VERTICAL);

        buttonAdd = new Button(this);
        buttonAdd.setOnClickListener(this);
        buttonAdd.setText("+");

        buttonRemove = new Button(this);
        buttonRemove.setOnClickListener(this);
        buttonRemove.setText("-");

        linearLayoutVertical.addView(buttonAdd, new LinearLayout.LayoutParams(150, 150));
        linearLayoutVertical.addView(buttonRemove, new LinearLayout.LayoutParams(150, 150));

        Knob knobRed = new Knob(this, Color.RED);
        knobRed.setOnAngleChangedListener(this);

        Knob knobGreen = new Knob(this, Color.GREEN);
        knobGreen.setOnAngleChangedListener(this);

        Knob knobBlue = new Knob(this, Color.BLUE);
        knobBlue.setOnAngleChangedListener(this);

        linearLayoutHorizontal.addView(knobRed, new LinearLayout.LayoutParams(250, 250));
        linearLayoutHorizontal.addView(knobGreen, new LinearLayout.LayoutParams(250, 250));
        linearLayoutHorizontal.addView(knobBlue, new LinearLayout.LayoutParams(250, 250));
        linearLayoutHorizontal.addView(linearLayoutVertical);

        setContentView(linearLayoutHorizontal);
    }

    public void onKnobAngleChanged(float theta)
    {
        Log.i("Tag", "Knob angle changed to " + theta);
    }

    @Override
    public void onClick(View view)
    {
        buttonRemove.setEnabled(false);
    }
}