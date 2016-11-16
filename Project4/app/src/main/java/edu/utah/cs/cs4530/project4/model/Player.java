/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable
{
    // fields

    private final int intPlayerIndex;
    private List<String> listPlayerNames;
    private final String stringPlayerID;

    // constructors

    public Player(int playerIndex, String playerID)
    {
        intPlayerIndex = playerIndex;
        stringPlayerID = playerID;
    }

    // methods

    public String getOpponentName()
    {
        return listPlayerNames.get((intPlayerIndex + 1) % Battleship.getBattleship().getPlayers().size());
    }

    public String getPlayerID()
    {
        return stringPlayerID;
    }

    public String getPlayerName()
    {
        return listPlayerNames.get(intPlayerIndex);
    }

    public void setPlayerNames(List<String> playerNames)
    {
        if (listPlayerNames == null)
        {
            listPlayerNames = playerNames;
        }
    }
}