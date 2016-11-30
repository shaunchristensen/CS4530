/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.18
 * Assignment: Project F - Lights Out
 * Credits:    Alegrerya Sans Black Italic Font - http://www.1001fonts.com/alegreya-sans-font.html
 *             Built Titling Light Font - http://www.dafont.com/built-titling.font
 *             Tiger Electronics Logo - https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Tiger_Electronics_logo.svg/2000px-Tiger_Electronics_logo.svg.png
 */

package edu.utah.cs.cs4530.lightsout.controller;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
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

public class AppCompatActivityLightsOut extends AppCompatActivity implements OnClickListener, OnGlobalLayoutListener
{
    // fields

    private boolean booleanPuzzle;
    private Button buttonHelp, buttonOnOff, buttonSelect, buttonSound, buttonStart;
    private final int intColumns = LightsOut.getColumns();
    private final int intRows = LightsOut.getRows();
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
        LinearLayout linearLayout;

        setContentView(R.layout.lights_out);

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

        Typeface typeFace = createFromAsset(getAssets(), "fonts/Alegreya Sans Black Italic.ttf");

        ((TextView)findViewById(R.id.textViewHelp)).setTypeface(typeFace);
        ((TextView)findViewById(R.id.textViewOnOff)).setTypeface(typeFace);
        ((TextView)findViewById(R.id.textViewSelect)).setTypeface(typeFace);
        ((TextView)findViewById(R.id.textViewSound)).setTypeface(typeFace);
        ((TextView)findViewById(R.id.textViewStart)).setTypeface(typeFace);
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

        findViewById(R.id.linearLayout).getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout()
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        float width = linearLayout.getHeight() < linearLayout.getWidth() ? linearLayout.getWidth() / 2 : linearLayout.getWidth();
        float length = width / 59;
        width = (width - length * 12) / 5;

        LayoutParams layoutParams;

        for (int i = 0; i < listCells.size(); i++)
        {
            layoutParams = new LayoutParams((int)(length * 9.4), (int)(length * 7.6));

            if (i % intColumns == 0)
            {
                layoutParams.setMargins((int)(length * 2), (int)(length), (int)(length), (int)(length));
            }
            else if (i % intColumns < intColumns - 1)
            {
                layoutParams.setMargins((int)(length), (int)(length), (int)(length), (int)(length));
            }
            else
            {
                layoutParams.setMargins((int)(length), (int)(length), (int)(length * 2), (int)(length));
            }

            listCells.get(i).setLayoutParams(layoutParams);
        }

        findViewById(R.id.linearLayoutSelect).setLayoutParams(new LayoutParams((int)(length * 8 + width * 3), MATCH_PARENT));

        for (int i = 0; i < listButtons.size(); i++)
        {
            layoutParams = new LayoutParams((int)(width - length), (int)(length * 2));
            layoutParams.setMargins((int)(length / 2), (int)(length / 2), (int)(length / 2), (int)(length / 2));

            listButtons.get(i).setLayoutParams(layoutParams);
        }

        for (int i = 0; i < listTextViews.size(); i++)
        {
            layoutParams = (LayoutParams)listTextViews.get(i).getLayoutParams();
            layoutParams.setMargins(0, 0, (int)(length * 2), 0);

            listTextViews.get(i).setLayoutParams(layoutParams);
        }

        for (int i = 0; i < listViews.size(); i++)
        {
            layoutParams = new LayoutParams(MATCH_PARENT, (int)length);
            layoutParams.setMargins(0, (int)(length / 2), 0, (int)length);

            listViews.get(i).setLayoutParams(layoutParams);
        }

        findViewById(R.id.tiger).setLayoutParams(new LayoutParams((int)(length * 2000 / 336 * 3), (int)(length * 3)));

        layoutParams = new LayoutParams((int)(length * 18), (int)(length * 12));
        layoutParams.setMargins((int)length, (int)(length * 3), 0, 0);

        findViewById(R.id.linearLayoutLogo).setLayoutParams(layoutParams);

        layoutParams = new LayoutParams((int)(length * 16.5), (int)(length * 4.5));
        layoutParams.setMargins(0, 0, 0, (int)(length / 2));

        findViewById(R.id.imageViewLights).setLayoutParams(layoutParams);

        layoutParams = new LayoutParams((int)(length * 4.5), (int)(length * 7));
        layoutParams.setMargins((int)(length / 2), 0, 0, 0);

        findViewById(R.id.imageViewViewOut).setLayoutParams(layoutParams);

        layoutParams = new LayoutParams((int)length, (int)length);
        layoutParams.setMargins((int)(length / 2), 0, 0, 0);

        findViewById(R.id.imageViewTM).setLayoutParams(layoutParams);

        layoutParams = new LayoutParams((int)(length * 16), (int)(length * 5));
        layoutParams.setMargins((int)length, (int)length, (int)length, (int)length);

        buttonSelect.setLayoutParams(layoutParams);

        layoutParams = (LayoutParams)findViewById(R.id.textViewSelect).getLayoutParams();
        layoutParams.setMargins(0, (int)length, 0, 0);

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