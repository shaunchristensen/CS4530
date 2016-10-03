/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */

public class Palette
{
    // fields

    private final List<Integer> listColors;

    // constructors

    public Palette()
    {
        listColors = new ArrayList<Integer>();
    }

    // methods

    public void addColor(int color)
    {
        listColors.add(color);
    }

    public int getColor(int index)
    {
        return listColors.get(index);
    }

    public List<Integer> getColors()
    {
        return listColors;
    }

    public void removeColor(int index)
    {
        listColors.remove(index);
    }
}