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

    public PlayerIDCell(String playerId, int cell)
    {
        int columnsCount = Battleship.getBattleship().getColumnsCount();
        this.playerId = playerId;
        xPos = cell % columnsCount;
        yPos = cell / columnsCount;
    }

    // methods

    public int getCell()
    {
        return xPos + yPos * Battleship.getBattleship().getColumnsCount();
    }
}
