/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.10
 * Assignment: Project 3 - MVC Battleship
 */

// ITC Machine Bold font http://www.onlinewebfonts.com
package edu.utah.cs.cs4530.project3.controller;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import edu.utah.cs.cs4530.project3.model.Battleship;
import edu.utah.cs.cs4530.project3.view.Grid;
import edu.utah.cs.cs4530.project3.view.LinearLayoutTemp;
import edu.utah.cs.cs4530.project3.view.Ship;

/**
 * Created by Shaun Christensen on 2016.10.10.
 */
public class ActivityMain extends AppCompatActivity
{
    // fields

    Battleship battleship;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        battleship = Battleship.getBattleship();
        battleship.startGame();

        TextView textView = new TextView(this);
        textView.setTextColor(Color.rgb(132, 132, 130));
        textView.setText("Battleship");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ITC Machine Bold.ttf"));

        Grid grid = new Grid(this, true, 10, 10, 0, 2, (Set<Integer>[])new HashSet<?>[2], (Set<Integer>[])new HashSet<?>[2], new Ship[2][5]);

        LinearLayoutTemp linearLayout = new LinearLayoutTemp(this);
//        linearLayout.addView(textView);
        linearLayout.setBackgroundColor(Color.rgb(64, 164, 223));

        setContentView(grid);
    }
}