/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

public class Cells
{
    // fields

    private Cell[] opponentBoard, playerBoard;

    // constructors

    public Cells()
    {
    }

    // methods

    public Cell[] getCells(int index)
    {
        return index == 0 ? opponentBoard : playerBoard;
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

        public int getCell()
        {
            return xPos + yPos * Battleship.getBattleship().getColumnsCount();
        }

        public int getCellSet()
        {
            return Battleship.getBattleship().getCellSet(status);
        }
    }
}