/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.model;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.*;

public class Battleship implements Serializable
{
    // fields

    private static Battleship battleship;
    private final int intColumns;
    private int intGame;
    private final int intPlayers;
    private final int intRows;
    private final List<Game> listGames;
    private final List<Pair<Integer, Integer>> listShips;
    private final Random random;

    // constructors

    private Battleship()
    {
        intColumns = intRows = 10;
        intPlayers = 2;
        listGames = new ArrayList<>();
        listShips = new ArrayList<>(Arrays.asList(new Pair<>(0, 5), new Pair<>(1, 4), new Pair<>(2, 3), new Pair<>(3, 3), new Pair<>(4, 2)));
        random = new Random();
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

    public boolean getStatus()
    {
        return listGames.get(intGame).getStatus();
    }

    public boolean shoot(int player, int cell)
    {
        for (Ship s : listGames.get(intGame).getShips()[player])
        {
            if (s.containsCell(cell))
            {
                listGames.get(intGame).getHits()[player].add(cell);
                s.removeCell(cell);

                for (Ship t : listGames.get(intGame).getShips()[player])
                {
                    if (t.getStatus())
                    {
                        return true;
                    }
                }

                listGames.get(intGame).setStatus(false);

                return true;
            }
        }

        listGames.get(intGame).getMisses()[player].add(cell);

        return false;
    }

    public int getGame()
    {
        return intGame;
    }

    public int getPlayer()
    {
        return listGames.get(intGame).getPlayer();
    }

    public List<List<List<Integer>>> getShips()
    {
        List<List<List<Integer>>> ships = new ArrayList<>();
        Ship[][] s = listGames.get(intGame).getShips();

        for (int i = 0; i < intPlayers; i++)
        {
            ships.add(new ArrayList<List<Integer>>());

            for (int j = 0; j < listShips.size(); j++)
            {
                ships.get(i).add(s[i][j].getShip());
            }
        }

        return ships;
    }

    public List<Set<Integer>> getHits()
    {
        return new ArrayList<>(Arrays.asList(listGames.get(intGame).getHits()));
    }

    public List<Set<Integer>> getMisses()
    {
        return new ArrayList<>(Arrays.asList(listGames.get(intGame).getMisses()));
    }

    public void setGame(int game)
    {
        intGame = game;
    }

    public void startGame()
    {
        int cell, heading, length, minimum, player, ship, stern;
        List<Pair<Integer, Integer>> pairs;
        Set<Integer> cells;
        Set<Integer>[] hits = (Set<Integer>[])new HashSet<?>[intPlayers];
        Set<Integer>[] misses = (Set<Integer>[])new HashSet<?>[intPlayers];
        Ship[][] ships = new Ship[intPlayers][listShips.size()];

        player = random.nextInt(intPlayers);

        for (int i = 0; i < intPlayers; i++)
        {
            hits[player] = new HashSet<>();
            misses[player] = new HashSet<>();
            pairs = new ArrayList<>(listShips);

            while (pairs.size() > 0)
            {
                cells = new HashSet<>();
                ship = random.nextInt(pairs.size());
                length = pairs.get(ship).second;

                ship: while (cells.isEmpty())
                {
                    heading = random.nextInt(4) * 90;
                    minimum = stern = ((heading == 0 ? length - 1 : 0) + random.nextInt(intRows - (heading == 0 || heading == 180 ? length - 1 : 0))) * intColumns + (heading == 270 ? length - 1 : 0) + random.nextInt(intColumns - (heading == 90 || heading == 270 ? length - 1 : 0));

                    for (int j = 0; j < length; j++)
                    {
                        cell = stern + ((int)sin(heading * PI / 180) - (int)cos(heading * PI / 180) * intColumns) * j;

                        for (Ship s : ships[player])
                        {
                            if (s != null && s.containsCell(cell))
                            {
                                cells.clear();

                                continue ship;
                            }
                        }

                        cells.add(cell);
                        minimum = min(cell, stern);
                    }

                    ships[player][pairs.get(ship).first] = new Ship(length, heading, minimum % intColumns, minimum / intColumns, cells);
                    pairs.remove(ship);
                }
            }

            player = (player + 1) % intPlayers;
        }

        intGame = listGames.size();
        listGames.add(new Game(player, hits, misses, ships));
    }

    public void togglePlayer()
    {
        listGames.get(intGame).setPlayer((listGames.get(intGame).getPlayer() + 1) % intPlayers);
    }
}