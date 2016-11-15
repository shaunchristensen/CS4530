/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

import java.io.Serializable;

public class Status implements Serializable
{
    // fields

    private boolean isYourTurn;
    private String winner;

    // constructors

    public Status()
    {
    }

    // methods

    public boolean getIsYourTurn()
    {
        return isYourTurn;
    }

    public String getWinner()
    {
        return winner;
    }
}