/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.10
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSets
{
    // fields

    private static GameSets gameSets;
    public final int ALL, IN_PROGRESS, MY_GAMES, OVER, WAITING;
    private List<String> listGameSets;
    private Map<String, Integer> mapGameSets;
    private String stringAll, stringInProgress, stringMyGames, stringOver, stringWaiting;

    // constructors

    private GameSets()
    {
        stringAll = "All";
        stringInProgress = "In Progress";
        stringMyGames = "My Games";
        stringOver = "Over";
        stringWaiting = "Waiting";

        listGameSets = new ArrayList<>();
        listGameSets.add(stringAll);
        listGameSets.add(stringMyGames);
        listGameSets.add(stringWaiting);
        listGameSets.add(stringInProgress);
        listGameSets.add(stringOver);

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

    public static GameSets getInstance()
    {
        if (gameSets == null)
        {
            gameSets = new GameSets();
        }

        return gameSets;
    }

    public int getGameSet(String gameSet)
    {
        return mapGameSets.get(gameSet);
    }

    public List<String> getGameSets()
    {
        return new ArrayList<>(listGameSets);
    }

    public String getGameSet(int gameSet)
    {
        return listGameSets.get(gameSet);
    }
}