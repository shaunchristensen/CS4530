/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.08.29
 * Assignment: Project 0 - Light Switch
 */

package edu.utah.cs.cs4530.lightswitch;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener
{
    ImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.off);

        LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 3);
        imageViewLayoutParams.topMargin = 100;

        Switch lightSwitch = new Switch(this);
        lightSwitch.setOnCheckedChangeListener(this);

        LinearLayout.LayoutParams lightSwitchLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1);
        lightSwitchLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(imageView, imageViewLayoutParams);
        linearLayout.addView(lightSwitch, lightSwitchLayoutParams);
        linearLayout.setBackgroundColor(Color.argb(255, 173, 216, 230));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        setContentView(linearLayout);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
    {
        if (b)
        {
            imageView.setImageResource(R.drawable.on);
            Log.i("Tag", "Toggle Light Switch On");
        }
        else
        {
            imageView.setImageResource(R.drawable.off);
            Log.i("Tag", "Toggle Light Switch Off");
        }
    }
}