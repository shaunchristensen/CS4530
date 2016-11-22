package edu.utah.cs.cs4530.lightsout.controller;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.utah.cs.cs4530.lightsout.R;
import edu.utah.cs.cs4530.lightsout.model.LightsOut;
import edu.utah.cs.cs4530.lightsout.view.Cell;

public class AppCompatActivityLightsOut extends AppCompatActivity implements OnClickListener, OnGlobalLayoutListener
{
    /*
    3 11/16 w x 6 h

top h * .05f

grid .5 h
1/59 w _ 1/59 w 9/59 w 1/59 w _ 1/59 w
1/96 h 8/96 h 1/96 h

bottom left
36/59 w
2.5/6 h

lights out
11/36 w
16/36 w
2/36 w
7/36 w

3/40 h
4/40 h
8/40 h
3/40 h

select
9/36 w
1/36 w
16/16 w
1/16 w
9/16 w

1/40 h
6/40 h
1/40 h

select label
2/40 h
3/40 h
9/40 h


bottom right
1//23 w
9/23 w
1/23w
10/23 w
2/23 w

7/40 h
.5/40 h x4
2/40 h
3/40 h
5/40 h
     */
    // #d3d3d3 light gray background
    // #696969 dim gray
    // #c0c0c0 silver

    // off button
    // #778899
    // #d8bfd8
    // #778899

    // off button pressed
    // #616f7d
    // #b19cb1
    // #616f7d

    // on button
    // #ff0000
    // #906f7d

    // #d10000
    // #765b66
    // on button pressed

    // purple
    // #8e4585
    // #4b0082

    // purple pressed
    // #b583b5
    // #3d006a

    // yellow
    // #ffff00
    // ffd700

    // yellow pressed
    // #d1d100
    // #d1b000

    private boolean booleanPuzzle;
    private final int intColumns = LightsOut.Columns;
    private final int intRows = LightsOut.Rows;
    private LinearLayout linearLayout;
    private List<Cell> listCells;
    private Set<Integer> setCells;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        booleanPuzzle = true;
        Cell cell;
        int row;

        setContentView(R.layout.activity_app_compat_lights_out);

        linearLayout = (LinearLayout)findViewById(R.id.LinearLayout);
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);

        listCells = new ArrayList<>();
        setCells = new HashSet<>();

        for (int i = 0; i < intColumns * intRows; i++)
        {
            cell = new Cell(this, i);
            cell.setOnClickListener(this);

            row = i / intColumns;

            if (row == 0)
            {
                ((LinearLayout)findViewById(R.id.LinearLayoutRow0)).addView(cell);
            }
            else if (row == 1)
            {
                ((LinearLayout)findViewById(R.id.LinearLayoutRow1)).addView(cell);
            }
            else if (row == 2)
            {
                ((LinearLayout)findViewById(R.id.LinearLayoutRow2)).addView(cell);
            }
            else if (row == 3)
            {
                ((LinearLayout)findViewById(R.id.LinearLayoutRow3)).addView(cell);
            }
            else
            {
                ((LinearLayout)findViewById(R.id.LinearLayoutRow4)).addView(cell);
            }

            listCells.add(cell);
        }
    }

    private void toggleCell(int cell)
    {
        if (setCells.contains(cell))
        {
            listCells.get(cell).setBackgroundResource(false);
            setCells.remove(cell);
        }
        else
        {
            listCells.get(cell).setBackgroundResource(true);
            setCells.add(cell);
        }
    }

    private void clickCell(int cell)
    {
        toggleCell(cell);

        if (booleanPuzzle)
        {
            if (cell / intColumns > 0)
            {
                toggleCell(cell - intColumns);
            }

            if (cell % intColumns > 0)
            {
                toggleCell(cell - 1);
            }

            if (cell % intColumns < intColumns - 1)
            {
                toggleCell(cell + 1);
            }

            if (cell / intColumns < intRows - 1)
            {
                toggleCell(cell + intColumns);
            }

            booleanPuzzle = setCells.size() > 0 ? true : false;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout()
    {
        linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        int width = linearLayout.getWidth();

        if (linearLayout.getHeight() > width)
        {

        }
        else
        {
            width /= 2;
        }

        float margin = width / 59;
        float height = margin * 8;

        int column;
        LayoutParams layoutParams;

        for (int i = 0; i < listCells.size(); i++)
        {
            column = i % intColumns;
            layoutParams = new LayoutParams(0, (int)height, 1);

            if (column == 0)
            {
                layoutParams.setMargins((int)(margin * 2), (int)(margin / 2), (int)margin, (int)(margin / 2));
            }
            else if (column < intColumns - 1)
            {
                layoutParams.setMargins((int)margin, (int)(margin / 2), (int)margin, (int)(margin / 2));
            }
            else
            {
                layoutParams.setMargins((int)margin, (int)(margin / 2), (int)(margin * 2), (int)(margin / 2));
            }

            listCells.get(i).setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v instanceof Cell)
        {
            clickCell(((Cell)v).getCell());
        }
    }
}