/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.model;

import android.util.Log;
import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public boolean startGame()
    {
        try
        {
            int cell, column, heading, index, length, player, row, ship;
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
                        column = (heading == 270 ? length - 1 : 0) + random.nextInt(intColumns - (heading == 90 || heading == 270 ? length - 1 : 0));
                        row = (heading == 0 ? length - 1 : 0) + random.nextInt(intRows - (heading == 0 || heading == 180 ? length - 1 : 0));

                        for (int j = 0; j < length; j++)
                        {
                            cell = ((int)sin(heading * PI / 180) - (int)cos(heading * PI / 180) * intColumns) * j + intColumns * row + column;

                            for (Ship s : ships[player])
                            {
                                if (s != null && s.containsCell(cell))
                                {
                                    cells.clear();

                                    continue ship;
                                }
                            }

                            cells.add(cell);
                        }

                        ships[player][pairs.get(ship).first] = new Ship(length, heading, cells);
                        pairs.remove(ship);
                    }
                }

                player = (player + 1) % intPlayers;
            }

            intGame = listGames.size();
            listGames.add(new Game(player, hits, misses, ships));

            return true;
        }
        catch (Exception e)
        {
            Log.e("Battleship.startGame", "Error: Unable to start the game. " + e.getMessage());

            return false;
        }
    }

    public Boolean getStatus()
    {
        try
        {
            return listGames.get(intGame).getStatus();
        }
        catch (Exception e)
        {
            Log.e("Battleship.shoot", "Error: Unable to get the status. " + e.getMessage());

            return null;
        }
    }

    public Boolean shoot(int player, int cell)
    {
        try
        {
            if (listGames.get(intGame).getStatus())
            {
                for (Ship s : listGames.get(intGame).getShips()[player])
                {
                    if (s.containsCell(cell))
                    {
                        listGames.get(intGame).getHits()[player].add(cell);

                        for (Ship t : listGames.get(intGame).getShips()[player])
                        {
                            if (t.getCellCount() > 0)
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
        }
        catch (Exception e)
        {
            Log.e("Battleship.shoot", "Error: Unable to shoot. " + e.getMessage());
        }

        return null;
    }

    public Boolean togglePlayer()
    {
        try
        {
            if (listGames.get(intGame).getStatus())
            {
                listGames.get(intGame).setPlayer((listGames.get(intGame).getPlayer() + 1) % intPlayers);

                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            Log.e("Battleship.togglePlayer", "Error: Unable to toggle the player. " + e.getMessage());

            return null;
        }
    }

    public int getGame()
    {
        return intGame;
    }

    public int[][][] getShips()
    {
        try
        {
            int cell;
            int[][][] ships = new int[intPlayers][listShips.size()][];

            for (int i = 0; i < intPlayers; i++)
            {
                for (int j = 0; j < listShips.size(); j++)
                {
                    cell = Collections.min(listGames.get(intGame).getShips()[i][j].getCells());

                    ships[i][j] = new int[] {listGames.get(intGame).getShips()[i][j].getHeading(), listGames.get(intGame).getShips()[i][j].getLength(), cell / intColumns, cell % intColumns};
                }
            }

            return ships;
        }
        catch (Exception e)
        {
            Log.e("Battleship.getShips", "Error: Unable to get the ships. " + e.getMessage());

            return null;
        }
    }

    public Integer getPlayer()
    {
        try
        {
            return listGames.get(intGame).getPlayer();
        }
        catch (Exception e)
        {
            Log.e("Battleship.getPlayer", "Error: Unable to get the player. " + e.getMessage());

            return null;
        }
    }

    public Set<Integer>[] getHits()
    {
        try
        {
            Set<Integer>[] hits = (Set<Integer>[])new HashSet<?>[intPlayers];

            for (int i = 0; i < intPlayers; i++)
            {
                hits[i] = new HashSet<>(listGames.get(intGame).getHits()[i]);
            }

            return hits;
        }
        catch (Exception e)
        {
            Log.e("Battleship.getHits", "Error: Unable to get the shots. " + e.getMessage());

            return null;
        }
    }

    public Set<Integer>[] getMisses()
    {
        try
        {
            Set<Integer>[] misses = (Set<Integer>[])new HashSet<?>[intPlayers];

            for (int i = 0; i < intPlayers; i++)
            {
                misses[i] = new HashSet<>(listGames.get(intGame).getHits()[i]);
            }

            return misses;
        }
        catch (Exception e)
        {
            Log.e("Battleship.getMisses", "Error: Unable to get the shots. " + e.getMessage());

            return null;
        }
    }

    public void setGame(int game)
    {
        intGame = game;
    }
}