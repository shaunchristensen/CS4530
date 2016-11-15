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

    public PlayerIDCell(String playerId, int xPos, int yPos)
    {
        this.playerId = playerId;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    // methods

    public int getColumn()
    {
        return xPos;
    }

    public int getRow()
    {
        return yPos;
    }
}
