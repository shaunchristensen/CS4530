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

import edu.utah.cs.cs4530.project3.model.Battleship;

import static android.graphics.Color.WHITE;
import static android.graphics.Color.rgb;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.AdapterView.*;
import static android.widget.LinearLayout.VERTICAL;

public class ListFragmentMenu extends ListFragment implements ListAdapter, OnClickListener, OnItemClickListener
{
    // fields

    private Battleship battleship = Battleship.getBattleship();
    private int intGame;
    private OnGameClickListener onGameClickListener;
    private OnNewGameClickListener onNewGameClickListener ;

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
        return battleship.getGamesCount();
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView textview = new TextView(getActivity());

        if (position == intGame)
        {
            textview.setBackgroundColor(rgb(132, 132, 130));
        }
        else
        {
            textview.setBackgroundColor(WHITE);
        }
        textview.setText("Status: " + (battleship.getStatus(position) ? "In Progress" : "Game Over"));
        // Player/Winner: 1/2
        // shots by player
        return textview;
    }

    public void invalidate()
    {
        //        getListAdapter().notify();
        getListView().deferNotifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

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
        onGameClickListener = (OnGameClickListener)getActivity();
        onNewGameClickListener = (OnNewGameClickListener)getActivity();

        Button button = new Button(getActivity());
        button.setOnClickListener(this);
        button.setText("New Game");

        ListView listView = new ListView(getActivity());
        listView.setId(android.R.id.list);
        listView.setOnItemClickListener(this);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.addView(button, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        linearLayout.addView(listView, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1));
        linearLayout.setOrientation(VERTICAL);

//        setListAdapter((ListAdapter)getActivity());

        return linearLayout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        onGameClickListener.onGameClick(position);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer)
    {
    }

    public void setGame(int game)
    {
        intGame = game;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer)
    {
    }
}