/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.21
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.controller;

import android.animation.ObjectAnimator;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
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
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.AdapterView.*;
import static android.widget.LinearLayout.VERTICAL;
import static java.lang.Math.abs;

public class ListFragmentMenu extends ListFragment implements ListAdapter, OnClickListener, OnGestureListener, OnItemClickListener, OnItemLongClickListener, OnTouchListener
{
    // fields

    private boolean booleanItemLongClick;
    private GestureDetector gestureDetector;
    private int intGame, intItemLongClick, intPadding;
    private List<String> listGameStrings;
    private OnGameClickListener onGameClickListener;
    private OnGameFlingListener onGameFlingListener;
    private OnNewGameClickListener onNewGameClickListener;

    @Override
    public boolean onDown(MotionEvent e)
    {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e)
    {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        float x = e1.getX() - e2.getX();
        float y = e1.getY() - e2.getY();

        int width = getListView().getWidth() / 4;

        if (booleanItemLongClick && abs(x) > abs(y) && abs(x) > width && velocityX > width)// && onGameFlingListener.onGameFling(intItemLongClick))
        {
            booleanItemLongClick = false;

            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(getListView().getChildAt(intItemLongClick), "x", (int)e2.getX());
            objectAnimator.setDuration(5000);
            objectAnimator.start();
            // animate height to 0 in half a second?
            if (x < 0)
            {
                // left
            }
            else
            {
                // rght
            }

//            listGameStrings.remove(intItemLongClick);
            invalidateViews();

            return true;
        }

        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        return gestureDetector.onTouchEvent(event);
    }

    // interfaces

    public interface OnGameFlingListener
    {
        boolean onGameFling(int game);
    }

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

        textView.setPadding(intPadding, intPadding, intPadding, intPadding);
        textView.setText(listGameStrings.get(position));
        textView.setTextColor(BLACK);
        textView.setTextSize(COMPLEX_UNIT_SP, 12.5f);

        return textView;
    }

    public void addGameString(String gameString)
    {
        listGameStrings.add(gameString);

        booleanItemLongClick = false;
        intGame = listGameStrings.size() - 1;

        invalidateViews();
        getListView().smoothScrollToPosition(intGame);
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
        getListView().setOnTouchListener(this);
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

        gestureDetector = new GestureDetector(getActivity(), this);
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

        onGameClickListener.onGameClick(position);

        invalidateViews();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        booleanItemLongClick = true;
        intItemLongClick = position;

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
        listGameStrings.remove(game);

        clearSelection();
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