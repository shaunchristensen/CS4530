/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

public class Grids
{
    // fields

    private Cell[] opponentBoard, playerBoard;

    // constructors

    public Grids()
    {
    }

    // methods

    public Cell[] getOpponentBoard()
    {
        return opponentBoard;
    }

    public Cell[] getPlayerBoard()
    {
        return playerBoard;
    }

    // classes

    public class Cell
    {
        // fields

        private int xPos, yPos;
        private String status;

        // constructors

        public Cell()
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

        public String getStatus()
        {
            return status;
        }
    }
}