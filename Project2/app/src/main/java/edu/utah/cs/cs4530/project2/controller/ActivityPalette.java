/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.utah.cs.cs4530.project2.model.Palette;
import edu.utah.cs.cs4530.project2.view.LinearLayoutColor;
import edu.utah.cs.cs4530.project2.view.ViewGroupPalette;
import edu.utah.cs.cs4530.project2.view.ViewGroupPalette.OnSetColorListener;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;
import static android.graphics.Color.rgb;
import static android.widget.LinearLayout.*;
import static android.widget.LinearLayout.LayoutParams.*;
import static edu.utah.cs.cs4530.project2.view.LinearLayoutColor.*;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class ActivityPalette extends AppCompatActivity implements OnButtonColorAddClickListener, OnButtonColorRemoveClickListener, OnButtonOKClickListener, OnSetAngleListener, OnSetColorListener
{
    // fields

    private LinearLayoutColor linearLayoutColor;
    private Palette palette;
    private ViewGroupPalette viewGroupPalette;

    // methods

    private void deserialize(String tag)
    {
        try
        {
            FileInputStream fileInputStream = getApplicationContext().openFileInput("palette");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            palette = (Palette)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }
        catch (Exception e)
        {
            palette = Palette.getPalette();
            palette.addColor(RED);
            palette.addColor(BLUE);
            palette.addColor(GREEN);
            palette.addColor(YELLOW);
            palette.addColor(rgb(128, 0, 128));

            Log.e(tag, "Error: Unable to read the palette. " + e.getMessage());
        }
    }

    private void serialize(String tag)
    {
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), "palette"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(palette);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch(Exception e)
        {
            Log.e(tag, "Error: Unable to write the palette. " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        serialize("ActivityPalette.onBackPressed()");
    }

    @Override
    public void onButtonColorAddClick(int color)
    {
        palette.addColor(color);
        viewGroupPalette.addColor(color);
        linearLayoutColor.setButtonColorRemoveEnabled(true);
    }

    @Override
    public void onButtonColorRemoveClick()
    {
        palette.removeColor(palette.getColorIndex());
        viewGroupPalette.removeColor();

        if (palette.getColorCount() > 1)
        {
            linearLayoutColor.setButtonColorRemoveEnabled(true);
        }
        else
        {
            linearLayoutColor.setButtonColorRemoveEnabled(false);
        }
    }

    @Override
    public void onButtonOKClick()
    {
        serialize("ActivityPalette.onButtonOKClick()");
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        deserialize("ActivityPalette.onCreate()");

        int padding = (getResources().getDisplayMetrics().heightPixels > getResources().getDisplayMetrics().widthPixels ? getResources().getDisplayMetrics().heightPixels : getResources().getDisplayMetrics().widthPixels) / 100;
        int width = getResources().getDisplayMetrics().heightPixels < getResources().getDisplayMetrics().widthPixels ? getResources().getDisplayMetrics().heightPixels : getResources().getDisplayMetrics().widthPixels;
        int height = (width - padding * 6) / 4 + (padding * 2);
        int margin = (getResources().getDisplayMetrics().widthPixels - width) / 2;

        linearLayoutColor = new LinearLayoutColor(this, palette.getAngleRed(), palette.getAngleGreen(), palette.getAngleBlue());
        linearLayoutColor.setPadding(0, padding, 0, padding);
        linearLayoutColor.setOnButtonColorAddClickListener(this);
        linearLayoutColor.setOnButtonColorRemoveClickListener(this);
        linearLayoutColor.setOnButtonOKClickListener(this);
        linearLayoutColor.setOnSetAngleListener(this);

        viewGroupPalette = new ViewGroupPalette(this, palette.getColorIndex(), palette.getColors());
        viewGroupPalette.setPadding(padding, padding, padding, 0);
        viewGroupPalette.setOnSetColorListener(this);

        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.setMargins(margin, 0, margin, 0);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(viewGroupPalette, new LayoutParams(MATCH_PARENT, 0, 1));
        linearLayout.addView(linearLayoutColor, layoutParams);
        linearLayout.setOrientation(VERTICAL);

        setContentView(linearLayout);
    }

    @Override
    public void onSetAngle(float thetaRed, float thetaGreen, float thetaBlue)
    {
        palette.setAngleBlue(thetaBlue);
        palette.setAngleGreen(thetaGreen);
        palette.setAngleRed(thetaRed);
    }

    @Override
    public void onSetColor(int colorIndex)
    {
        palette.setColorIndex(colorIndex);
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        serialize("ActivityPalette.onStop()");
    }
}