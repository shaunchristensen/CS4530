/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.model;

import java.io.Serializable;
import java.util.Set;

public class Game implements Serializable
{
    // fields

    private boolean booleanStatus;
    private int intPlayer;
    private final Set<Integer>[] arrayHits;
    private final Set<Integer>[] arrayMisses;
    private final Ship[][] arrayShips;

    // constructors

    public Game(int player, Set<Integer>[] hits, Set<Integer>[] misses, Ship[][] ships)
    {
        arrayHits = hits;
        arrayMisses = misses;
        arrayShips = ships;
        booleanStatus = true;
        intPlayer = player;
    }

    // methods

    public boolean getStatus()
    {
        return booleanStatus;
    }

    public int getPlayer()
    {
        return intPlayer;
    }

    public Set<Integer>[] getHits()
    {
        return arrayHits;
    }

    public Set<Integer>[] getMisses()
    {
        return arrayMisses;
    }

    public Ship[][] getShips()
    {
        return arrayShips;
    }

    public void setPlayer(int player)
    {
        intPlayer = player;
    }

    public void setStatus(boolean status)
    {
        booleanStatus = status;
    }
}