/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.21
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.controller;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.rgb;
import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.AdapterView.*;
import static android.widget.LinearLayout.VERTICAL;

public class ListFragmentMenu extends ListFragment implements ListAdapter, OnClickListener, OnItemClickListener
{
    // fields

    private int intGame, intPadding;
    private List<String> listGameStrings;
    private OnGameClickListener onGameClickListener;
    private OnNewGameClickListener onNewGameClickListener;

    // interfaces

    public interface OnGameClickListener
    {
        void onGameClick(int game);
    }

    public interface OnNewGameClickListener
    {
        void onNewGameClick();
    }

    // methods

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

    @Override
    public int getCount()
    {
        return listGameStrings.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return 0;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView textview = new TextView(getActivity());

        if (position == intGame)
        {
            textview.setBackgroundColor(rgb(192, 192, 192));
        }
        else
        {
            textview.setBackgroundColor(WHITE);
        }

        textview.setText(listGameStrings.get(position));
        textview.setTextColor(BLACK);
        textview.setTextSize(COMPLEX_UNIT_SP, 12.5f);

        return textview;
    }

    public void addGameString(String gameString)
    {
        listGameStrings.add(gameString);

        intGame = listGameStrings.size() - 1;

        getListView().invalidateViews();
    }

    public void clearSelection()
    {
        intGame = -1;

        getListView().invalidateViews();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemClickListener(this);
        setListAdapter(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v instanceof Button)
        {
            onNewGameClickListener.onNewGameClick();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            intGame = savedInstanceState.getInt("intGame");
            intPadding = savedInstanceState.getInt("intPadding");
            listGameStrings = new ArrayList<>(savedInstanceState.getStringArrayList("listGameStrings"));
        }

        onGameClickListener = (OnGameClickListener)getActivity();
        onNewGameClickListener = (OnNewGameClickListener)getActivity();

        Button button = new Button(getActivity());
        button.setBackgroundColor(rgb(192, 192, 192));
        button.setOnClickListener(this);
        button.setText("New Game");

        ListView listView = new ListView(getActivity());
        listView.setId(android.R.id.list);
        listView.setPadding(0, intPadding, 0, 0);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.addView(button, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        linearLayout.addView(listView, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1));
        linearLayout.setOrientation(VERTICAL);

        return linearLayout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        intGame = position;

        onGameClickListener.onGameClick(position);

        getListView().invalidateViews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt("intGame", intGame);
        outState.putInt("intPadding", intPadding);
        outState.putStringArrayList("listGameStrings", (ArrayList<String>)listGameStrings);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer)
    {
    }

    public void removeGameString(int game)
    {
        listGameStrings.remove(game);

        clearSelection();
    }

    public void setGameString(int game, String gameString)
    {
        listGameStrings.set(game, gameString);

        getListView().invalidateViews();
    }

    public void setGameStrings(List<String> gameStrings)
    {
        intGame = -1;
        listGameStrings = gameStrings;
    }

    public void setPadding(int padding)
    {
        intPadding = padding;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer)
    {
    }
}