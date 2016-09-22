/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.22
 * Assignment: Project 1 - Palette Paint
 */

package edu.utah.cs.cs4530.project1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * Created by Shaun Christensen on 2016.09.17.
 */
public class MainActivity extends AppCompatActivity implements LinearLayoutColor.OnColorAddClickListener, LinearLayoutColor.OnColorRemoveClickListener, ViewGroupPalette.OnColorChangeListener
{
    private LinearLayoutColor linearLayoutColor;
    private ViewCanvas viewCanvas;
    private ViewGroupPalette viewGroupPalette;

    @Override
    public void onColorAddClick(int color)
    {
        linearLayoutColor.setColorRemoveEnabled(true);
        viewGroupPalette.addColor(color);
    }

    @Override
    public void onColorChange(int color)
    {
        viewCanvas.setColor(color);
    }

    @Override
    public void onColorRemoveClick()
    {
        if (viewGroupPalette.removeColor() > 1)
        {
            linearLayoutColor.setColorRemoveEnabled(true);
        }
        else
        {
            linearLayoutColor.setColorRemoveEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int height = this.getResources().getDisplayMetrics().heightPixels;
        int width = this.getResources().getDisplayMetrics().widthPixels;

        LayoutParams layoutParmsColor = new LayoutParams((int)(width * .6f), 0, 15);
        layoutParmsColor.setMargins((int)(width * .2f), (int)(height * .01f), (int)(width * .2f), (int)(height * .01f));

        linearLayoutColor = new LinearLayoutColor(this);
        linearLayoutColor.setOnColorAddClickListener(this);
        linearLayoutColor.setOnColorRemoveClickListener(this);

        viewCanvas = new ViewCanvas(this);

        viewGroupPalette = new ViewGroupPalette(this);
        viewGroupPalette.setOnColorChangeListener(this);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(viewCanvas, new LayoutParams(width, 0, 50));
        linearLayout.addView(viewGroupPalette, new LayoutParams(width, 0, 33));
        linearLayout.addView(linearLayoutColor, layoutParmsColor);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        setContentView(linearLayout);
    }
}