/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Game implements Serializable
{
    // fields

    private boolean booleanStatus;
    private int intPlayer;
    private final List<List<Ship>> listShips;
    private final List<Set<Integer>> listHits;
    private final List<Set<Integer>> listMisses;

    // constructors

    public Game(int player, List<List<Ship>> ships, List<Set<Integer>> hits, List<Set<Integer>> misses)
    {
        booleanStatus = true;
        intPlayer = player;
        listHits = hits;
        listMisses = misses;
        listShips = ships;
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

    public List<List<Ship>> getShips()
    {
        return listShips;
    }

    public List<Set<Integer>> getHits()
    {
        return listHits;
    }

    public List<Set<Integer>> getMisses()
    {
        return listMisses;
    }

    public void setStatus(boolean active)
    {
        booleanStatus = active;
    }

    public void setPlayer(int player)
    {
        intPlayer = player;
    }
}