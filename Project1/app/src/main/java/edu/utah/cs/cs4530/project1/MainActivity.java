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

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(b);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

        ViewGroupPalette viewGroupPalette = new ViewGroupPalette(this);

        linearLayout.addView(viewGroupPalette);

        setContentView(linearLayout);
    }
}