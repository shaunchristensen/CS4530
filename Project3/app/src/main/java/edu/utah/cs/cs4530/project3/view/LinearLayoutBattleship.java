/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.19
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.*;
import static android.widget.LinearLayout.VERTICAL;
import static edu.utah.cs.cs4530.project3.view.Cell.*;

public class LinearLayoutBattleship extends LinearLayout implements OnCellClickListener
{
    // fields

    private boolean booleanStatus;
    private final Grid gridOpponent;
    private final Grid gridPlayer;
    private final int intColumns;
    private int intOpponent;
    private int intPlayer;
    private final int intRows;
    private final List<List<Cell>> listCells;
    private List<List<Ship>> listShips;
    private List<Set<Integer>> listHits;
    private List<Set<Integer>> listMisses;
    private final OnShootListener onShootListener;

    // constructors

    public LinearLayoutBattleship(Context context, int rows, int columns, List<Integer> players, OnShootListener listener)
    {
        super(context);

        intColumns = columns;
        intRows = rows;
        listHits = new ArrayList<>();
        listMisses = new ArrayList<>();
        listShips = new ArrayList<>();

        List<List<Cell>> cells = new ArrayList<>();

        for (int i : players)
        {
            cells.add(new ArrayList<Cell>());

            for (int j = 0; j < intRows * intColumns; j++)
            {
                cells.get(i).add(new Cell(context, false, j, this));
            }

            listHits.add(new HashSet<Integer>());
            listMisses.add(new HashSet<Integer>());
            listShips.add(new ArrayList<Ship>());
        }

        listCells = Collections.unmodifiableList(cells);
        gridOpponent = new Grid(context, false);
        gridPlayer = new Grid(context, true);
        onShootListener = listener;

        LayoutParams layoutParams;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            layoutParams = new LayoutParams(0, MATCH_PARENT, 1);
        }
        else
        {
            layoutParams = new LayoutParams(MATCH_PARENT, 0, 1);
        }

        // handle rotation
        addView(gridOpponent, layoutParams);
        addView(gridPlayer, layoutParams);
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

    public void loadGame(boolean status, int opponent, int player, List<List<Ship>> ships, List<Set<Integer>> hits, List<Set<Integer>> misses)
    {
        listHits = hits;
        listMisses = misses;
        listShips = ships;

        setPlayers(opponent, player);
        setStatus(status);
    }

    @Override
    public void onCellClick(int cell)
    {
        onShootListener.onShoot(cell);
    }

    public void setPlayers(int opponent, int player)
    {
        intOpponent = opponent;
        intPlayer = player;
    }

    public void setStatus(boolean status)
    {
        booleanStatus = status;

        gridOpponent.togglePlayers();
        gridPlayer.togglePlayers();
    }































    // classes

    private class Grid extends GridLayout
    {
        // fields

        private final boolean booleanPlayer;
        private float floatLength;
        private float floatMargin;
        private final GridLayoutGrid gridLayoutGrid;
        private final ViewGrid viewGrid;
        private TextView textView;

        // constructors

        public Grid(Context context, boolean player)
        {
            super(context);

            booleanPlayer = player;
            gridLayoutGrid = new GridLayoutGrid(context);

            textView = new TextView(context);
            textView.setBackgroundColor(WHITE);

            viewGrid = new ViewGrid(context);

            addView(textView);

            for (int i = 0; i < intRows + intColumns + 1; i++)
            {
                TextView textView = new TextView(context);
                textView.setBackgroundColor(WHITE);

                if (i > 0 && i < intColumns + 1)
                {
                    textView.setText(Integer.toString(i));
                }
                else
                {
                    textView.setText(getString(i - intColumns));
                }

                addView(textView);
            }

            FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.addView(gridLayoutGrid);
            frameLayout.addView(viewGrid);

            addView(frameLayout);
            setBackgroundColor(BLACK);
        }

        // methods

        private String getString(int i)
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
        protected void onLayout(boolean changed, int l, int t, int r, int b)
        {
            super.onLayout(changed, l, t, r, b);

            float height, left, top, width;

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                floatLength = height = getHeight();
                left = width = getWidth() - getHeight();
                top = 0;
            }
            else
            {
                floatLength = width = getWidth();
                height = top = getHeight() - getWidth();
                left = 0;
            }

            floatLength /= ((intColumns > intRows ? intColumns : intRows) + 1);
            floatMargin = floatLength / 25;

            View view;

            for (int i = 0; i < getChildCount(); i++)
            {
                view = getChildAt(i);

                if (i == 0)
                {
                    view.layout(0, 0, (int)width, (int)height);
                }
                else if (i < intColumns + 2)
                {
                    view.layout((int)((i - 1) % (intColumns + 1) * floatLength + left), (int)top, (int)(((i - 1) % (intColumns + 1) + 1) * floatLength - floatMargin + left), (int)(floatLength - floatMargin + top));
                }
                else if (i < intColumns + intRows + 2)
                {
                    view.layout((int)left, (int)(((i - 1) - intColumns) * floatLength + top), (int)(floatLength - floatMargin + left), (int)(((i - 1) - intColumns + 1) * floatLength - floatMargin + top));
                }
                else
                {
                    view.layout((int)(floatLength + left), (int)(floatLength + top), (int)((intColumns + 1) * floatLength + left), (int)((intRows + 1) * floatLength + top));
                }
            }
        }

        public void togglePlayers()
        {
            gridLayoutGrid.setEnabled();
            viewGrid.invalidate();
        }

        // classes

        private class GridLayoutGrid extends GridLayout
        {
            // constructors

            public GridLayoutGrid(Context context)
            {
                super(context);

                for (Cell c : listCells.get(booleanPlayer ? 1 : 0))
                {
                    addView(c);
                }
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

            public void setEnabled()
            {
                if (!booleanPlayer && booleanStatus)
                {
                    for (Cell c : listCells.get(intOpponent))
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
                    for (Cell c : listCells.get(intOpponent))
                    {
                        c.setEnabled(false);
                    }
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

                int i, player;

                Paint paint = new Paint(ANTI_ALIAS_FLAG);
                paint.setStrokeWidth(floatLength / 25);

                Path path;

                if (booleanPlayer)
                {
                    player = intPlayer;

                    for (Ship s : listShips.get(player))
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
                else
                {
                    player = intOpponent;
                }

                for (Cell c : listCells.get(player))
                {
                    i = c.getCell();

                    if (listHits.get(player).contains(i))
                    {
                        paint.setColor(RED);
                    }
                    else if (listMisses.get(player).contains(i))
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

//                    canvas.drawCircle(i % intColumns * floatLength + (floatLength - floatMargin) / 2, i / intColumns * floatLength + (floatLength - floatMargin) / 2, floatLength / 10, paint);
                }
            }
        }
    }
}