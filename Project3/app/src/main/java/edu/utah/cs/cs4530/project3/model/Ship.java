/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Ship implements Serializable
{
    // fields

    private final float floatLeft;
    private final float floatTop;
    private final int intHeading;
    private final int intLength;
    private final Set<Integer> setCells;

    // constructors

    public Ship(int length, int heading, float left, float top, Set<Integer> cells)
    {
        floatLeft = left;
        floatTop = top;
        intHeading = heading;
        intLength = length;
        setCells = cells;
    }

    // methods

    public boolean containsCell(Integer cell)
    {
        return setCells.contains(cell);
    }

    public boolean getStatus()
    {
        return setCells.size() > 0;
    }

    public List<Number> getShip()
    {
        return new ArrayList<Number>(Arrays.asList(intLength, intHeading, floatLeft, floatTop));
    }

    public void removeCell(Integer cell)
    {
        setCells.remove(cell);
    }
}