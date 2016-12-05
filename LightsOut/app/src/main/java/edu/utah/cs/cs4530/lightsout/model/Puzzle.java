package edu.utah.cs.cs4530.lightsout.model;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Shaun on 2016.12.02.
 */

public class Puzzle
{
    // fields

    private final int intMoves;
    private final List<Integer> listHints;
    private final Set<Integer> setCells;

    // constructors

    public Puzzle(int moves, @Nullable List<Integer> hints, Set<Integer> cells)
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