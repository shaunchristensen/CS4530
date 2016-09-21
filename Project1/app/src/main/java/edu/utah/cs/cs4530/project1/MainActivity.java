package edu.utah.cs.cs4530.project1;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Shaun Christensen on 2016.09.17.
 */
public class MainActivity extends AppCompatActivity// implements Knob.OnKnobAngleChangedListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button b = new Button(this);
/*
        ViewPaint viewPaintRed = new ViewPaint(this, Color.RED);
        ViewPaint viewPaintBlue = new ViewPaint(this, Color.GREEN);
        ViewPaint viewPaintGreen = new ViewPaint(this, Color.BLUE);
        ViewPaint viewPaintYellow = new ViewPaint(this, Color.YELLOW);
        ViewPaint viewPaintPurple = new ViewPaint(this, Color.argb(255, 128, 0, 128));

        ViewGroupPalette viewGroupPalette = new ViewGroupPalette(this);
        viewGroupPalette.addView(viewPaintRed);
        viewGroupPalette.addView(viewPaintBlue);
        viewGroupPalette.addView(viewPaintGreen);
        viewGroupPalette.addView(viewPaintYellow);
        viewGroupPalette.addView(viewPaintPurple);
*/
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(b);
/*
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

        linearLayout.addView(viewGroupPalette);
*/
        setContentView(linearLayout);
    }
}