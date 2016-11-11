/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.controller;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.utah.cs.cs4530.project4.R;
import edu.utah.cs.cs4530.project4.model.Game;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.rgb;
import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

public class ListFragmentMenu extends ListFragment implements OnClickListener, OnItemClickListener, OnItemSelectedListener
{
    // fields

    private Gson gson;
    private int intGameSet, intPadding;
    private List<Game> listGames;
    private List<String> listGameSets;
    private OnGameClickListener onGameClickListener;
    private OnGameSetSelectListener onGameSetSelectListener;
    private OnNewGameClickListener onNewGameClickListener;
    private Spinner spinner;
    private String stringGameID;
    private Type type;

    // interfaces

    public interface OnGameClickListener
    {
        void onGameClick(String id);
    }

    public interface OnGameSetSelectListener
    {
        void onGameSetSelect(int index);
    }

    public interface OnNewGameClickListener
    {
        void onNewGameClick();
    }

    // methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        onGameClickListener = (OnGameClickListener)getActivity();
        onGameSetSelectListener = (OnGameSetSelectListener)getActivity();
        onNewGameClickListener = (OnNewGameClickListener)getActivity();

        Button button = new Button(getActivity());
        button.setBackgroundColor(rgb(192, 192, 192));
        button.setOnClickListener(this);
        button.setText("New Game");

        ListView listView = new ListView(getActivity());
        listView.setId(android.R.id.list);

        spinner = new Spinner(getActivity());
        spinner.setAdapter(new SpinnerAdapterMenu());
        spinner.setOnItemSelectedListener(this);

        LinearLayout linearLayoutSpinner = new LinearLayout(getActivity());
        linearLayoutSpinner.addView(spinner, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        linearLayoutSpinner.setBackgroundColor(WHITE);

        LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        layoutParams.setMargins(0, intPadding, 0, intPadding);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.addView(button, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        linearLayout.addView(linearLayoutSpinner, layoutParams);
        linearLayout.addView(listView, new LayoutParams(MATCH_PARENT, 0, 1));
        linearLayout.setOrientation(VERTICAL);

        return linearLayout;
    }

    public void invalidateViews()
    {
        if (isAdded())
        {
            getListView().invalidateViews();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemClickListener(this);
        setListAdapter(new ListAdapterMenu());
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
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        gson = new Gson();
        type = new TypeToken<List<Game>>(){}.getType();

        if (savedInstanceState != null)
        {
            intGameSet = savedInstanceState.getInt("intGameSet");
            intPadding = savedInstanceState.getInt("intPadding");
            listGames = gson.fromJson(savedInstanceState.getString("listGames"), type);
            stringGameID = savedInstanceState.getString("stringGameID");
        }
        else
        {
            listGames = new ArrayList<>();
            listGameSets = new ArrayList<>();

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id)
    {
        String s = listGames.get(i).toString();

        if (!stringGameID.equalsIgnoreCase(s))
        {
            stringGameID = s;

            onGameClickListener.onGameClick(s);
            invalidateViews();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long id)
    {
        if (intGameSet != i)
        {
            intGameSet = i;

            onGameSetSelectListener.onGameSetSelect(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt("intGameSet", intGameSet);
        outState.putInt("intPadding", intPadding);
        outState.putString("listGames", gson.toJson(listGames, type));
        outState.putString("stringGameID", stringGameID);

        super.onSaveInstanceState(outState);
    }

    public void setGames(List<Game> games)
    {
        listGames = games;

        invalidateViews();
    }

    public void setGameSets(List<String> gameSets)
    {
        listGameSets = gameSets;
    }

    public void setPadding(int padding)
    {
        intPadding = padding;
    }

    public void setSelection(int gameSet)
    {
        spinner.setSelection(gameSet);
    }

    // classes

    private class ListAdapterMenu implements ListAdapter
    {
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
        public boolean isEnabled(int i)
        {
            return true;
        }

        @Override
        public int getCount()
        {
            return listGames.size();
        }

        @Override
        public int getItemViewType(int i)
        {
            return 0;
        }

        @Override
        public int getViewTypeCount()
        {
            return 1;
        }

        @Override
        public long getItemId(int i)
        {
            return 0;
        }

        @Override
        public Object getItem(int i)
        {
            return null;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            TextView textView = new TextView(getActivity());
Log.i("Null", "" + listGames.get(i).getID());
            if (listGames.get(i).getID().equalsIgnoreCase(stringGameID))
            {
                textView.setBackgroundColor(rgb(64, 164, 223));
            }
            else
            {
                textView.setBackgroundColor(WHITE);
            }

            textView.setPadding(intPadding, intPadding, intPadding, intPadding);
            textView.setText(listGames.get(i).toString());
            textView.setTextColor(BLACK);
            textView.setTextSize(COMPLEX_UNIT_SP, 12.5f);

            return textView;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver)
        {
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver)
        {
        }
    }

    private class SpinnerAdapterMenu implements SpinnerAdapter
    {
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
        public int getCount()
        {
            return listGameSets.size();
        }

        @Override
        public int getItemViewType(int i)
        {
            return 0;
        }

        @Override
        public int getViewTypeCount()
        {
            return 1;
        }

        @Override
        public long getItemId(int i)
        {
            return 0;
        }

        @Override
        public Object getItem(int i)
        {
            return null;
        }

        @Override
        public View getDropDownView(int i, View view, ViewGroup viewGroup)
        {
            TextView textView = (TextView)getView(i, view, viewGroup);

            if (i == intGameSet)
            {
                textView.setBackgroundColor(rgb(64, 164, 223));
            }
            else
            {
                textView.setBackgroundColor(WHITE);
            }

            return textView;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            TextView textView = new TextView(getActivity());
            textView.setPadding(intPadding, intPadding, intPadding, intPadding);
            textView.setText(listGameSets.get(i));
            textView.setTextColor(BLACK);
            textView.setTextSize(COMPLEX_UNIT_SP, 12.5f);

            return textView;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver)
        {
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver)
        {
        }
    }
}