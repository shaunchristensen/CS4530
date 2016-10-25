/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.21
 * Assignment: Project 3 - MVC Battleship
 */

// ITC Machine Bold font credit http://www.onlinewebfonts.com

package edu.utah.cs.cs4530.project3.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import static android.graphics.Color.*;
import static android.graphics.Typeface.*;
import static android.util.TypedValue.*;
import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.view.View.*;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

public class FragmentStart extends Fragment implements OnClickListener
{
    // fields

    private OnStartClickListener onStartClickListener;

    // interfaces

    public interface OnStartClickListener
    {
        void onStartClick(Fragment fragment);
    }

    // methods

    @Override
    public void onClick(View v)
    {
        if (v instanceof Button)
        {
            onStartClickListener.onStartClick(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        onStartClickListener = (OnStartClickListener)getActivity();

        int margin = getResources().getDisplayMetrics().heightPixels / 8;

        Button button = new Button(getActivity());
        button.setOnClickListener(this);
        button.setText("Start");
        button.setTextColor(BLACK);
        button.setTextSize(COMPLEX_UNIT_SP, 25);

        TextView textViewBattleship = new TextView(getActivity());
        textViewBattleship.setText("Battleship");
        textViewBattleship.setTextColor(rgb(132, 132, 130));
        textViewBattleship.setTextSize(COMPLEX_UNIT_SP, 100);
        textViewBattleship.setTypeface(createFromAsset(getActivity().getAssets(), "fonts/ITC Machine Bold.ttf"));

        TextView textViewInstruction = new TextView(getActivity());
        textViewInstruction.setText("Press Start.");
        textViewInstruction.setTextColor(BLACK);
        textViewInstruction.setTextSize(COMPLEX_UNIT_SP, 50);
        textViewInstruction.setTypeface(null, BOLD);

        LayoutParams layoutParamsBattleship = new LayoutParams(WRAP_CONTENT, 0, 5);
        layoutParamsBattleship.gravity = CENTER_HORIZONTAL;
        layoutParamsBattleship.topMargin = margin;

        LayoutParams layoutParamsButton = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParamsButton.bottomMargin = margin;
        layoutParamsButton.gravity = CENTER_HORIZONTAL;

        LayoutParams layoutParamsInstruction = new LayoutParams(WRAP_CONTENT, 0, 3);
        layoutParamsInstruction.gravity = CENTER_HORIZONTAL;

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.addView(textViewBattleship, layoutParamsBattleship);
        linearLayout.addView(textViewInstruction, layoutParamsInstruction);
        linearLayout.addView(button, layoutParamsButton);
        linearLayout.setOrientation(VERTICAL);

        return linearLayout;
    }
}