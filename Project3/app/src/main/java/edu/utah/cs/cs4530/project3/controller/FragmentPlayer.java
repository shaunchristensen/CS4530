/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.21
 * Assignment: Project 3 - MVC Battleship
 */

// ITC Machine Bold font credit: http://www.onlinewebfonts.com

package edu.utah.cs.cs4530.project3.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.Stack;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.rgb;
import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.createFromAsset;
import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

public class FragmentPlayer extends Fragment implements OnClickListener
{
    // fields

    private int intColumnsCount, intMargin;
    private OnOKClickListener onOKClickListener;
    private String stringOpponent, stringPlayer;
    private TextView textViewOpponent, textViewPlayer;

    // interfaces

    public interface OnOKClickListener
    {
        void onOKClick(Fragment fragment);
    }

    // methods

    private String getRowColumnString(int cell)
    {
        int row = cell / intColumnsCount + 1;

        Stack<Integer> stack = new Stack<>();
        StringBuilder stringBuilder = new StringBuilder();

        while (row > 0)
        {
            stack.push(row % 26);

            row /= 26;
        }

        while (stack.size() > 0)
        {
            stringBuilder.append((char)(stack.pop() + 64));
        }

        stringBuilder.append(cell % intColumnsCount + 1);

        return stringBuilder.toString();
    }

    @Override
    public void onClick(View v)
    {
        if (v instanceof Button)
        {
            onOKClickListener.onOKClick(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            intColumnsCount = savedInstanceState.getInt("intColumnsCount");
            intMargin = savedInstanceState.getInt("intMargin");
            stringOpponent = savedInstanceState.getString("stringOpponent");
            stringPlayer = savedInstanceState.getString("stringPlayer");
        }

        onOKClickListener = (OnOKClickListener)getActivity();

        Button button = new Button(getActivity());
        button.setBackgroundColor(rgb(192, 192, 192));
        button.setOnClickListener(this);
        button.setText("OK");
        button.setTextColor(BLACK);
        button.setTextSize(COMPLEX_UNIT_SP, 25);

        TextView textViewBattleship = new TextView(getActivity());
        textViewBattleship.setText("Battleship");
        textViewBattleship.setTextColor(rgb(132, 132, 130));
        textViewBattleship.setTextSize(COMPLEX_UNIT_SP, 75);
        textViewBattleship.setTypeface(createFromAsset(getActivity().getAssets(), "fonts/ITC Machine Bold.ttf"));

        textViewOpponent = new TextView(getActivity());
        textViewOpponent.setText(stringOpponent);
        textViewOpponent.setTextColor(BLACK);
        textViewOpponent.setTextSize(COMPLEX_UNIT_SP, 25);

        textViewPlayer = new TextView(getActivity());
        textViewPlayer.setText(stringPlayer);
        textViewPlayer.setTextColor(BLACK);
        textViewPlayer.setTextSize(COMPLEX_UNIT_SP, 25);
        textViewPlayer.setTypeface(null, BOLD);

        LayoutParams layoutParamsBattleship = new LayoutParams(WRAP_CONTENT, 0, 2);
        layoutParamsBattleship.gravity = CENTER_HORIZONTAL;
        layoutParamsBattleship.topMargin = intMargin;

        LayoutParams layoutParamsButton = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParamsButton.bottomMargin = intMargin;
        layoutParamsButton.gravity = CENTER_HORIZONTAL;

        LayoutParams layoutParamsOpponent = new LayoutParams(WRAP_CONTENT, 0, 1);
        layoutParamsOpponent.gravity = CENTER;

        LayoutParams layoutParamsPlayer = new LayoutParams(WRAP_CONTENT, 0, 1);
        layoutParamsPlayer.gravity = CENTER;

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.addView(textViewBattleship, layoutParamsBattleship);
        linearLayout.addView(textViewOpponent, layoutParamsOpponent);
        linearLayout.addView(textViewPlayer, layoutParamsPlayer);
        linearLayout.addView(button, layoutParamsButton);
        linearLayout.setOrientation(VERTICAL);

        return linearLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt("intColumnsCount", intColumnsCount);
        outState.putInt("intMargin", intMargin);
        outState.putString("stringOpponent", stringOpponent);
        outState.putString("stringPlayer", stringPlayer);

        super.onSaveInstanceState(outState);
    }

    public void setColumnsCount(int columnsCount)
    {
        intColumnsCount = columnsCount;
    }

    public void setMargin(int margin)
    {
        intMargin = margin;
    }

    public void setText(boolean hit, boolean status, int cell, int opponent, int player)
    {
        setText("Player " + (opponent + 1) + ": " + getRowColumnString(cell) + " ‐ " + (hit ? "Hit!" : "Miss."), "Player " + (player + 1) + (status ? " press OK." : " won!"));
    }

    public void setText(boolean status, int opponent, int player)
    {
        setText(status ? "" : "Player " + (player + 1) + " won.", status ? "Player " + (player + 1) + " press OK." : "Game Over");
    }

    private void setText(String opponent, String player)
    {
        stringOpponent = opponent;
        stringPlayer = player;

        textViewOpponent.setText(stringOpponent);
        textViewPlayer.setText(stringPlayer);
    }
}