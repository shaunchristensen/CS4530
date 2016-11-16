/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

public class Summary
{
    // fields

    private int missilesLaunched;
    private String id, name, player1, player2, status, winner;

    // constructors

    public Summary()
    {
    }

    // methods

    public int getShots()
    {
        return missilesLaunched;
    }

    public int getStatus()
    {
        return Battleship.getBattleship().getGameSet(status);
    }

    public String getGameID()
    {
        return id;
    }

    public String getGameName()
    {
        return name;
    }

    public String getPlayer1ID()
    {
        return player1;
    }

    public String getPlayer2ID()
    {
        return player2;
    }

    public String getStatus(int status)
    {
        return Battleship.getBattleship().getGameSet(status);
    }

    public String getWinner()
    {
        return getStatus() == Battleship.getBattleship().GAME_OVER ? winner : null;
    }
}