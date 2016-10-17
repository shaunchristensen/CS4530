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

/**
 * Created by Shaun Christensen on 2016.10.10.
 */
public class Cell extends Button
{
    // fields

    private final int intColumn;
    private final int intRow;

    // constructors

    public Cell(Context context, boolean enabled, int row, int column)
    {
        super(context);

        intColumn = column;
        intRow = row;

        setBackgroundColor(rgb(64, 164, 223));
        setEnabled(enabled);
    }

    // methods

    public int getColumn()
    {
        return intColumn;
    }

    public int getRow()
    {
        return intRow;
    }
}