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
import android.graphics.Path;
import android.view.View;
import android.widget.FrameLayout;
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

public class GridLayoutBattleship extends GridLayout
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

    public GridLayoutBattleship(Context context, boolean enabled, int rows, int columns, int player, int players, Set<Integer>[] hits, Set<Integer>[] misses, Ship[][] ships)
    {
        super(context);

        arrayCells = new Cell[rows * columns];
        intColumns = columns;
        intPlayers = players;
        intRows = rows;

        columns++;

//        int length = (int)(floatLength - floatMargin);

        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.addView(new GridLayoutGrid(context));
        frameLayout.addView(new ViewGrid(context));

        TextView textView;

        for (int i = 0; i < rows + columns; i++)
        {
            textView = new TextView(getContext());
            textView.setBackgroundColor(WHITE);

            if (i > 0 && i < columns)
            {
                textView.setText(Integer.toString(i));
            }
            else
            {
                textView.setText(Integer.toString(i / columns, 26));
            }

            addView(textView);
        }

        addView(frameLayout);
        setBackgroundColor(BLACK);
        setColumnCount(columns);
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

        ships[0][0] = new Carrier(context, 5, 90, 2.5f, .5f);
        ships[0][1] = new Battleship(context, 4, 90, 2, 1.5f);
        ships[0][2] = new Cruiser(context, 3, 90, 1.5f, 2.5f);
        ships[0][3] = new Submarine(context, 3, 90, 1.5f, 3.5f);
        ships[0][4] = new Destroyer(context, 2, 90, 1, 4.5f);
        ships[1][0] = new Carrier(context, 5, 90, 2.5f, 5.5f);
        ships[1][1] = new Battleship(context, 4, 90, 2, 6.5f);
        ships[1][2] = new Cruiser(context, 3, 90, 1.5f, 7.5f);
        ships[1][3] = new Submarine(context, 3, 90, 1.5f, 8.5f);
        ships[1][4] = new Destroyer(context, 2, 90, 1, 9.5f);

        setEnabled();
        invalidate();
    }

    // methods

    public void addHit(int cell)
    {
        if (arrayHits[intPlayer] != null)
        {
            arrayHits[intPlayer].add(cell);

            invalidate();
        }
    }

    public void addMiss(int cell)
    {
        if (arrayMisses[intPlayer] != null)
        {
            arrayMisses[intPlayer].add(cell);

            invalidate();
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

            if (i < intColumns + 1)
            {
                view.layout((int)(i % (intColumns + 1) * floatLength), 0, (int)((i % (intColumns + 1) + 1) * floatLength - floatMargin), (int)(floatLength - floatMargin));
            }
            else if (i < intColumns + intRows + 1)
            {
                view.layout(0, (int)((i - intColumns) * floatLength), (int)(floatLength - floatMargin), (int)((i - intColumns + 1) * floatLength - floatMargin));
            }
            else
            {
                view.layout((int)floatLength, (int)floatLength, (int)((intColumns + 1) * floatLength), (int)((intRows + 1) * floatLength));
            }
        }
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

    // classes

    private class GridLayoutGrid extends GridLayout
    {
        // constructors

        public GridLayoutGrid(Context context)
        {
            super(context);

            Cell cell;

            for (int i = 0; i < intRows * intColumns; i++)
            {
                cell = new Cell(context, booleanEnabled, i);

                arrayCells[i] = cell;

                addView(cell);
            }

            setColumnCount(intColumns);
        }

        // methods

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom)
        {
            super.onLayout(changed, left, top, right, bottom);

            View view;

            for (int i = 0; i < getChildCount(); i++)
            {
                view = getChildAt(i);
                view.layout((int)(i % intColumns * floatLength), (int)(i / intColumns * floatLength), (int)((i % intColumns + 1) * floatLength - floatMargin), (int)((i / intColumns + 1) * floatLength - floatMargin));
            }
        }
    }

    private class ViewGrid extends View
    {
        // constructors

        public ViewGrid(Context context)
        {
            super(context);
        }

        // methods

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);

            if (arrayHits[intPlayer] != null && arrayMisses[intPlayer] != null)
            {
                int i;

                Paint paint = new Paint(ANTI_ALIAS_FLAG);
                paint.setStrokeWidth(floatLength / 50);

                Path path;

                for (Ship s : arrayShips[intPlayer])
                {
                    if (arrayShips[intPlayer] != null)
                    {
                        path = s.getPath(floatLength, floatMargin);

                        paint.setColor(rgb(132, 132, 130));
                        paint.setStyle(Style.FILL);

                        canvas.drawPath(path, paint);

                        paint.setColor(BLACK);
                        paint.setStyle(STROKE);

                        canvas.drawPath(path, paint);
                    }
                }

                for (Cell c : arrayCells)
                {
                    i = c.getCell();

                    if (arrayHits[intPlayer].contains(i))
                    {
                        paint.setColor(RED);
                    }
                    else if (arrayMisses[intPlayer].contains(i))
                    {
                        paint.setColor(WHITE);
                    }
                    else
                    {
                        paint.setColor(BLACK);
                    }

                    paint.setStyle(Style.FILL);

                    canvas.drawCircle(i % intColumns * floatLength + (floatLength - floatMargin) / 2, i / intColumns * floatLength + (floatLength - floatMargin) / 2, floatLength / 10, paint);

                    paint.setColor(BLACK);
                    paint.setStyle(STROKE);

                    canvas.drawCircle(i % intColumns * floatLength + (floatLength - floatMargin) / 2, i / intColumns * floatLength + (floatLength - floatMargin) / 2, floatLength / 10, paint);
                }
            }
        }
    }
}