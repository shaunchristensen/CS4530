/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.model;

import android.util.Log;

import java.io.Serializable;
import java.util.Set;

public class Ship implements Serializable
{
    // fields

    protected final int intHeading;
    protected final int intLength;
    protected final Set<Integer> setCells;

    // constructors

    public Ship(int length, int heading, Set<Integer> cells)
    {
        intHeading = heading;
        intLength = length;
        setCells = cells;
    }

    // methods

    public Boolean containsCell(int cell)
    {
        try
        {
            return setCells.contains(cell);
        }
        catch (Exception e)
        {
            Log.e("Ship.containsCell", "Error: Unable to check the cell. " + e.getMessage());

            return null;
        }
    }

    public int getCellCount()
    {
        return setCells.size();
    }

    public int getHeading()
    {
        return intHeading;
    }

    public int getLength()
    {
        return intLength;
    }

    public Set<Integer> getCells()
    {
        return setCells;
    }

    public void removeCell(int cell)
    {
        try
        {
            setCells.remove(cell);
        }
        catch (Exception e)
        {
            Log.e("Ship.removeCell", "Error: Unable to remove the cell. " + e.getMessage());
        }
    }
}