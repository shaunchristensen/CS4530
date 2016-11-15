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
    public final int ALL, IN_PROGRESS, MY_GAMES, OVER, WAITING;
    private final List<Integer> listPlayers;
    private final List<String> listGameSets;
    private final Map<String, Integer> mapGameSets;
    private final String stringAll, stringInProgress, stringMyGames, stringOver, stringWaiting;

    // constructors

    private Battleship()
    {
        intColumnsCount = intRowsCount = 10;

        stringAll = "All";
        stringInProgress = "In Progress";
        stringMyGames = "My Games";
        stringOver = "Over";
        stringWaiting = "Waiting";

        listGameSets  = Collections.unmodifiableList(Arrays.asList(stringAll, stringMyGames, stringWaiting, stringInProgress, stringOver));
        listPlayers = Collections.unmodifiableList(Arrays.asList(0, 1));

        ALL = listGameSets.indexOf(stringAll);
        IN_PROGRESS = listGameSets.indexOf(stringInProgress);
        MY_GAMES = listGameSets.indexOf(stringMyGames);
        OVER = listGameSets.indexOf(stringOver);
        WAITING = listGameSets.indexOf(stringWaiting);

        mapGameSets = new HashMap<>();
        mapGameSets.put("WAITING", WAITING);
        mapGameSets.put("PLAYING", IN_PROGRESS);
        mapGameSets.put("DONE", OVER);
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