/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.model;

import android.util.Log;

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
    private final List<Integer> listShips;
    private final Random random;

    // constructors

    private Battleship()
    {
        intColumns = intRows = 10;
        intPlayers = 2;
        listGames = new ArrayList<>();
        listShips = new ArrayList<Integer>(Arrays.asList(5, 4, 3, 3, 2));
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
            int angle, head, length, player, ship, tail;
            List<Integer> lengths;
            List<List<Ship>> ships = Arrays.asList((List<Ship>[])Arrays.copyOf(new ArrayList<Ship>().toArray(), intPlayers));
            List<Set<Integer>> hits = Arrays.asList((Set<Integer>[])Arrays.copyOf(new HashSet<Integer>().toArray(), intPlayers));
            List<Set<Integer>> misses = Arrays.asList((Set<Integer>[])Arrays.copyOf(new HashSet<Integer>().toArray(), intPlayers));
            Set<Integer> cells;

            player = random.nextInt();

            for (int i = 0; i < intPlayers; i++)
            {
                hits.set(i, new HashSet<Integer>());
                misses.set(i, new HashSet<Integer>());
                ships.set(i, new ArrayList<Ship>(listShips.size()));

                lengths = new ArrayList<>(listShips);
                player = (player + i) % intPlayers;

                while (lengths.size() > 0)
                {
                    cells = new HashSet<>();
                    ship = random.nextInt() % lengths.size();
                    length = lengths.get(ship);

                    ship: while (cells.isEmpty())
                    {
                        tail = random.nextInt() % (intRows * intColumns);
                        angle = random.nextInt() % 4 * 90;
                        head = tail + ((int)sin(angle / PI * 2) - (int)cos(angle / PI * 2) * intColumns) * length;

                        if (head >= 0 && head < intRows * intColumns)
                        {
                            for (int j = min(head, tail); j <= max(head, tail); j++)
                            {
                                for (Ship s : ships.get(player))
                                {
                                    if (s.containsCell(j))
                                    {
                                        cells.clear();

                                        continue ship;
                                    }
                                }

                                cells.add(j);
                            }

                            lengths.remove(ship);
                            ships.get(player).set(ship, new Ship(length, angle, cells));
                        }
                    }
                }
            }

            listGames.add(new Game((player + 1) % intPlayers, ships, hits, misses));

            intGame = listGames.size();

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
                for (Ship ship1 : listGames.get(intGame).getShips().get(player))
                {
                    if (ship1.containsCell(cell))
                    {
                        listGames.get(intGame).getHits().get(player).add(cell);

                        for (Ship ship2 : listGames.get(intGame).getShips().get(player))
                        {
                            if (ship2.getCellCount() > 0)
                            {
                                return true;
                            }
                        }

                        listGames.get(intGame).setStatus(false);

                        return true;
                    }
                }

                listGames.get(intGame).getMisses().get(player).add(cell);

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

    public List<List<List<Integer>>> getShips()
    {
        try
        {
            int cell;
            List<List<List<Integer>>> ships = new ArrayList<>();

            for (int i = 0; i < intPlayers; i++)
            {
                ships.add(new ArrayList<List<Integer>>());

                for (Ship ship : listGames.get(intGame).getShips().get(i))
                {
                    cell = Collections.min(ship.getCells());

                    ships.get(i).add(Arrays.asList(ship.getHeading(), ship.getLength(), cell / intColumns, cell % intColumns));
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

    public List<Set<Integer>> getHits()
    {
        try
        {
            return new ArrayList<>(listGames.get(intGame).getHits());
        }
        catch (Exception e)
        {
            Log.e("Battleship.getHits", "Error: Unable to get the shots. " + e.getMessage());

            return null;
        }
    }

    public List<Set<Integer>> getMisses()
    {
        try
        {
            return new ArrayList<>(listGames.get(intGame).getMisses());
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