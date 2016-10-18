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

    private final int intHeading;
    private final int intLeft;
    private final int intLength;
    private final int intTop;
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

    public boolean containsCell(Integer cell)
    {
        return setCells.contains(cell);
    }

    public boolean getStatus()
    {
        return setCells.size() > 0;
    }

    public List<Integer> getShip()
    {
        return new ArrayList<>(Arrays.asList(intLength, intHeading, intLeft, intTop));
    }

    public void removeCell(Integer cell)
    {
        setCells.remove(cell);
    }
}