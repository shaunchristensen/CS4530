/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

// ITC Machine Bold font credit: http://www.onlinewebfonts.com

package edu.utah.cs.cs4530.project4.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.rgb;
import static android.graphics.Typeface.createFromAsset;
import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

public class FragmentSummary extends Fragment
{
    // fields

    private int intMargin;
    private String stringSummary;
    private TextView textViewSummary;

    // methods

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            intMargin = savedInstanceState.getInt("intMargin");
            stringSummary = savedInstanceState.getString("stringSummary");
        }
        else
        {
            stringSummary = "";
        }

        TextView textViewBattleship = new TextView(getActivity());
        textViewBattleship.setText("Battleship");
        textViewBattleship.setTextColor(rgb(132, 132, 130));
        textViewBattleship.setTextSize(COMPLEX_UNIT_SP, 75);
        textViewBattleship.setTypeface(createFromAsset(getActivity().getAssets(), "fonts/ITC Machine Bold.ttf"));

        textViewSummary = new TextView(getActivity());
        textViewSummary.setText(Html.fromHtml(stringSummary == null ? "" : stringSummary));
        textViewSummary.setTextColor(BLACK);
        textViewSummary.setTextSize(COMPLEX_UNIT_SP, 25);

        LayoutParams layoutParamsBattleship = new LayoutParams(WRAP_CONTENT, 0, 1);
        layoutParamsBattleship.gravity = CENTER_HORIZONTAL;
        layoutParamsBattleship.topMargin = intMargin;

        LayoutParams layoutParamsSummary = new LayoutParams(WRAP_CONTENT, 0, 2);
        layoutParamsSummary.gravity = CENTER;

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.addView(textViewBattleship, layoutParamsBattleship);
        linearLayout.addView(textViewSummary, layoutParamsSummary);
        linearLayout.setOrientation(VERTICAL);

        return linearLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt("intMargin", intMargin);
        outState.putString("stringSummary", stringSummary);

        super.onSaveInstanceState(outState);
    }

    public void setMargin(int margin)
    {
        intMargin = margin;
    }

    public void setText(String game, String player1, String player2, int shots, String status, String winner)
    {
        stringSummary = "<b>Game:</b> " + game + "<br /><b>Player 1:</b> " + player1 + "<br /><b>Player 2:</b> " + player2 + "<br /><b>Shots:</b> " + shots + "<br /><b>Status:</b> " + status + (winner == null ? "" : "<br /><b>Winner:</b> " + winner);

        textViewSummary.setText(Html.fromHtml(stringSummary == null ? "" : stringSummary));
    }
}