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

    private int intColumnsCount;
    private OnOKClickListener onOKClickListener;
    private String stringInstruction, stringShot;
    private TextView textViewInstruction, textViewShot;

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
            stringInstruction = savedInstanceState.getString("stringInstruction");
            stringShot = savedInstanceState.getString("stringShot");
        }

        onOKClickListener = getActivity() instanceof OnOKClickListener ? (OnOKClickListener)getActivity() : null;

        int margin = getResources().getDisplayMetrics().heightPixels / 8;

        Button button = new Button(getActivity());
        button.setBackgroundColor(rgb(192, 192, 192));
        button.setOnClickListener(this);
        button.setText("OK");
        button.setTextColor(BLACK);
        button.setTextSize(COMPLEX_UNIT_SP, 25);

        TextView textViewBattleship = new TextView(getActivity());
        textViewBattleship.setText("Battleship");
        textViewBattleship.setTextColor(rgb(132, 132, 130));
        textViewBattleship.setTextSize(COMPLEX_UNIT_SP, 100);
        textViewBattleship.setTypeface(createFromAsset(getActivity().getAssets(), "fonts/ITC Machine Bold.ttf"));

        textViewInstruction = new TextView(getActivity());
        textViewInstruction.setText(stringInstruction);
        textViewInstruction.setTextColor(BLACK);
        textViewInstruction.setTextSize(COMPLEX_UNIT_SP, 25);
        textViewInstruction.setTypeface(null, BOLD);

        textViewShot = new TextView(getActivity());
        textViewShot.setText(stringShot);
        textViewShot.setTextColor(BLACK);
        textViewShot.setTextSize(COMPLEX_UNIT_SP, 25);

        LinearLayout.LayoutParams layoutParamsBattleship = new LinearLayout.LayoutParams(WRAP_CONTENT, 0, 3);
        layoutParamsBattleship.gravity = CENTER_HORIZONTAL;
        layoutParamsBattleship.topMargin = margin;

        LinearLayout.LayoutParams layoutParamsButton = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParamsButton.bottomMargin = margin;
        layoutParamsButton.gravity = CENTER_HORIZONTAL;

        LinearLayout.LayoutParams layoutParamsInstruction = new LinearLayout.LayoutParams(WRAP_CONTENT, 0, 2);
        layoutParamsInstruction.gravity = CENTER;

        LinearLayout.LayoutParams layoutParamsShot = new LinearLayout.LayoutParams(WRAP_CONTENT, 0, 1);
        layoutParamsShot.gravity = CENTER;

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.addView(textViewBattleship, layoutParamsBattleship);
        linearLayout.addView(textViewShot, layoutParamsShot);
        linearLayout.addView(textViewInstruction, layoutParamsInstruction);
        linearLayout.addView(button, layoutParamsButton);
        linearLayout.setOrientation(VERTICAL);

        return linearLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt("intColumnsCount", intColumnsCount);
        outState.putString("stringInstruction", stringInstruction);
        outState.putString("stringShot", stringShot);

        super.onSaveInstanceState(outState);
    }

    public void setColumnsCount(int columnsCount)
    {
        intColumnsCount = columnsCount;
    }

    public void setText(boolean shot, boolean hit, boolean status, int cell, int opponent, int player)
    {
        stringInstruction = "Player " + (status ? (player + 1) + " press OK." : (opponent + 1) + " won!");
        stringShot = shot ? "Player " + (opponent + 1) + ": " + getRowColumnString(cell) + " ‐ " + (hit ? "Hit!" : "Miss.") : "";

        textViewInstruction.setText(stringInstruction);
        textViewInstruction.invalidate();

        textViewShot.setText(stringShot);
        textViewShot.invalidate();
    }
}