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
public class Point implements Serializable
{
    // fields

    private final float floatX;
    private final float floatY;

    // constructors

    public Point(float x, float y)
    {
        floatX = x;
        floatY = y;
    }

    // methods

    public float getX()
    {
        return floatX;
    }

    public float getY()
    {
        return floatY;
    }
}