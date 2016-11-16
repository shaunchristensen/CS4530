/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Battleship
{
    // fields

    private static Battleship battleship;
    private final int intColumnsCount, intRowsCount;
    public final int ALL, HIT, IN_PROGRESS, MISS, MY_GAMES, GAME_OVER, SHIP, WAITING;
    private final List<Integer> listPlayers;
    private final List<String> listGameSets;
    private final Map<String, Integer> mapCellSets, mapGameSets;
    private final String stringAll, stringHit, stringInProgress, stringMiss, stringMyGames, stringGameOver, stringShip, stringWaiting;

    // constructors

    private Battleship()
    {
        intColumnsCount = intRowsCount = 10;

        stringAll = "All";
        stringHit = "Hit";
        stringInProgress = "In Progress";
        stringMiss = "Miss";
        stringMyGames = "My Games";
        stringGameOver = "Game Over";
        stringShip = "Ship";
        stringWaiting = "Waiting";

        listGameSets  = Collections.unmodifiableList(Arrays.asList(stringAll, stringMyGames, stringWaiting, stringInProgress, stringGameOver));
        listPlayers = Collections.unmodifiableList(Arrays.asList(0, 1));

        ALL = listGameSets.indexOf(stringAll);
        HIT = 0;
        IN_PROGRESS = listGameSets.indexOf(stringInProgress);
        MISS = 1;
        MY_GAMES = listGameSets.indexOf(stringMyGames);
        GAME_OVER = listGameSets.indexOf(stringGameOver);
        SHIP = 2;
        WAITING = listGameSets.indexOf(stringWaiting);

        mapCellSets = new HashMap<>();
        mapCellSets.put("HIT", HIT);
        mapCellSets.put("MISS", MISS);
        mapCellSets.put("SHIP", SHIP);

        mapGameSets = new HashMap<>();
        mapGameSets.put("WAITING", WAITING);
        mapGameSets.put("PLAYING", IN_PROGRESS);
        mapGameSets.put("DONE", GAME_OVER);
    }

    // methods

    public static Battleship getBattleship()
    {
        if (battleship == null)
        {
            battleship = new Battleship();
        }

        return battleship;
    }

    public int getColumnsCount()
    {
        return intColumnsCount;
    }

    public int getCellSet(String cellSet)
    {
        return mapCellSets.get(cellSet);
    }

    public int getGameSet(String gameSet)
    {
        return mapGameSets.get(gameSet);
    }

    public int getRowsCount()
    {
        return intRowsCount;
    }

    public List<Integer> getPlayers()
    {
        return listPlayers;
    }

    public List<String> getGameSets()
    {
        return new ArrayList<>(listGameSets);
    }

    public String getGameSet(boolean gameSet)
    {
        return gameSet ? "PLAYING" : "DONE";
    }

    public String getGameSet(int gameSet)
    {
        return listGameSets.get(gameSet);
    }
}