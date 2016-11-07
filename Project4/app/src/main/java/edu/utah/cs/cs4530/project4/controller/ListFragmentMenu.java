/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.controller;

import android.database.DataSetObserver;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.TimerTask;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.rgb;
import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_OUTSIDE;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.AdapterView.*;
import static android.widget.LinearLayout.VERTICAL;
import static java.lang.Math.abs;

public class ListFragmentMenu extends ListFragment implements ListAdapter, OnClickListener, OnItemClickListener, OnItemLongClickListener, OnTouchListener
{
    // fields

    private boolean booleanItemLongClick, booleanItemClick;
    private float floatX;
    private Handler handler;
    private int intItemClick, intItemLongClick, intPadding;
    private List<String> listGameStrings;
    private OnGameClickListener onGameClickListener;
    private OnNewGameClickListener onNewGameClickListener;
    private OnGameTouchListener onGameTouchListener;
    private PointF pointF;
    private Runnable runnable;

    // interfaces

    public interface OnGameClickListener
    {
        void onGameClick(int game);
    }

    public interface OnNewGameClickListener
    {
        void onNewGameClick();
    }

    public interface OnGameTouchListener
    {
        void onGameTouch(int game);
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (position == intItemLongClick)
        {
            clearItemLongClick();
        }
        else
        {
            booleanItemLongClick = true;
            intItemLongClick = position;
        }

        invalidateViews();

        return true;
    }

    @Override
    public boolean onTouch(final View view, MotionEvent motionEvent)
    {
        if (getListView().getPositionForView(view) == intItemLongClick)
        {
            if (booleanItemLongClick)
            {
                if (motionEvent.getActionMasked() == ACTION_DOWN)
                {
                    booleanItemClick = true;
                    floatX = view.getX();
                    pointF = new PointF(motionEvent.getRawX(), motionEvent.getRawY());
                    runnable = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            booleanItemLongClick = false;

                            view.setAlpha(1);
                            view.setBackgroundColor(intItemClick == intItemLongClick ? rgb(64, 164, 223) : WHITE);
                            view.setX(floatX);
                            view.invalidate();
                        }
                    };

                    handler.postDelayed(runnable, 500);
                }
                else
                {
                    float absX = abs(view.getX() - floatX);

                    if (booleanItemClick && absX >= view.getWidth() / 25 || motionEvent.getActionMasked() == ACTION_CANCEL)
                    {
                        handler.removeCallbacks(runnable);

                        booleanItemClick = false;
                    }

                    if (motionEvent.getActionMasked() == ACTION_CANCEL || motionEvent.getActionMasked() == ACTION_UP)
                    {
                        if (booleanItemClick)
                        {
                            handler.removeCallbacks(runnable);

                            getListView().performItemClick(view, getListView().getPositionForView(view), view.getId());
                        }
                        else if (abs(motionEvent.getRawX() - pointF.x) < abs(motionEvent.getRawY() - pointF.y) ||  absX < view.getWidth() / 2)
                        {
                            view.setAlpha(1);
                            view.setX(floatX);
                            view.invalidate();
                        }
                        else
                        {
                            onGameTouchListener.onGameTouch(intItemLongClick);
                        }
                    }
                    else
                    {
                        view.setAlpha(absX < view.getWidth() / 2 ? 1 - absX / view.getWidth() / 2 : 0);
                        view.setX(floatX + motionEvent.getRawX() - pointF.x);
                        view.invalidate();
                    }
                }
            }
            else if (motionEvent.getActionMasked() == ACTION_CANCEL || motionEvent.getActionMasked() == ACTION_UP)
            {
                intItemLongClick = -1;
            }

            return true;
        }
        else
        {
            return false;
        }
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
        else if (position == intItemClick)
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            booleanItemLongClick = savedInstanceState.getBoolean("booleanItemLongClick");
            intItemClick = savedInstanceState.getInt("intItemClick");
            intItemLongClick = savedInstanceState.getInt("intItemLongClick");
            intPadding = savedInstanceState.getInt("intPadding");
            listGameStrings = new ArrayList<>(savedInstanceState.getStringArrayList("listGameStrings"));
        }

        onGameClickListener = (OnGameClickListener)getActivity();
        onGameTouchListener = (OnGameTouchListener)getActivity();
        onNewGameClickListener = (OnNewGameClickListener)getActivity();
        handler = new Handler();

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

    public void addGameString(String gameString)
    {
        listGameStrings.add(gameString);

        intItemClick = listGameStrings.size() - 1;

        clearItemLongClick();
        setListAdapter(this);
        getListView().invalidateViews();
        getListView().setSelection(intItemClick);
    }

    public void clearItemClick()
    {
        booleanItemClick = false;
        intItemClick = -1;
    }

    public void clearItemLongClick()
    {
        booleanItemLongClick = false;
        intItemLongClick = -1;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        onGameClickListener.onGameClick(position);

        intItemClick = position;

        clearItemLongClick();
        invalidateViews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putBoolean("booleanItemLongClick", booleanItemLongClick);
        outState.putInt("intItemClick", intItemClick);
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

        if (intItemClick == intItemLongClick)
        {
            clearItemClick();
        }
        else if (game < intItemClick)
        {
            intItemClick--;
        }

        clearItemLongClick();
        invalidateViews();
    }

    public void setGameString(int game, String gameString)
    {
        listGameStrings.set(game, gameString);
        invalidateViews();
    }

    public void setGameStrings(List<String> gameStrings)
    {
        listGameStrings = gameStrings;

        clearItemClick();
        clearItemLongClick();
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