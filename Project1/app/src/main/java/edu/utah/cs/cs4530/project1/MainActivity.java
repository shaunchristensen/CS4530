package edu.utah.cs.cs4530.project1;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Shaun Christensen on 2016.09.17.
 */
public class MainActivity extends AppCompatActivity implements Knob.OnKnobAngleChangedListener, ViewPaint.OnPaintSelectedListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        KnobGroup knobGroup = new KnobGroup(this);

        Button b = new Button(this);
        Knob knob = new Knob(this, Color.YELLOW);
        knob.setOnKnobAngleChangedListener(this);
        ViewPaint v1 = new ViewPaint(this, Color.RED);
        v1.setOnPaintSelectedListener(this);
        ViewPaint v2 = new ViewPaint(this, Color.GREEN);
        v2.setOnPaintSelectedListener(this);
        ViewPaint v3 = new ViewPaint(this, Color.BLUE);
        v3.setOnPaintSelectedListener(this);

        LinearLayout l = new LinearLayout(this);
        l.setBackgroundColor(Color.RED);
        l.addView(b);
        l.addView(knob, new LinearLayout.LayoutParams(250, 300));

        l.addView(v1, 20, 20);
        l.addView(v2);//, new LinearLayout.LayoutParams(150, 150));
        l.addView(v3);//, new LinearLayout.LayoutParams(150, 150));

        setContentView(l);
    }

    @Override
    public void onPaintSelected(int color)
    {
        Log.i("Tag", "Paint " + color + " selected.");
    }

    @Override
    public void onKnobAngleChanged(float theta) {

    }
}