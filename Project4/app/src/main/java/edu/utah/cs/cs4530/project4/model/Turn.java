/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

public class Turn
{
    // fields

    private boolean isYourTurn;
    private String winner;

    // constructors

    public Turn()
    {
    }

    // methods

    public boolean getTurn()
    {
        return isYourTurn;
    }

    public String getWinner()
    {
        return winner;
    }
}