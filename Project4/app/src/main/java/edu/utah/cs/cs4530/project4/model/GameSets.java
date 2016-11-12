/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.10
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameSets
{
    // fields

    private static GameSets gameSets;
    private final List<String> listGameSets;
    private final Map<String, String> mapGameSets;

    // constructors

    private GameSets()
    {
        listGameSets = new ArrayList<>();
        listGameSets.add("All");
        listGameSets.add("My Games");

        mapGameSets = new LinkedHashMap<>();
        mapGameSets.put("WAITING", "Waiting");
        mapGameSets.put("PLAYING", "In Progress");
        mapGameSets.put("DONE", "Over");

        listGameSets.addAll(mapGameSets.values());
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

    public List<String> getGameSets()
    {
        return new ArrayList<>(listGameSets);
    }

    public String getGameSet(int gameSet)
    {
        return listGameSets.get(gameSet);
    }

    public String getGameSet(String gameSet)
    {
        return mapGameSets.get(gameSet);
    }
}