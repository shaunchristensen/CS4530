/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.11
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

public class GameIDPlayerName
{
    // fields

    private String playerName, id;

    // constructors

    public GameIDPlayerName(String gameId, String playerName)
    {
        this.id = gameId;
        this.playerName = playerName;
    }

    // methods

    public String getGameID()
    {
        return id;
    }
}
