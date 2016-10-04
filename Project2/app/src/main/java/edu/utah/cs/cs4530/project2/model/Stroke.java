/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.model;

import java.io.Serializable;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class Stroke implements Serializable
{
    // fields

    private final int intColor;
    private final Point[] arrayPoints;

    // constructors

    public Stroke(int color, Point[] points)
    {
        intColor = color;
        arrayPoints = points;
    }

    // methods

    public int getColor()
    {
        return intColor;
    }

    public Point[] getPoints()
    {
        return arrayPoints;
    }
}