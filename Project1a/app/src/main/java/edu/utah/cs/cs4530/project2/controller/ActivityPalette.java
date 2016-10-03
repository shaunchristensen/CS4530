/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import edu.utah.cs.cs4530.project2.view.LinearLayoutColor;
import edu.utah.cs.cs4530.project2.view.ViewGroupPalette;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */

public class ActivityPalette extends AppCompatActivity
{
    // fields

    private LinearLayoutColor linearLayoutColor;
    private ViewGroupPalette viewGroupPalette;
    /*
    Intent returnIntent = new Intent();
    returnIntent.putExtra("result",result);
    setResult(Activity.RESULT_OK,returnIntent);
    finish();
    */

    // methods

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        linearLayoutColor = new LinearLayoutColor(this);

        viewGroupPalette = new ViewGroupPalette(this);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(viewGroupPalette, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        linearLayout.addView(linearLayoutColor, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        setContentView(linearLayout);
    }
}