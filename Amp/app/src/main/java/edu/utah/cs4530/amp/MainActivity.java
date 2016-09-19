package edu.utah.cs4530.amp;

import android.graphics.Color;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements KnobView.OnAngleChangedListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);

        final KnobView knobViewVolume = new KnobView(this);
        knobViewVolume.setBackgroundColor(Color.RED);
        knobViewVolume.setOnAngleChangedListener(this);
        linearLayout.addView(knobViewVolume, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 500, 0));

        final KnobView knobViewBass = new KnobView(this);
        knobViewBass.setBackgroundColor(Color.GRAY);
        knobViewBass.setOnAngleChangedListener(this);
        linearLayout.addView(knobViewBass, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 500, 0));

        final KnobView knobViewFade = new KnobView(this);
        knobViewFade.setBackgroundColor(Color.BLUE);
        knobViewFade.setOnAngleChangedListener(this);
        linearLayout.addView(knobViewFade, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 500, 0));

//        knobView.setPadding(20, 40, 60, 80);
//        knobView.setTheta((float) Math.PI * 1.25f);
//        view.setGravity(Gravity.CENTER_VERTICAL);

        setContentView(linearLayout);
    }

    public void onAngleChanged(float theta)
    {
        float volume = theta;
        Log.i("Tag", "Volume changed to: " + volume);
    }
}