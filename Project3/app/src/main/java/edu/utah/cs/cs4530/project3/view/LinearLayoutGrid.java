/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.19
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import edu.utah.cs.cs4530.project3.view.ship.Ship;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.rgb;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.Paint.Style.STROKE;
import static android.view.ViewTreeObserver.*;
import static edu.utah.cs.cs4530.project3.view.Cell.*;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class LinearLayoutGrid extends LinearLayout implements OnCellClickListener, OnGlobalLayoutListener
{
    // fields

    private boolean booleanStatus;
    private final Grid gridOpponent, gridPlayer;
    private final int intColumnsCount, intPadding, intRowsCount;
    private int intOpponent, intPlayer;
    private final List<List<Cell>> listCells;
    private final LinearLayout linearLayoutOpponent, linearLayoutPlayer;
    private List<List<Ship>> listShips;
    private List<Set<Integer>> listHits, listMisses;
    private final OnShootListener onShootListener;
    private String stringOpponent, stringPlayer;
    private final TextView textViewOpponent, textViewPlayer;

    // constructors

    public LinearLayoutGrid(Context context, int rowsCount, int columnsCount, int padding, List<Integer> players, OnShootListener listener)
    {
        super(context);

        intColumnsCount = columnsCount;
        intPadding = padding;
        intRowsCount = rowsCount;
        listHits = new ArrayList<>();
        listMisses = new ArrayList<>();
        listShips = new ArrayList<>();

        List<List<Cell>> cells = new ArrayList<>();

        for (int i : players)
        {
            cells.add(new ArrayList<Cell>());

            for (int j = 0; j < intRowsCount * intColumnsCount; j++)
            {
                cells.get(i).add(new Cell(context, j, this));
            }

            listHits.add(new HashSet<Integer>());
            listMisses.add(new HashSet<Integer>());
            listShips.add(new ArrayList<Ship>());
        }

        listCells = Collections.unmodifiableList(cells);
        gridOpponent = new Grid(context, false, 0);
        gridPlayer = new Grid(context, true, 1);
        onShootListener = listener;

        textViewOpponent = new TextView(context);
        textViewOpponent.setText(stringOpponent);
        textViewOpponent.setTextColor(BLACK);
        textViewOpponent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        textViewPlayer = new TextView(context);
        textViewPlayer.setText(stringPlayer);
        textViewPlayer.setTextColor(BLACK);
        textViewPlayer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        linearLayoutOpponent = new LinearLayout(context);
        linearLayoutOpponent.addView(textViewOpponent);
        linearLayoutOpponent.addView(gridOpponent);
        linearLayoutOpponent.setOrientation(VERTICAL);

        linearLayoutPlayer = new LinearLayout(context);
        linearLayoutPlayer.addView(textViewPlayer);
        linearLayoutPlayer.addView(gridPlayer);
        linearLayoutPlayer.setOrientation(VERTICAL);

        addView(linearLayoutOpponent);
        addView(linearLayoutPlayer);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    // interfaces

    public interface OnShootListener
    {
        void onShoot(int cell);
    }

    // methods

    public void addShot(boolean hit, int cell)
    {
        if (hit)
        {
            listHits.get(intOpponent).add(cell);
        }
        else
        {
            listMisses.get(intOpponent).add(cell);
        }
    }

    @Override
    public void onCellClick(int cell)
    {
        onShootListener.onShoot(cell);
    }

    @Override
    public void onGlobalLayout()
    {
        int height, length, maxHeight, width;
        LayoutParams layoutParams;

        textViewOpponent.measure(0, 0);
        textViewPlayer.measure(0, 0);

        height = getHeight();
        maxHeight = max(textViewOpponent.getMeasuredHeight(), textViewPlayer.getMeasuredHeight());
        width = getWidth();

        if (height < width)
        {
            width = (width - intPadding) / 2;
            layoutParams = new LayoutParams(width + intPadding, height);

            linearLayoutOpponent.setPadding(0, 0, intPadding, 0);
            setOrientation(HORIZONTAL);
        }
        else
        {
            height = (height - intPadding) / 2;
            layoutParams = new LayoutParams(width, height + intPadding);

            linearLayoutOpponent.setPadding(0, 0, 0, intPadding);
            setOrientation(VERTICAL);
        }

        linearLayoutOpponent.setLayoutParams(layoutParams);

        layoutParams = new LayoutParams(width, height);

        linearLayoutPlayer.setLayoutParams(layoutParams);

        height -= maxHeight;
        length = min(height, width);

        layoutParams = new LayoutParams(length, length);
        layoutParams.setMargins(0, 0, height - length, width - length);

        gridOpponent.setLayoutParams(layoutParams);
        gridOpponent.requestLayout();

        gridPlayer.setLayoutParams(layoutParams);
        gridPlayer.requestLayout();

        layoutParams = new LayoutParams(width, maxHeight);

        textViewOpponent.setLayoutParams(layoutParams);
        textViewOpponent.setPadding(0, 0, 0, intPadding);

        textViewPlayer.setLayoutParams(layoutParams);
        textViewPlayer.setPadding(0, 0, 0, intPadding);
    }

    public void setGame(boolean status, int opponent, int player, List<List<Ship>> ships, List<Set<Integer>> hits, List<Set<Integer>> misses)
    {
        listHits = hits;
        listMisses = misses;
        listShips = ships;

        setStatus(status);
        setPlayers(opponent, player);
    }

    public void setPlayers(int opponent, int player)
    {
        intOpponent = opponent;
        intPlayer = player;

        gridOpponent.invalidate();
        gridOpponent.setEnabled();

        gridPlayer.invalidate();
        gridPlayer.setEnabled();

        if (booleanStatus)
        {
            stringOpponent = "Opponent";
            stringPlayer = "Player " + (intPlayer + 1);
        }
        else
        {
            stringOpponent = "Player " + (intOpponent + 1) + " lost.";
            stringPlayer = "Player " + (intPlayer + 1) + " won!";
        }

        textViewOpponent.setText(stringOpponent);
        textViewPlayer.setText(stringPlayer);
    }

    public void setStatus(boolean status)
    {
        booleanStatus = status;
    }

    // classes

    private class Grid extends GridLayout implements OnGlobalLayoutListener
    {
        // fields

        private final boolean booleanPlayer;
        private float floatLength;
        private float floatMargin;
        private final int intCellsIndex;
        private final GridLayoutGrid gridLayoutGrid;
        private final ViewGrid viewGrid;

        // constructors

        public Grid(Context context, boolean player, int cellsIndex)
        {
            super(context);

            booleanPlayer = player;
            intCellsIndex = cellsIndex;
            gridLayoutGrid = new GridLayoutGrid(context);
            viewGrid = new ViewGrid(context);

            for (int i = 0; i < intRowsCount + intColumnsCount + 1; i++)
            {
                TextView textView = new TextView(context);
                textView.setBackgroundColor(WHITE);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(BLACK);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.5f);

                if (i > 0 && i < intColumnsCount + 1)
                {
                    textView.setText(Integer.toString(i));
                }
                else
                {
                    textView.setText(getRowString(i - intColumnsCount));
                }

                addView(textView);
            }

            FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.addView(gridLayoutGrid);
            frameLayout.addView(viewGrid);

            addView(frameLayout);
            getViewTreeObserver().addOnGlobalLayoutListener(this);
            setBackgroundColor(BLACK);
        }

        // methods

        private String getRowString(int i)
        {
            Stack<Integer> stack = new Stack<>();
            StringBuilder stringBuilder = new StringBuilder();

            while (i > 0)
            {
                stack.push(i % 26);

                i /= 26;
            }

            while (stack.size() > 0)
            {
                stringBuilder.append((char)(stack.pop() + 64));
            }

            return stringBuilder.toString();
        }

        @Override
        public void invalidate()
        {
            super.invalidate();

            viewGrid.invalidate();
        }

        @Override
        public void onGlobalLayout()
        {
            for (int i = 1; i < getChildCount() - 1; i++)
            {
                ((TextView)getChildAt(i)).setHeight((int)floatLength);
                ((TextView)getChildAt(i)).setWidth((int)floatLength);
            }

            getChildAt(getChildCount() - 1).requestLayout();
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b)
        {
            super.onLayout(changed, l, t, r, b);

            floatLength = (float)min(getHeight() - getPaddingBottom(), getWidth() - getPaddingRight()) / (max(intColumnsCount, intRowsCount) + 1);
            floatMargin = floatLength / 25;

            View view;

            for (int i = 0; i < getChildCount(); i++)
            {
                view = getChildAt(i);

                if (i == 0)
                {
                    view.layout((int)floatMargin, (int)floatMargin, (int)(floatLength - floatMargin), (int)(floatLength - floatMargin));
                }
                else if (i < intColumnsCount + 1)
                {
                    view.layout((int)(i % (intColumnsCount + 1) * floatLength), (int)floatMargin, (int)((i % (intColumnsCount + 1) + 1) * floatLength - floatMargin), (int)(floatLength - floatMargin));
                }
                else if (i < intColumnsCount + intRowsCount + 1)
                {
                    view.layout((int)floatMargin, (int)((i - intColumnsCount) * floatLength), (int)(floatLength - floatMargin), (int)((i - intColumnsCount + 1) * floatLength - floatMargin));
                }
                else
                {
                    view.layout((int)(floatLength), (int)(floatLength), (int)((intColumnsCount + 1) * floatLength), (int)((intRowsCount + 1) * floatLength));
                }
            }
        }

        public void setEnabled()
        {
            gridLayoutGrid.setEnabled();
        }

        // classes

        private class GridLayoutGrid extends GridLayout
        {
            // constructors

            public GridLayoutGrid(Context context)
            {
                super(context);

                for (Cell c : listCells.get(intCellsIndex))
                {
                    addView(c);
                }
            }

            // methods

            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b)
            {
                super.onLayout(changed, l, t, r, b);

                View view;

                for (int i = 0; i < getChildCount(); i++)
                {
                    view = getChildAt(i);
                    view.layout((int)(i % intColumnsCount * floatLength), (int)(i / intColumnsCount * floatLength), (int)((i % intColumnsCount + 1) * floatLength - floatMargin), (int)((i / intColumnsCount + 1) * floatLength - floatMargin));
                }
            }

            public void setEnabled()
            {
                if (!booleanPlayer && booleanStatus)
                {
                    for (Cell c : listCells.get(intCellsIndex))
                    {
                        if (listHits.get(intOpponent).contains(c.getCell()) || listMisses.get(intOpponent).contains(c.getCell()))
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
                    for (Cell c : listCells.get(intCellsIndex))
                    {
                        c.setEnabled(false);
                    }
                }
            }
        }

        private class ViewGrid extends View
        {
            // fields

            private int intPlayersIndex;

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

                int i;

                intPlayersIndex = booleanPlayer ? intPlayer : intOpponent;

                Paint paint = new Paint(ANTI_ALIAS_FLAG);
                paint.setStrokeWidth(floatLength / 25);

                Path path;

                if (booleanPlayer || !booleanStatus)
                {
                    for (Ship s : listShips.get(intPlayersIndex))
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

                for (Cell c : listCells.get(intCellsIndex))
                {
                    i = c.getCell();

                    if (listHits.get(intPlayersIndex).contains(i))
                    {
                        paint.setColor(RED);
                    }
                    else if (listMisses.get(intPlayersIndex).contains(i))
                    {
                        paint.setColor(WHITE);
                    }
                    else
                    {
                        paint.setColor(BLACK);
                    }

                    paint.setStyle(Style.FILL);
                    canvas.drawCircle(i % intColumnsCount * floatLength + (floatLength - floatMargin) / 2, i / intColumnsCount * floatLength + (floatLength - floatMargin) / 2, floatLength / 10, paint);
               }
            }
        }
    }
}