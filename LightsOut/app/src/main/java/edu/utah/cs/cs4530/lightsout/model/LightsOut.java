/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.18
 * Assignment: Project F - Lights Out
 */

package edu.utah.cs.cs4530.lightsout.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.utah.cs.cs4530.lightsout.R;

import static java.lang.Math.min;

public class LightsOut
{
    // fields

    private static final int intBuffer = 10;
    private static final int intColumns = 5;
    private static final int intModes = 3;
    private static int intPuzzle;
    private static final int intRows = 5;
    private static List<Puzzle> listPuzzles;

    // methods

    public static int getBuffer()
    {
        return intBuffer;
    }

    public static int getColumns()
    {
        return intColumns;
    }

    public static int getModes()
    {
        return intModes;
    }

    public static int getPuzzle()
    {
        return intPuzzle;
    }

    public static int getRows()
    {
        return intRows;
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
        try
        {
            if (puzzle >- 0 && puzzle < listPuzzles.size())
            {
                intPuzzle = puzzle;
            }
        }
        catch (Exception e)
        {
            intPuzzle = 0;

            Log.e("setPuzzle", "Error: Unable to set the puzzle. " + e.getMessage());
        }
    }

    public static void setPuzzles(List<Puzzle> puzzles)
    {
        if (listPuzzles ==  null)
        {
            listPuzzles = Collections.unmodifiableList(puzzles);
        }
    }
}