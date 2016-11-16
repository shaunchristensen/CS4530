/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.utah.cs.cs4530.project4.view.LinearLayoutGrid;
import edu.utah.cs.cs4530.project4.view.LinearLayoutGrid.OnShootListener;

public class FragmentGame extends Fragment
{
    // fields

    private int intColumnsCount, intPadding, intRowsCount;
    private LinearLayoutGrid linearLayoutGrid;
    private List<Integer> listPlayers;

    // methods

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return linearLayoutGrid;
    }

    public void addShot(boolean hit, int cell)
    {
        linearLayoutGrid.addShot(hit, cell);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
        {
            intColumnsCount = savedInstanceState.getInt("intColumnsCount");
            intPadding = savedInstanceState.getInt("intPadding");
            intRowsCount = savedInstanceState.getInt("intRowsCount");
            listPlayers = new ArrayList<>(savedInstanceState.getIntegerArrayList("listPlayers"));
        }

        if (linearLayoutGrid == null)
        {
//            linearLayoutGrid = new LinearLayoutGrid(getActivity(), intRowsCount, intColumnsCount, intPadding, (OnShootListener)getActivity());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt("intColumnsCount", intColumnsCount);
        outState.putInt("intPadding", intPadding);
        outState.putInt("intRowsCount", intRowsCount);
        outState.putIntegerArrayList("listPlayers", (ArrayList<Integer>)listPlayers);

        super.onSaveInstanceState(outState);
    }

    public void setColumnsCount(int columnsCount)
    {
        intColumnsCount = columnsCount;
    }

    public void setGame(List<Set<Integer>> cells, List<Set<Integer>> hits, List<Set<Integer>> misses, List<Set<Integer>> ships)
    {
//        linearLayoutGrid.setGame(cells, hits, misses, ships);
    }

    public void setPadding(int padding)
    {
        intPadding = padding;
    }

    public void setPlayers(int opponent, int player)
    {
//        linearLayoutGrid.setPlayers(opponent, player);
    }

    public void setPlayers(List<Integer> players)
    {
        listPlayers = players;
    }

    public void setRowsCount(int rowsCount)
    {
        intRowsCount = rowsCount;
    }

    public void setStatus(boolean status)
    {
        linearLayoutGrid.setStatus(status);
    }
}