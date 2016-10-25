/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.model.ship;

import java.util.HashSet;
import java.util.Set;

public class Destroyer extends Ship
{
    // constructors

    public Destroyer()
    {
        this(0, 0, 0, new HashSet<Integer>());
    }

    public Destroyer(int heading, int left, int top, Set<Integer> cells)
    {
        super(2, heading, left, top, cells);
    }

    // methods

    public Ship getShip(int heading, int left, int top, Set<Integer> cells)
    {
        return new Destroyer(heading, left, top, cells);
    }
}