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
    public final int ALL, HIT, IN_PROGRESS, MISS, NONE, MY_GAMES, GAME_OVER, SHIP, WAITING;
    private final List<Integer> listPlayers;
    private final List<String> listGameSets;
    private final Map<String, Integer> mapCellSets, mapGameSets;
    private final String stringAll, stringInProgress, stringMyGames, stringGameOver, stringWaiting;

    // constructors

    private Battleship()
    {
        intColumnsCount = intRowsCount = 10;

        stringAll = "All";
        stringInProgress = "In Progress";
        stringMyGames = "My Games";
        stringGameOver = "Game Over";
        stringWaiting = "Waiting";

        listGameSets  = Collections.unmodifiableList(Arrays.asList(stringAll, stringMyGames, stringWaiting, stringInProgress, stringGameOver));
        listPlayers = Collections.unmodifiableList(Arrays.asList(0, 1));

        NONE = 0;
        HIT = 1;
        MISS = 2;
        SHIP = 3;

        ALL = listGameSets.indexOf(stringAll);
        IN_PROGRESS = listGameSets.indexOf(stringInProgress);
        MY_GAMES = listGameSets.indexOf(stringMyGames);
        GAME_OVER = listGameSets.indexOf(stringGameOver);
        WAITING = listGameSets.indexOf(stringWaiting);

        mapCellSets = new HashMap<>();
        mapCellSets.put("NONE", NONE);
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