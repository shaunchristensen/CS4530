/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.21
 * Assignment: Project 3 - MVC Battleship
 */

// ITC Machine Bold font credit http://www.onlinewebfonts.com

package edu.utah.cs.cs4530.project3.controller;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.rgb;
import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.createFromAsset;
import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;
import static java.lang.Math.*;

public class FragmentSummary extends Fragment
{
    // fields

    private String stringPlayer, stringPlayer1, stringPlayer2;
    private TextView textViewPlayer, textViewPlayer1, textViewPlayer2;

    // methods

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        TextView textViewBattleship = new TextView(getActivity());
        textViewBattleship.setText("Battleship");
        textViewBattleship.setTextColor(rgb(132, 132, 130));
        textViewBattleship.setTextSize(COMPLEX_UNIT_SP, 100);
        textViewBattleship.setTypeface(createFromAsset(getActivity().getAssets(), "fonts/ITC Machine Bold.ttf"));

        textViewPlayer = new TextView(getActivity());
        textViewPlayer.setText(stringPlayer);
        textViewPlayer.setTextColor(BLACK);
        textViewPlayer.setTextSize(COMPLEX_UNIT_SP, 25);
        textViewPlayer.setTypeface(null, BOLD);

        textViewPlayer1 = new TextView(getActivity());
        textViewPlayer1.setText(stringPlayer1);
        textViewPlayer1.setTextColor(BLACK);
        textViewPlayer1.setTextSize(COMPLEX_UNIT_SP, 25);

        textViewPlayer2 = new TextView(getActivity());
        textViewPlayer2.setText(stringPlayer2);
        textViewPlayer2.setTextColor(BLACK);
        textViewPlayer2.setTextSize(COMPLEX_UNIT_SP, 25);

        TextView textViewShots = new TextView(getActivity());
        textViewShots.setText("Shots");
        textViewShots.setTextColor(BLACK);
        textViewShots.setTextSize(COMPLEX_UNIT_SP, 25);
        textViewShots.setTypeface(null, BOLD);

        LayoutParams layoutParamsBattleship = new LayoutParams(WRAP_CONTENT, 0, 4);
        layoutParamsBattleship.gravity = CENTER_HORIZONTAL;
        layoutParamsBattleship.topMargin = getResources().getDisplayMetrics().heightPixels / 8;

        LayoutParams layoutParamsPlayer = new LayoutParams(WRAP_CONTENT, 0, 2);
        layoutParamsPlayer.gravity = CENTER_HORIZONTAL;

        LayoutParams layoutParamsPlayer1 = new LayoutParams(WRAP_CONTENT, 0, 1);
        layoutParamsPlayer1.gravity = CENTER_HORIZONTAL;

        LayoutParams layoutParamsPlayer2 = new LayoutParams(WRAP_CONTENT, 0, 1);
        layoutParamsPlayer2.gravity = CENTER_HORIZONTAL;

        LayoutParams layoutParamsShots = new LayoutParams(WRAP_CONTENT, 0, 1);
        layoutParamsShots.gravity = CENTER_HORIZONTAL;

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.addView(textViewBattleship, layoutParamsBattleship);
        linearLayout.addView(textViewPlayer, layoutParamsPlayer);
        linearLayout.addView(textViewShots, layoutParamsShots);
        linearLayout.addView(textViewPlayer1, layoutParamsPlayer1);
        linearLayout.addView(textViewPlayer2, layoutParamsPlayer2);
        linearLayout.setOrientation(VERTICAL);

        setRetainInstance(true);

        return linearLayout;
    }

    public void setText(int player, int hitsCount0, int missesCount0, int hitsCount1, int missesCount1)
    {
        stringPlayer = "Game Over ‐ Player " + (player + 1) + " won.";
        stringPlayer1 = "Player 1: " + hitsCount0 + "/" + (hitsCount0 + missesCount0) + " (" + round(hitsCount0 * 100d / (hitsCount0 + missesCount0)) + "%)";
        stringPlayer2 = "Player 2: " + hitsCount1 + "/" + (hitsCount1 + missesCount1) + " (" + round(hitsCount1 * 100d / (hitsCount1 + missesCount1)) + "%)";

        textViewPlayer.setText(stringPlayer);
        textViewPlayer.invalidate();

        textViewPlayer1.setText(stringPlayer1);
        textViewPlayer1.invalidate();

        textViewPlayer2.setText(stringPlayer2);
        textViewPlayer2.invalidate();
    }
}