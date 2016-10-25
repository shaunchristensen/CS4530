/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.21
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Set;

import edu.utah.cs.cs4530.project3.model.Battleship;
import edu.utah.cs.cs4530.project3.view.LinearLayoutGrid;
import edu.utah.cs.cs4530.project3.view.LinearLayoutGrid.OnShootListener;
import edu.utah.cs.cs4530.project3.view.ship.Ship;

public class FragmentGame extends Fragment
{
    // fields

    private Battleship battleship = Battleship.getBattleship();
    private LinearLayoutGrid linearLayoutGrid;

    // methods

    public void addShot(boolean hit, int cell)
    {
        linearLayoutGrid.addShot(hit, cell);
    }

    public void loadGame(boolean status, int opponent, int player, List<List<Ship>> ships, List<Set<Integer>> hits, List<Set<Integer>> misses)
    {
        linearLayoutGrid.loadGame(status, opponent, player, ships, hits, misses);
    }

    public void setPlayers(int opponent, int player)
    {
        linearLayoutGrid.setPlayers(opponent, player);
    }

    public void setStatus(boolean status)
    {
        linearLayoutGrid.setStatus(status);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        linearLayoutGrid = new LinearLayoutGrid(getActivity(), battleship.getRowsCount(), battleship.getColumnsCount(), battleship.getPlayers(), (OnShootListener)getActivity());

        return linearLayoutGrid;
    }
}