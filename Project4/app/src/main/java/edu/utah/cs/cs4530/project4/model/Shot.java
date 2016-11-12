/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

public class Shot
{
    // fields

    private boolean hit;
    private int shipSunk;

    // constructors

    public Shot()
    {
    }

    // methods

    public boolean getHit()
    {
        return hit;
    }

    public int getShipSunk()
    {
        return shipSunk;
    }
}