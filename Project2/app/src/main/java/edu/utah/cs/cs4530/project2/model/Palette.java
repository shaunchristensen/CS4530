/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class Palette implements Serializable
{
    // fields

    private float floatThetaBlue;
    private float floatThetaGreen;
    private float floatThetaRed;
    private int intColorIndex;
    private final List<Integer> listColors;
    private static Palette palette;

    // constructors

    private Palette()
    {
        listColors = new ArrayList<Integer>();
    }

    // methods

    public void addColor(int color)
    {
        listColors.add(color);
    }

    public float getAngleBlue()
    {
        return floatThetaBlue;
    }

    public float getAngleGreen()
    {
        return floatThetaGreen;
    }

    public float getAngleRed()
    {
        return floatThetaRed;
    }

    public int getColor(int index)
    {
        return listColors.get(index);
    }

    public int getColorCount()
    {
        return listColors.size();
    }

    public int getColorIndex()
    {
        return intColorIndex;
    }

    public List<Integer> getColors()
    {
        return listColors;
    }

    public static Palette getPalette()
    {
        if (palette == null)
        {
            palette = new Palette();
        }

        return palette;
    }

    public void removeColor(int index)
    {
        listColors.remove(index);
    }

    public void setAngleBlue(float theta)
    {
        floatThetaBlue = theta;
    }

    public void setAngleGreen(float theta)
    {
        floatThetaGreen = theta;
    }

    public void setAngleRed(float theta)
    {
        floatThetaRed = theta;
    }

    public void setColorIndex(int index)
    {
        intColorIndex = index;
    }
}