/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.view;

import android.content.Context;
import android.widget.Button;

import static android.graphics.Color.rgb;

public class Cell extends Button
{
    // fields

    private final int intCell;

    // constructors

    public Cell(Context context, boolean enabled, int cell)
    {
        super(context);

        intCell = cell;

        setBackgroundColor(rgb(64, 164, 223));
        setEnabled(enabled);
        setZ(0);
    }

    // methods

    public int getCell()
    {
        return intCell;
    }
}