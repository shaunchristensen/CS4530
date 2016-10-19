/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.rgb;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.Paint.Style.STROKE;

public class Grid extends GridLayout
{
    // fields

    private boolean booleanEnabled;
    private final Cell[] arrayCells;
    private final int intColumns;
    private int intPlayer;
    private final int intPlayers;
    private final int intRows;
    private float floatLength;
    private float floatMargin;
    private Set<Integer>[] arrayHits;
    private Set<Integer>[] arrayMisses;
    private Ship[][] arrayShips;

    // constructors

    public Grid(Context context, boolean enabled, int rows, int columns, int player, int players, Set<Integer>[] hits, Set<Integer>[] misses, Ship[][] ships)
    {
        super(context);

        arrayCells = new Cell[rows * columns];

        for (int i = 0; i < rows * columns; i++)
        {
            arrayCells[i] = new Cell(context, enabled, i);
        }

        intColumns = columns;
        intPlayers = players;
        intRows = rows;

        columns++;
        rows++;

        TextView textView;

        for (int i = 0; i < rows * columns; i++)
        {
            if (i < columns)
            {
                if (i == 0)
                {
                    textView = new TextView(getContext());
                    textView.setBackgroundColor(WHITE);

                    addView(textView);
                }
                else
                {
                    textView = new TextView(getContext());
                    textView.setBackgroundColor(WHITE);
                    textView.setText(Integer.toString(i));

                    addView(textView);
                }
            }
            else
            {
                if (i % columns == 0)
                {
                    textView = new TextView(getContext());
                    textView.setBackgroundColor(WHITE);
                    textView.setText(Integer.toString(i / columns, 26));

                    addView(textView);
                }
                else
                {
                    addView(arrayCells[i - i / columns - columns]);
                }
            }
        }

        setBackgroundColor(BLACK);
        setColumnCount(intColumns);
        setGrid(enabled, player, hits, misses, ships);






        hits[0] = new HashSet<>();
        hits[1] = new HashSet<>();
        hits[0].add(0);
        hits[0].add(12);
        hits[0].add(24);
        hits[0].add(36);
        hits[0].add(48);
        hits[1].add(10);
        hits[1].add(20);
        hits[1].add(30);
        hits[1].add(40);
        hits[1].add(50);

        misses[0] = new HashSet<>();
        misses[1] = new HashSet<>();
        misses[0].add(1);
        misses[0].add(13);
        misses[0].add(25);
        misses[0].add(37);
        misses[0].add(49);
        misses[1].add(15);
        misses[1].add(25);
        misses[1].add(35);
        misses[1].add(45);
        misses[1].add(55);

        ships[0][0] = new Carrier(context, 5, 90, 0, 0);
        ships[0][1] = new Battleship(context, 4, 180, 9, 0);
        ships[0][2] = new Cruiser(context, 3, 270, 9, 9);
        ships[0][3] = new Submarine(context, 3, 0, 0, 9);
        ships[0][4] = new Destroyer(context, 2, 90, 4, 4);
        ships[1][0] = new Carrier(context, 5, 90, 0, 1);
        ships[1][1] = new Battleship(context, 4, 180, 8, 9);
        ships[1][2] = new Cruiser(context, 3, 270, 9, 8);
        ships[1][3] = new Submarine(context, 3, 0, 1, 9);
        ships[1][4] = new Destroyer(context, 2, 90, 5, 5);

        setEnabled();
        invalidate();
    }

    // methods

    public void addHit(int cell)
    {
        arrayHits[intPlayer].add(cell);

        invalidate();
    }

    public void addMiss(int cell)
    {
        arrayMisses[intPlayer].add(cell);

        invalidate();
    }

    public void setEnabled()
    {
        if (booleanEnabled && arrayHits[intPlayer] != null && arrayMisses[intPlayer] != null)
        {
            for (Cell c : arrayCells)
            {
                if (arrayHits[intPlayer].contains(c.getCell()) || arrayMisses[intPlayer].contains(c.getCell()))
                {
                    c.setEnabled(false);
                }
                else
                {
                    c.setEnabled(true);
                }
            }
        }
        else
        {
            for (Cell c : arrayCells)
            {
                c.setEnabled(false);
            }
        }
    }

    public void setEnabled(boolean enabled)
    {
        booleanEnabled = enabled;

        setEnabled();
    }

    public void setGrid(boolean enabled, int player, Set<Integer>[] hits, Set<Integer>[] misses, Ship[][] ships)
    {
        arrayHits = hits;
        arrayMisses = misses;
        arrayShips = ships;
        booleanEnabled = enabled;

        setPlayer(player);
    }

    public void setPlayer(int player)
    {
        intPlayer = player;

        setEnabled();

        invalidate();
    }
















    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(floatLength / 50);

        for (Ship s : arrayShips[intPlayer])
        {
            paint.setColor(rgb(132, 132, 130));
            paint.setStyle(Style.FILL);

            canvas.drawPath(s.getPath(floatLength, floatLength), paint);

            paint.setColor(BLACK);
            paint.setStyle(STROKE);

            canvas.drawPath(s.getPath(floatLength, floatLength), paint);
        }

        paint.setStyle(Style.FILL);

        for (Cell c : arrayCells)
        {
            if (arrayHits[intPlayer].contains(c.getCell()))
            {
                paint.setColor(RED);
            }
            else if (arrayMisses[intPlayer].contains(c.getCell()))
            {
                paint.setColor(WHITE);
            }
            else
            {
                paint.setColor(BLACK);
            }

            canvas.drawCircle((c.getCell() % (intColumns + 1) + 1) * floatLength + floatLength / 2, (c.getCell() / (intColumns + 1) + 1) * floatLength + floatLength / 2, floatLength / 10, paint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        floatLength = (getHeight() < getWidth() ? getHeight() : getWidth()) / ((intColumns > intRows ? intColumns : intRows) + 1);
        floatMargin = floatLength / 10;

        View view;

        for (int i = 0; i < getChildCount(); i++)
        {
            view = getChildAt(i);
            view.layout((int)((i % (intColumns + 1)) * floatLength), (int)((i / (intColumns + 1)) * floatLength), (int)((i % (intColumns + 1)) * floatLength + floatLength/* - floatMargin*/), (int)((i / (intColumns + 1)) * floatLength + floatLength/* - floatMargin*/));
        }
    }
}