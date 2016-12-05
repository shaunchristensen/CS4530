/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.12.02
 * Assignment: Project F - Lights Out
 */

package edu.utah.cs.cs4530.lightsout.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Puzzle
{
    // fields

    private final int intMoves;
    private final List<Integer> listHints;
    private final Set<Integer> setCells;

    // constructors

    public Puzzle(int moves, List<Integer> hints, Set<Integer> cells)
    {
        intMoves = moves;
        listHints = hints;
        setCells = cells;
    }

    // methods

    public int getMoves()
    {
        return intMoves;
    }

    public List<Integer> getHints()
    {
        return new ArrayList<>(listHints);
    }

    public Set<Integer> getCells()
    {
        return new HashSet<>(setCells);
    }
}