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

    public int getMissilesLaunched()
    {
        return missilesLaunched;
    }

    public int getStatus()
    {
        return Battleship.getBattleship().getGameSet(status);
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

    public String getWinner()
    {
        return winner;
    }

    public String toString()
    {
        return "Game Name: " + getName() + "\nPlayer 1: " + getPlayer1() + "\nPlayer 2: " + getPlayer2() + "\nShots Fired: " + getMissilesLaunched() + "\nStatus: " + Battleship.getBattleship().getGameSet(getStatus()) + (getStatus() == Battleship.getBattleship().GAME_OVER ? "\nWinner: " + getWinner() : "");
    }
}