/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.18
 * Assignment: Project F - Lights Out
 */

package edu.utah.cs.cs4530.lightsout.view;

import android.content.Context;
import android.widget.Button;

import edu.utah.cs.cs4530.lightsout.R;

public class Cell extends Button
{
    // fields

    private boolean booleanOn;
    private final int intCell;

    // constructors

    public Cell(Context context, int cell)
    {
        super(context);

        intCell = cell;

        setBackgroundResource(R.drawable.cell_off);
    }

    // methods

    public int getCell()
    {
        return intCell;
    }

    public void toggleCell()
    {
        if (booleanOn)
        {
            booleanOn = false;

            setBackgroundResource(R.drawable.cell_off);
        }
        else
        {
            booleanOn = true;

            setBackgroundResource(R.drawable.cell_on);
        }
    }
}