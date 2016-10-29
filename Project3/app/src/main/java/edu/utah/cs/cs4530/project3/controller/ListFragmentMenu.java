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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.rgb;
import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.AdapterView.*;
import static android.widget.LinearLayout.VERTICAL;
import static java.lang.Math.abs;

public class ListFragmentMenu extends ListFragment implements ListAdapter, OnClickListener, OnItemClickListener, OnItemLongClickListener, OnTouchListener
{
    // fields

    private boolean booleanItemLongClick, booleanTouch;
    private float floatX, floatY;
    private int intGame, intItemLongClick, intPadding;
    private List<String> listGameStrings;
    private OnGameClickListener onGameClickListener;
    private OnGameFlingListener onGameFlingListener;
    private OnNewGameClickListener onNewGameClickListener;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if (booleanItemLongClick)
        {
            if (motionEvent.getActionMasked() == ACTION_DOWN)
            {
                if (!booleanTouch)
                booleanTouch = getListView().getPositionForView(view) == intItemLongClick ? true: false;
                floatX = motionEvent.getRawX();
                floatY = motionEvent.getRawY();
            }
            else if (motionEvent.getActionMasked() == ACTION_UP)
            {
                /* view.getHitRect(viewRect);
 if(viewRect.contains(
         Math.round(view.getX() + event.getX()),
         Math.round(view.getY() + event.getY()))) {
   // inside
 } else {
   // outside
 }*/
                booleanTouch = false;
Log.i("xasdf", "X: " + abs(motionEvent.getRawX() - floatX) + " Width: " + view.getWidth());
                if (abs(motionEvent.getRawX() - floatX) > abs(motionEvent.getRawY() - floatY) && abs(motionEvent.getRawX() - floatX) > view.getWidth())
                {
                    onGameFlingListener.onGameFling(intGame);
                }
                else
                {
                    view.setAlpha(1);
                    view.setTranslationX(0);
                    view.invalidate();
                }
                // check whether to delete
            }
            else
            {
                Log.i("xczxcv", "X: " + motionEvent.getRawX() + " Y: " + motionEvent.getRawY());
                view.setAlpha(abs(motionEvent.getRawX() - floatX) < view.getWidth() ? 1 - abs(motionEvent.getRawX() - floatX) / view.getWidth() : 0);
                view.setTranslationX(motionEvent.getRawX() - floatX);
                view.invalidate();
            }

            return booleanTouch;
        }

        return false;
    }

    // interfaces

    public interface OnGameClickListener
    {
        void onGameClick(int game);
    }

    public interface OnGameFlingListener
    {
        void onGameFling(int game);
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
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView textView = new TextView(getActivity());

        if (booleanItemLongClick && position == intItemLongClick)
        {
            textView.setBackgroundColor(RED);
        }
        else if (position == intGame)
        {
            textView.setBackgroundColor(rgb(64, 164, 223));
        }
        else
        {
            textView.setBackgroundColor(WHITE);
        }

        textView.setOnTouchListener(this);
        textView.setPadding(intPadding, intPadding, intPadding, intPadding);
        textView.setText(listGameStrings.get(position));
        textView.setTextColor(BLACK);
        textView.setTextSize(COMPLEX_UNIT_SP, 12.5f);

        return textView;
    }

    public void addGameString(String gameString)
    {
        synchronized (listGameStrings)
        {
            listGameStrings.add(gameString);

            intGame = listGameStrings.size() - 1;

            getListView().requestLayout();
//            getListView().invalidateViews();
            getListView().smoothScrollToPosition(intGame);
        }
    }

    public void clearSelection()
    {
        booleanItemLongClick = false;
        intGame = -1;

        invalidateViews();
    }

    private void invalidateViews()
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
        getListView().setOnItemLongClickListener(this);
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
            booleanItemLongClick = savedInstanceState.getBoolean("booleanItemLongClick");
            intGame = savedInstanceState.getInt("intGame");
            intItemLongClick = savedInstanceState.getInt("intItemLongClick");
            intPadding = savedInstanceState.getInt("intPadding");
            listGameStrings = new ArrayList<>(savedInstanceState.getStringArrayList("listGameStrings"));
        }

        onGameClickListener = (OnGameClickListener)getActivity();
        onGameFlingListener = (OnGameFlingListener)getActivity();
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
        booleanItemLongClick = false;
        intGame = position;

        getListView().getChildAt(intItemLongClick).setOnTouchListener(null);
        onGameClickListener.onGameClick(position);
        invalidateViews();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        booleanItemLongClick = true;
        intItemLongClick = position;

        getListView().getChildAt(intItemLongClick).setOnTouchListener(null);
        getListView().getChildAt(position).setOnTouchListener(this);
        invalidateViews();

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putBoolean("booleanItemLongClick", booleanItemLongClick);
        outState.putInt("intGame", intGame);
        outState.putInt("intItemLongClick", intItemLongClick);
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
        getListView().getChildAt(intGame).setOnTouchListener(null);

        listGameStrings.remove(game);

        clearSelection();
        getListView().invalidateViews();
    }

    public void setGameString(int game, String gameString)
    {
        listGameStrings.set(game, gameString);

        invalidateViews();
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