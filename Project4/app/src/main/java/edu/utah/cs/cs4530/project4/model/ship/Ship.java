/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model.ship;

import java.io.Serializable;
import java.util.Set;

public abstract class Ship implements Serializable
{
    // fields

    private final int intHeading, intLeft, intLength, intTop;
    private final Set<Integer> setCells;

    // constructors

    public Ship(int length, int heading, int left, int top, Set<Integer> cells)
    {
        intHeading = heading;
        intLeft = left;
        intLength = length;
        intTop = top;
        setCells = cells;
    }

    // methods

    public boolean getStatus()
    {
        return setCells.size() > 0;
    }

    public boolean containsCell(int cell)
    {
        return setCells.contains(cell);
    }

    public int getHeading()
    {
        return intHeading;
    }

    public int getLength()
    {
        return intLength;
    }

    public int getLeft()
    {
        return intLeft;
    }

    public int getTop()
    {
        return intTop;
    }

    public abstract Ship getShip(int heading, int left, int top, Set<Integer> cells);

    public void removeCell(int cell)
    {
        setCells.remove(cell);
    }
}