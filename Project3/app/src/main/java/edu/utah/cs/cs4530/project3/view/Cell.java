/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import static android.graphics.Color.rgb;
import static android.view.View.*;

public class Cell extends Button implements OnClickListener
{
    // fields

    private final int intCell;
    private final OnCellClickListener onCellClickListener;

    // constructors

    public Cell(Context context, int cell, OnCellClickListener listener)
    {
        super(context);

        intCell = cell;
        onCellClickListener = listener;

        setBackgroundColor(rgb(64, 164, 223));
        setEnabled(false);
        setOnClickListener(this);
    }

    // interfaces

    public interface OnCellClickListener
    {
        void onCellClick(int cell);
    }

    // methods

    public int getCell()
    {
        return intCell;
    }

    @Override
    public void onClick(View view)
    {
        onCellClickListener.onCellClick(intCell);
    }
}