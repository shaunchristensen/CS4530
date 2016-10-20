/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.14
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.utah.cs.cs4530.project3.model.ship.Carrier;
import edu.utah.cs.cs4530.project3.model.ship.Cruiser;
import edu.utah.cs.cs4530.project3.model.ship.Destroyer;
import edu.utah.cs.cs4530.project3.model.ship.Ship;
import edu.utah.cs.cs4530.project3.model.ship.Submarine;

import static java.lang.Math.*;

public class Battleship implements Serializable
{
    // fields

    private static Battleship battleship;
    private final int intColumns;
    private int intGame;
    private final int intRows;
    private final List<Game> listGames;
    private final List<Integer> listPlayers;
    private final List<Ship> listShips;
    private final Random random;

    // constructors

    private Battleship()
    {
        intColumns = intRows = 10;
        listGames = new ArrayList<>();
        listPlayers = Collections.unmodifiableList(Arrays.asList(0, 1));
        listShips = Collections.unmodifiableList(Arrays.asList(new edu.utah.cs.cs4530.project3.model.ship.Battleship(), new Carrier(), new Cruiser(), new Destroyer(), new Submarine()));
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

    public boolean shoot(int cell)
    {
        return listGames.get(intGame).shoot(cell);
    }

    public int getColumns()
    {
        return intColumns;
    }

    public int getGame()
    {
        return intGame;
    }

    public int getOpponent()
    {
        return listGames.get(intGame).getOpponent();
    }

    public int getPlayer()
    {
        return listGames.get(intGame).getPlayer();
    }

    public int getRows()
    {
        return intRows;
    }

    public List<Integer> getPlayers()
    {
        return listPlayers;
    }

    public List<Set<Integer>> getHits()
    {
        return listGames.get(intGame).getHits();
    }

    public List<Set<Integer>> getMisses()
    {
        return listGames.get(intGame).getMisses();
    }

    public List<Ship> getShips(int player)
    {
        return listGames.get(intGame).getShips(player);
    }

    private List<Ship> shuffleShips()
    {
        List<Ship> ships = new ArrayList<>(listShips);

        Collections.shuffle(ships);

        return ships;
    }

    public void setGame(int game)
    {
        intGame = game;
    }

    public void startGame()
    {
        int cell, heading, minimum, stern;
        List<List<Ship>> ships = new ArrayList<>();
        List<Set<Integer>> hits = new ArrayList<>();
        List<Set<Integer>> misses = new ArrayList<>();
        Set<Integer> cells;

        for (int i : getPlayers())
        {
            hits.add(new HashSet<Integer>());
            misses.add(new HashSet<Integer>());
            ships.add(new ArrayList<Ship>());

            for (Ship s : shuffleShips())
            {
                cells = new HashSet<>();

                cells: while (cells.isEmpty())
                {
                    heading = random.nextInt(4) * 90;
                    cell = minimum = stern = ((heading == 0 ? s.getLength() - 1 : 0) + random.nextInt(intRows - (heading == 0 || heading == 180 ? s.getLength() - 1 : 0))) * intColumns + (heading == 270 ? s.getLength() - 1 : 0) + random.nextInt(intColumns - (heading == 90 || heading == 270 ? s.getLength() - 1 : 0));

                    for (int j = 0; j < s.getLength(); j++)
                    {
                        cell = stern + ((int)sin(heading * PI / 180) - (int)cos(heading * PI / 180) * intColumns) * j;

                        for (Ship t : ships.get(i))
                        {
                            if (t.containsCell(cell))
                            {
                                cells.clear();

                                continue cells;
                            }
                        }

                        cells.add(cell);
                    }

                    minimum = min(cell, stern);

                    if (s.getClass().equals(edu.utah.cs.cs4530.project3.model.ship.Battleship.class))
                    {
                        ships.get(i).add(new edu.utah.cs.cs4530.project3.model.ship.Battleship(heading, minimum % intColumns, minimum / intColumns, cells));
                    }
                    else if (s.getClass().equals(Carrier.class))
                    {
                        ships.get(i).add(new Carrier(heading, minimum % intColumns, minimum / intColumns, cells));
                    }
                    else if (s.getClass().equals(Cruiser.class))
                    {
                        ships.get(i).add(new Cruiser(heading, minimum % intColumns, minimum / intColumns, cells));
                    }
                    else if (s.getClass().equals(Destroyer.class))
                    {
                        ships.get(i).add(new Destroyer(heading, minimum % intColumns, minimum / intColumns, cells));
                    }
                    else
                    {
                        ships.get(i).add(new Submarine(heading, minimum % intColumns, minimum / intColumns, cells));
                    }
                }
            }
        }

        intGame = listGames.size();

        listGames.add(new Game(random.nextInt(listPlayers.size()), listPlayers.size(), ships, hits, misses));
    }
}