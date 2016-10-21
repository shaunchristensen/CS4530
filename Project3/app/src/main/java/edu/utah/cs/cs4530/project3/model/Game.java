/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import edu.utah.cs.cs4530.project3.model.ship.Ship;

public class Game implements Serializable
{
    // fields

    private boolean booleanStatus;
    private int intPlayer;
    private final int intPlayers;
    private final List<List<Ship>> listShips;
    private final List<Set<Integer>> listHits;
    private final List<Set<Integer>> listMisses;

    // constructors

    public Game(int player, int players, List<List<Ship>> ships, List<Set<Integer>> hits, List<Set<Integer>> misses)
    {
        booleanStatus = true;
        intPlayer = player;
        intPlayers = players;
        listHits = Collections.unmodifiableList(hits);
        listMisses = Collections.unmodifiableList(misses);
        listShips = Collections.unmodifiableList(ships);
    }

    // methods

    public boolean getStatus()
    {
        return booleanStatus;
    }

    public boolean shoot(final int cell)
    {
        int opponent = getOpponent();

        for (Ship s : listShips.get(opponent))
        {
            if (s.containsCell(cell))
            {
                listHits.get(opponent).add(cell);
                s.removeCell(cell);

                for (Ship t : listShips.get(opponent))
                {
                    if (t.getStatus())
                    {
                        intPlayer = opponent;

                        return true;
                    }
                }

                booleanStatus = false;

                return true;
            }
        }

        intPlayer = opponent;

        listMisses.get(opponent).add(cell);

        return false;
    }

    public int getOpponent()
    {
        return (intPlayer + 1) % intPlayers;
    }

    public int getPlayer()
    {
        return intPlayer;
    }

    public List<Set<Integer>> getHits()
    {
        return new ArrayList<>(listHits);
    }

    public List<Set<Integer>> getMisses()
    {
        return new ArrayList<>(listMisses);
    }

    public List<Ship> getShips(int player)
    {
        return new ArrayList<>(listShips.get(player));
    }
}