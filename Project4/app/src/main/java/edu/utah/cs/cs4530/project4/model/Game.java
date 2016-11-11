/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

public class Game
{
    // fields

    private String id, name, status;

    // constructors

    public Game()
    {
    }

    // methods

    public String getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getStatus()
    {
        return GameSets.getGameSet(status);
    }

    @Override
    public String toString()
    {
        return getName();
    }
}