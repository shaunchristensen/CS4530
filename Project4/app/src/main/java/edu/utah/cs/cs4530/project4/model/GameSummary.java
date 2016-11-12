/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

public class GameSummary
{
    // fields

    private int missilesLaunched;
    private String id, name, player1, player2, status, winner;

    // constructors

    public GameSummary()
    {
    }

    // methods

    public int getMissilesLaunched()
    {
        return missilesLaunched;
    }

    public String getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getPlayer1()
    {
        return player1;
    }

    public String getPlayer2()
    {
        return player2;
    }

    public String getStatus()
    {
        return GameSets.getInstance().getGameSet(status);
    }

    public String getWinner()
    {
        return winner;
    }
}