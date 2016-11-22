package edu.utah.cs.cs4530.lightsout.view;

import android.content.Context;
import android.widget.Button;

import edu.utah.cs.cs4530.lightsout.R;

public class Cell extends Button
{
    // fields

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

    public void setBackgroundResource(boolean on)
    {
        if (on)
        {
            setBackgroundResource(R.drawable.cell_on);
        }
        else
        {
            setBackgroundResource(R.drawable.cell_off);
        }
    }
}