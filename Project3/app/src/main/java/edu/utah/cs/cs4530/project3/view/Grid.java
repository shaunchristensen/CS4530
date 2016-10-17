/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.GridLayout;
import android.widget.TextView;

/**
 * Created by Shaun Christensen on 2016.10.10.
 */
public class Grid extends GridLayout
{
    // fields

    private final boolean booleanEnabled;
    private int intColumns;
    private int intRows;
    private int[] arrayShots;
    private Ship[] arrayShips;

    // constructors

    public Grid(Context context, boolean enabled, int rows, int columns, Ship[] ships, int[] shots)
    {
        super(context);

        booleanEnabled = enabled;

        setGrid(rows, columns, ships, shots);
    }

    // methods

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }

    public void setColumns(int columns)
    {
        intColumns = columns + 1;
    }

    public void setGrid(int rows, int columns, Ship[] ships, int[] shots)
    {
        setRows(rows);
        setColumns(columns);
        arrayShips = ships;
        arrayShots = shots;

        setColumnCount(intColumns);

        TextView textView;

        for (int i = 0; i < intRows * intColumns; i++)
        {
            if (i < intColumns)
            {
                if (i == 0)
                {
                    textView = new TextView(getContext());

                    addView(textView);
                }
                else
                {
                    textView = new TextView(getContext());
                    textView.setText(Integer.toString(i));

                    addView(textView);
                }
            }
            else
            {
                if (i % intColumns == 0)
                {
                    textView = new TextView(getContext());
                    textView.setText(Integer.toString(i / columns, 26));

                    addView(textView);
                }
                else
                {
                    addView(new Cell(getContext(), booleanEnabled, i % intColumns - 1, i / intColumns));
                }
            }
        }
    }

    public void setRows(int rows)
    {
        intRows = rows + 1;
    }

    public void setShips(Ship[] ships)
    {
        arrayShips = ships;
    }

    public void setShots(int[] shots)
    {
        arrayShots = shots;
    }
}