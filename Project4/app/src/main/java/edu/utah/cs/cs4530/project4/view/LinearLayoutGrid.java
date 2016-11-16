/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.rgb;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewTreeObserver.*;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class LinearLayoutGrid extends LinearLayout implements OnGlobalLayoutListener
{
    // fields

    private boolean booleanStatus, booleanTurn;
    private Grid gridOpponent, gridPlayer;
    private final int intColumnsCount, intPadding, intRowsCount;
    private final LinearLayout linearLayoutOpponent, linearLayoutOpponentPlayer, linearLayoutPlayer, linearLayoutStatusTurn;
    private List<Set<Integer>> listHits, listMisses, listShips;
    private final OnShootListener onShootListener;
    private String stringOpponent, stringPlayer;
    private final TextView textViewOpponent, textViewPlayer, textViewStatus, textViewTurn;

    // constructors

    public LinearLayoutGrid(Context context, int rowsCount, int columnsCount, int padding, List<Integer> players, OnShootListener listener)
    {
        super(context);

        gridOpponent = new Grid(context, false, 0);
        gridPlayer = new Grid(context, true, 1);
        intColumnsCount = columnsCount;
        intPadding = padding;
        intRowsCount = rowsCount;
        listHits = new ArrayList<>();
        listMisses = new ArrayList<>();
        listShips = new ArrayList<>();

        for (int i : players)
        {
            listHits.add(new HashSet<Integer>());
            listMisses.add(new HashSet<Integer>());
            listShips.add(new HashSet<Integer>());
        }

        onShootListener = listener;

        textViewOpponent = new TextView(context);
        textViewOpponent.setTextColor(BLACK);
        textViewOpponent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        textViewPlayer = new TextView(context);
        textViewPlayer.setTextColor(BLACK);
        textViewPlayer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        textViewStatus = new TextView(context);
        textViewStatus.setTextColor(BLACK);
        textViewStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        textViewTurn = new TextView(context);
        textViewTurn.setTextColor(BLACK);
        textViewTurn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        linearLayoutOpponent = new LinearLayout(context);
        linearLayoutOpponent.addView(textViewOpponent);
        linearLayoutOpponent.addView(gridOpponent);
        linearLayoutOpponent.setOrientation(VERTICAL);

        linearLayoutPlayer = new LinearLayout(context);
        linearLayoutPlayer.addView(textViewPlayer);
        linearLayoutPlayer.addView(gridPlayer);
        linearLayoutPlayer.setOrientation(VERTICAL);

        linearLayoutOpponentPlayer = new LinearLayout(context);
        linearLayoutOpponentPlayer.addView(linearLayoutOpponent);
        linearLayoutOpponentPlayer.addView(linearLayoutPlayer);

        linearLayoutStatusTurn = new LinearLayout(context);
        linearLayoutStatusTurn.addView(textViewStatus);
        linearLayoutStatusTurn.addView(textViewTurn);

        addView(linearLayoutOpponentPlayer);
        addView(linearLayoutStatusTurn);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        setOrientation(VERTICAL);
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
            listHits.get(0).add(cell);
            listShips.get(0).remove(cell);
        }
        else
        {
            listMisses.get(0).add(cell);
        }

        gridOpponent.invalidate();
    }

    @Override
    public void onGlobalLayout()
    {
        int height, length, max, width;
        LayoutParams layoutParams;

        height = getHeight();
        width = getWidth();

        textViewOpponent.measure(0, 0);
        textViewPlayer.measure(0, 0);
        textViewStatus.measure(0, 0);
        textViewTurn.measure(0, 0);

        if (height < width)
        {
            max = max(textViewOpponent.getMeasuredHeight(), textViewPlayer.getMeasuredHeight());
            height -= max + intPadding;
            width = (width - intPadding) / 2;
            layoutParams = new LayoutParams(width, max);

            textViewOpponent.setLayoutParams(layoutParams);
            textViewOpponent.setPadding(0, 0, 0, intPadding);

            textViewPlayer.setLayoutParams(layoutParams);
            textViewPlayer.setPadding(0, 0, 0, intPadding);

            max = max(textViewStatus.getMeasuredHeight(), textViewTurn.getMeasuredHeight());
            height -= max + intPadding;
            layoutParams = new LayoutParams(width, max);

            textViewStatus.setLayoutParams(layoutParams);
            textViewTurn.setLayoutParams(layoutParams);

            linearLayoutOpponentPlayer.setOrientation(HORIZONTAL);
            linearLayoutStatusTurn.setOrientation(HORIZONTAL);

            linearLayoutPlayer.setPadding(intPadding, 0, 0, 0);
            linearLayoutStatusTurn.setPadding(0, intPadding, 0, 0);
            textViewTurn.setPadding(intPadding, 0, 0, 0);

            length = min(height, width);

            layoutParams = new LayoutParams(length, length);
            layoutParams.setMargins(0, 0, height - length, width - length);

            gridOpponent.setLayoutParams(layoutParams);
            gridOpponent.requestLayout();

            gridPlayer.setLayoutParams(layoutParams);
            gridPlayer.requestLayout();
        }
        else
        {
            textViewOpponent.setLayoutParams(new LayoutParams(MATCH_PARENT, textViewOpponent.getMeasuredHeight()));
            textViewPlayer.setLayoutParams(new LayoutParams(MATCH_PARENT, textViewPlayer.getMeasuredHeight()));
            textViewStatus.setLayoutParams(new LayoutParams(MATCH_PARENT, textViewStatus.getMeasuredHeight()));
            textViewTurn.setLayoutParams(new LayoutParams(MATCH_PARENT, textViewTurn.getMeasuredHeight()));




            height = (height - intPadding) / 2;























            layoutParams = new LayoutParams(width, height + intPadding);

            linearLayoutOpponent.setPadding(0, 0, 0, intPadding);

            linearLayoutOpponentPlayer.setOrientation(VERTICAL);
            linearLayoutStatusTurn.setOrientation(VERTICAL);
        }
/*
        linearLayoutOpponent.setLayoutParams(layoutParams);

        layoutParams = new LayoutParams(width, height);

        linearLayoutPlayer.setLayoutParams(layoutParams);

        height -= maxHeight;
        int length = min(height, width);

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
        */
    }

    private void setCells(List<Set<Integer>> hits, List<Set<Integer>> misses, List<Set<Integer>> ships)
    {
        listHits = hits;
        listMisses = misses;
        listShips = ships;

        gridOpponent.invalidate();
        gridPlayer.invalidate();
    }

    public void setGame(String opponent, String player, String winner, boolean status, boolean turn, List<Set<Integer>> hits, List<Set<Integer>> misses, List<Set<Integer>> ships)
    {
        stringOpponent = opponent;
        stringPlayer = player;

        setCells(hits, misses, ships);
        setStatus(status);
        setTurn(turn, winner);
        textViewOpponent.setText(opponent);
        textViewPlayer.setText(player);
    }

    private void setStatus(boolean status)
    {
        booleanStatus = status;

        if (booleanStatus)
        {
            textViewStatus.setText("Status: In Progress");
        }
        else
        {
            textViewStatus.setText("Status: Game Over");
        }
    }

    private void setTurn(boolean turn, String winner)
    {
        booleanTurn = turn;

        if (booleanStatus)
        {
            if (booleanTurn)
            {
                textViewTurn.setText("Turn: " + stringPlayer);
            }
            else
            {
                textViewTurn.setText("Status: " + stringOpponent);
            }
        }
        else
        {
            textViewTurn.setText("Winner: " + winner);
        }
    }

    // classes

    private class Grid extends GridLayout implements OnGlobalLayoutListener, OnTouchListener
    {
        // fields

        private final boolean booleanPlayer;
        private boolean booleanShoot;
        private float floatLength, floatMargin;
        private int intCell, intCellsIndex;
        private final ViewGrid viewGrid;

        // constructors

        public Grid(Context context, boolean player, int cellsIndex)
        {
            super(context);

            booleanPlayer = player;
            intCellsIndex = cellsIndex;
            viewGrid = new ViewGrid(context);

            if (!booleanPlayer)
            {
                viewGrid.setOnTouchListener(this);
            }

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

            addView(viewGrid);
            getViewTreeObserver().addOnGlobalLayoutListener(this);
            setBackgroundColor(BLACK);
        }

        // methods

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            if (booleanStatus && booleanTurn)
            {
                if (event.getActionMasked() == ACTION_DOWN)
                {
                    booleanShoot = true;
                    intCell = (int)(event.getX() / floatLength) + (int)(event.getY() / floatLength) * intColumnsCount;
                }
                else if (event.getActionMasked() == ACTION_MOVE && (int)(event.getX() / floatLength) + (int)(event.getY() / floatLength) * intColumnsCount != intCell)
                {
                    booleanShoot = false;
                }
                else if (event.getActionMasked() == ACTION_UP && booleanShoot && !listHits.get(intCellsIndex).contains(intCell) && !listMisses.get(intCellsIndex).contains(intCell))
                {
                    booleanTurn = false;

                    onShootListener.onShoot(intCell);
                }

                return true;
            }

            return false;
        }

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

        // classes

        private class ViewGrid extends View
        {
            // constructors

            public ViewGrid(Context context)
            {
                super(context);

                setBackgroundColor(BLACK);
            }

            // methods

            @Override
            protected void onDraw(Canvas canvas)
            {
                super.onDraw(canvas);

                Paint paint = new Paint(ANTI_ALIAS_FLAG);
                paint.setStrokeWidth(floatLength / 25);
                paint.setStyle(Style.FILL);

                for (int i = 0; i < intColumnsCount * intRowsCount; i++)
                {
                    if (listHits.get(intCellsIndex).contains(i))
                    {
                        paint.setColor(RED);
                    }
                    else if (listMisses.get(intCellsIndex).contains(i))
                    {
                        paint.setColor(WHITE);
                    }
                    else if ((booleanPlayer || !booleanStatus) && listShips.get(intCellsIndex).contains(i))
                    {
                        paint.setColor(rgb(132, 132, 130));
                    }
                    else
                    {
                        paint.setColor(rgb(64, 164, 223));
                    }

                    canvas.drawRect(i % intColumnsCount * floatLength, i / intColumnsCount * floatLength, (i % intColumnsCount + 1) * floatLength - floatMargin, (i / intColumnsCount + 1) * floatLength - floatMargin, paint);
                }
            }
        }
    }
}