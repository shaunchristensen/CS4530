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
    private final int intColumnsCount, intRowsCount;
    private final List<Game> listGames;
    private final List<Integer> listPlayers;
    private final List<Ship> listShips;
    private final Random random;

    // constructors

    private Battleship()
    {
        intColumnsCount = intRowsCount = 10;
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

    public boolean getStatus(int game)
    {
        return listGames.get(game).getStatus();
    }

    public boolean shoot(int game, int cell)
    {
        return listGames.get(game).shoot(cell);
    }

    public int getColumnsCount()
    {
        return intColumnsCount;
    }

    public int getGamesCount()
    {
        return listGames.size();
    }

    public int getHitsCount(int game, int player)
    {
        return listGames.get(game).getHitsCount(player);
    }

    public int getMissesCount(int game, int player)
    {
        return listGames.get(game).getMissesCount(player);
    }

    public int getOpponent(int game)
    {
        return listGames.get(game).getOpponent();
    }

    public int getPlayer(int game)
    {
        return listGames.get(game).getPlayer();
    }

    public int getRowsCount()
    {
        return intRowsCount;
    }

    public int newGame()
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
                    cell = stern = ((heading == 0 ? s.getLength() - 1 : 0) + random.nextInt(intRowsCount - (heading == 0 || heading == 180 ? s.getLength() - 1 : 0))) * intColumnsCount + (heading == 270 ? s.getLength() - 1 : 0) + random.nextInt(intColumnsCount - (heading == 90 || heading == 270 ? s.getLength() - 1 : 0));

                    for (int j = 0; j < s.getLength(); j++)
                    {
                        cell = stern + ((int)sin(heading * PI / 180) - (int)cos(heading * PI / 180) * intColumnsCount) * j;

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
                    ships.get(i).add(s.getShip(heading, minimum % intColumnsCount, minimum / intColumnsCount, cells));
                }
            }
        }

        listGames.add(new Game(random.nextInt(listPlayers.size()), listPlayers.size(), ships, hits, misses));

        return listGames.size() - 1;
    }

    public List<Integer> getPlayers()
    {
        return listPlayers;
    }

    public List<Set<Integer>> getHits(int game)
    {
        return listGames.get(game).getHits();
    }

    public List<Set<Integer>> getMisses(int game)
    {
        return listGames.get(game).getMisses();
    }

    public List<Ship> getShips(int game, int player)
    {
        return listGames.get(game).getShips(player);
    }

    private List<Ship> shuffleShips()
    {
        List<Ship> ships = new ArrayList<>(listShips);

        Collections.shuffle(ships);

        return ships;
    }
}