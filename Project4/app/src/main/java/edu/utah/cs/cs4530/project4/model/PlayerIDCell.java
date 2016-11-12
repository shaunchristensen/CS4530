/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.11
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

public class PlayerIDCell
{
    // fields

    private int xPos, yPos;
    private String playerId;

    // constructors

    public PlayerIDCell()
    {
    }

    // methods

    public int getXPos()
    {
        return xPos;
    }

    public int getYPos()
    {
        return yPos;
    }

    public String getPlayerID()
    {
        return playerId;
    }
}
