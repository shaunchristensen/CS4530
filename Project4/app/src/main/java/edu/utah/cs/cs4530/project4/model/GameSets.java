/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.10
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GameSets
{
    // fields

    private static List<String> listGameSets = Arrays.asList("All", "My Games", "Waiting", "In Progress", "Over");

    // methods

    public static int getGameSetIndex(String gameSet)
    {
        if (listGameSets.contains(gameSet))
        {
            return listGameSets.indexOf(gameSet);
        }
        else
        {
            return 0;
        }
    }

    public static List<String> getGameSets()
    {
        return new ArrayList<>(listGameSets);
    }

    public static String getGameSet(String gameSet)
    {
        if (gameSet.equalsIgnoreCase("Waiting"))
        {
            return listGameSets.get(2);
        }
        else if (gameSet.equalsIgnoreCase("Playing"))
        {
            return listGameSets.get(3);
        }
        else if (gameSet.equalsIgnoreCase("Done"))
        {
            return listGameSets.get(4);
        }
        else
        {
            return listGameSets.get(0);
        }
    }
}