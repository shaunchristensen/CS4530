/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

// ITC Machine Bold font credit http://www.onlinewebfonts.com

package edu.utah.cs.cs4530.project4.controller;

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
import static android.widget.LinearLayout.*;

public class FragmentMenu extends Fragment
{
    // fields

    private int intMargin;

    // methods

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        TextView textViewBattleship = new TextView(getActivity());
        textViewBattleship.setText("Battleship");
        textViewBattleship.setTextColor(rgb(132, 132, 130));
        textViewBattleship.setTextSize(COMPLEX_UNIT_SP, 75);
        textViewBattleship.setTypeface(createFromAsset(getActivity().getAssets(), "fonts/ITC Machine Bold.ttf"));

        TextView textViewInstruction = new TextView(getActivity());
        textViewInstruction.setText("Press New Game or select a game.");
        textViewInstruction.setTextColor(BLACK);
        textViewInstruction.setTextSize(COMPLEX_UNIT_SP, 25);
        textViewInstruction.setTypeface(null, BOLD);

        LayoutParams layoutParamsBattleship = new LayoutParams(WRAP_CONTENT, 0, 1);
        layoutParamsBattleship.gravity = CENTER_HORIZONTAL;
        layoutParamsBattleship.topMargin = intMargin;

        LayoutParams layoutParamsInstruction = new LayoutParams(WRAP_CONTENT, 0, 1);
        layoutParamsInstruction.gravity = CENTER_HORIZONTAL;

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.addView(textViewBattleship, layoutParamsBattleship);
        linearLayout.addView(textViewInstruction, layoutParamsInstruction);
        linearLayout.setOrientation(VERTICAL);

        return linearLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt("intMargin", intMargin);

        super.onSaveInstanceState(outState);
    }

    public void setMargin(int margin)
    {
        intMargin = margin;
    }
}