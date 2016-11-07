/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model.ship;

import java.util.HashSet;
import java.util.Set;

public class Cruiser extends Ship
{
    // constructors

    public Cruiser()
    {
        this(0, 0, 0, new HashSet<Integer>());
    }

    public Cruiser(int heading, int left, int top, Set<Integer> cells)
    {
        super(3, heading, left, top, cells);
    }

    // methods

    public Ship getShip(int heading, int left, int top, Set<Integer> cells)
    {
        return new Cruiser(heading, left, top, cells);
    }
}