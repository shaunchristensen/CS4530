/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.18
 * Assignment: Project F - Lights Out
 */

package edu.utah.cs.cs4530.lightsout.model;

import android.util.Log;

import java.util.Collections;
import java.util.List;

public class LightsOut
{
    // fields

    private static final int intColumnsCount = 5;
    private static final int intModesCount = 3;
    private static final int intMovesBuffer = 10;
    private static int intPuzzle;
    private static final int intRowsCount = 5;
    private static List<Puzzle> listPuzzles;

    // methods

    public static int getCellsCount()
    {
        return intColumnsCount * intRowsCount;
    }

    public static int getColumnsCount()
    {
        return intColumnsCount;
    }

    public static int getModesCount()
    {
        return intModesCount;
    }

    public static int getMovesBuffer()
    {
        return intMovesBuffer;
    }

    public static int getPuzzle()
    {
        return intPuzzle;
    }

    public static int getPuzzlesCount()
    {
        try
        {
            return listPuzzles.size();
        }
        catch(Exception e)
        {
            Log.e("getPuzzles", "Error: Unable to get the puzzles count. " + e.getMessage());

            return 0;
        }
    }

    public static int getRowsCount()
    {
        return intRowsCount;
    }

    public static Puzzle getPuzzle(int puzzle)
    {
        try
        {
            return listPuzzles.get(puzzle);
        }
        catch (Exception e)
        {
            Log.e("getPuzzle", "Error: Unable to get the puzzle. " + e.getMessage());

            return null;
        }
    }

    public static void setPuzzle(int puzzle)
    {
        intPuzzle = puzzle;
    }

    public static void setPuzzles(List<Puzzle> puzzles)
    {
        if (listPuzzles ==  null)
        {
            listPuzzles = Collections.unmodifiableList(puzzles);
        }
    }
}