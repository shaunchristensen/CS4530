/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.18
 * Assignment: Project F - Lights Out
 */

// Trebuchet MS and Verdana fonts credit http://www.fontpalace.com/
// Tiger Electronics logo credit https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Tiger_Electronics_logo.svg/2000px-Tiger_Electronics_logo.svg.png

package edu.utah.cs.cs4530.lightsout.controller;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.utah.cs.cs4530.lightsout.R;
import edu.utah.cs.cs4530.lightsout.model.LightsOut;
import edu.utah.cs.cs4530.lightsout.view.Cell;

import static android.graphics.Typeface.createFromAsset;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class AppCompatActivityLightsOut extends AppCompatActivity implements OnClickListener, OnGlobalLayoutListener
{
    /*
    3 11/16 w x 6 h

top h * .05f

gradient_diagonal .5 h
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
5.5/40 h
1/40 h

select label
2/40 h
3/40 h
9.5/40 h


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

    // off button_control
    // #778899
    // #d8bfd8
    // #778899

    // off button_control pressed
    // #616f7d
    // #b19cb1
    // #616f7d

    // on button_control
    // #ff0000
    // #906f7d

    // #d10000
    // #765b66
    // on button_control pressed

    // purple
    // #8e4585
    // #4b0082

    // purple pressed
    // #b583b5
    // #3d006a

    // yellow
    // #ffff00
    // #ffd700

    // yellow pressed
    // #d1d100
    // #d1b000

    private boolean booleanPuzzle;
    private Button buttonHelp, buttonOnOff, buttonSelect, buttonSound, buttonStart;
    private ImageView imageView;
    private final int intColumns = LightsOut.getColumns();
    private final int intRows = LightsOut.getRows();
    private LinearLayout linearLayout;
    private List<Cell> listCells;
    private List<View> listButtons, listTextViews, listViews;;
    private Set<Integer> setCells;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        booleanPuzzle = true;
        Cell cell;
        int row;

        setContentView(R.layout.lights_out_portrait);

        buttonHelp = (Button)findViewById(R.id.buttonHelp);
        buttonHelp.setOnClickListener(this);

        buttonOnOff = (Button)findViewById(R.id.buttonOnOff);
        buttonOnOff.setOnClickListener(this);

        buttonSelect = (Button)findViewById(R.id.buttonSelect);
        buttonSelect.setOnClickListener(this);

        buttonSound = (Button)findViewById(R.id.buttonSound);
        buttonSound.setOnClickListener(this);

        buttonStart = (Button)findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(this);

        imageView = (ImageView)findViewById(R.id.tiger);

        listButtons = new ArrayList<>();
        listButtons.add(findViewById(R.id.buttonOnOff));
        listButtons.add(findViewById(R.id.buttonStart));
        listButtons.add(findViewById(R.id.buttonSound));
        listButtons.add(findViewById(R.id.buttonHelp));

        listCells = new ArrayList<>();

        for (int i = 0; i < intColumns * intRows; i++)
        {
            cell = new Cell(this, i);
            cell.setOnClickListener(this);

            row = i / intColumns;

            if (row == 0)
            {
                linearLayout = (LinearLayout)findViewById(R.id.linearLayoutRow0);
            }
            else if (row == 1)
            {
                linearLayout = (LinearLayout)findViewById(R.id.linearLayoutRow1);
            }
            else if (row == 2)
            {
                linearLayout = (LinearLayout)findViewById(R.id.linearLayoutRow2);
            }
            else if (row == 3)
            {
                linearLayout = (LinearLayout)findViewById(R.id.linearLayoutRow3);
            }
            else
            {
                linearLayout = (LinearLayout)findViewById(R.id.linearLayoutRow4);
            }

            linearLayout.addView(cell);
            listCells.add(cell);
        }

        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);

        listTextViews = new ArrayList<>();
        listTextViews.add(findViewById(R.id.textViewOnOff));
        listTextViews.add(findViewById(R.id.textViewStart));
        listTextViews.add(findViewById(R.id.textViewSound));
        listTextViews.add(findViewById(R.id.textViewHelp));

        listViews = new ArrayList<>();
        listViews.add(findViewById(R.id.viewOnOff));
        listViews.add(findViewById(R.id.viewStart));
        listViews.add(findViewById(R.id.viewSound));
        listViews.add(findViewById(R.id.viewHelp));

        setCells = new HashSet<>();

        Typeface typeFace = createFromAsset(getAssets(), "fonts/Trebuchet MS.ttf");

        ((TextView)findViewById(R.id.textViewLights)).setTypeface(typeFace);
        ((TextView)findViewById(R.id.textViewOut)).setTypeface(typeFace);
        ((TextView)findViewById(R.id.textViewTM)).setTypeface(typeFace);

        typeFace = createFromAsset(getAssets(), "fonts/Verdana.ttf");

        ((TextView)findViewById(R.id.textViewHelp)).setTypeface(typeFace, Typeface.BOLD_ITALIC);
        ((TextView)findViewById(R.id.textViewOnOff)).setTypeface(typeFace, Typeface.BOLD_ITALIC);
        ((TextView)findViewById(R.id.textViewSelect)).setTypeface(typeFace, Typeface.BOLD_ITALIC);
        ((TextView)findViewById(R.id.textViewSound)).setTypeface(typeFace, Typeface.BOLD_ITALIC);
        ((TextView)findViewById(R.id.textViewStart)).setTypeface(typeFace, Typeface.BOLD_ITALIC);
    }

    private void toggleCell(int cell)
    {
        listCells.get(cell).toggleCell();

        if (setCells.contains(cell))
        {
            setCells.remove(cell);
        }
        else
        {
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

            booleanPuzzle = setCells.size() > 0;
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

        float margin = linearLayout.getWidth() / 59f;
        float width = (linearLayout.getWidth() - margin * 12) / 5;

        LayoutParams layoutParams;

        for (int i = 0; i < listCells.size(); i++)
        {
            layoutParams = new LayoutParams(0, MATCH_PARENT, 1);

            if (i % intColumns == 0)
            {
                layoutParams.setMargins((int)(margin * 2), (int)(margin), (int)(margin), (int)(margin));
            }
            else if (i % intColumns < intColumns - 1)
            {
                layoutParams.setMargins((int)(margin), (int)(margin), (int)(margin), (int)(margin));
            }
            else
            {
                layoutParams.setMargins((int)(margin), (int)(margin), (int)(margin * 2), (int)(margin));
            }

            listCells.get(i).setLayoutParams(layoutParams);
        }

        layoutParams = new LayoutParams((int)(margin * 8 + width * 3), WRAP_CONTENT);

        findViewById(R.id.linearLayoutSelect).setLayoutParams(layoutParams);

        for (int i = 0; i < listButtons.size(); i++)
        {
            layoutParams = new LayoutParams((int)(width - margin), (int)(margin * 2));
            layoutParams.setMargins((int)(margin / 2), (int)(margin / 2), (int)(margin / 2), (int)(margin / 2));

            listButtons.get(i).setLayoutParams(layoutParams);
        }

        for (int i = 0; i < listTextViews.size(); i++)
        {
            layoutParams = (LayoutParams)listTextViews.get(i).getLayoutParams();
            layoutParams.setMargins(0, 0, (int)(margin * 2), 0);

            listTextViews.get(i).setLayoutParams(layoutParams);
        }

        for (int i = 0; i < listViews.size(); i++)
        {
            layoutParams = new LayoutParams(MATCH_PARENT, (int)margin);
            layoutParams.setMargins(0, (int)(margin / 2), 0, (int)margin);

            listViews.get(i).setLayoutParams(layoutParams);
        }

        imageView.setLayoutParams(new LayoutParams((int)(margin * 2000 / 336 * 3), (int)(margin * 3)));

        layoutParams = new LayoutParams((int)(margin * 24), (int)(margin * 11));
        layoutParams.setMargins(0, (int)(margin * 3), 0, (int)(findViewById(R.id.linearLayoutSelect).getHeight() * .5625 - margin * 19));

// fix resize text
        findViewById(R.id.linearLayoutLightsOut).setLayoutParams(layoutParams);

        layoutParams = new LayoutParams((int)(margin * 16), (int)(margin * 5));
        layoutParams.setMargins((int)margin, (int)margin, (int)margin, (int)margin);

        buttonSelect.setLayoutParams(layoutParams);

        layoutParams = (LayoutParams)findViewById(R.id.textViewSelect).getLayoutParams();
        layoutParams.setMargins(0, (int)margin, 0, 0);

        findViewById(R.id.textViewSelect).setLayoutParams(layoutParams);
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