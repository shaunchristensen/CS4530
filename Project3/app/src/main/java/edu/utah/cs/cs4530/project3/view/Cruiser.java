/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.10
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.view;

import android.content.Context;
import android.graphics.Path;

public class Cruiser extends Ship
{
    // constructors

    public Cruiser(Context context, int length, int heading, int left, int top)
    {
        super(context, length, heading, left, top);
    }

    // methods

    @Override
    protected Path getPath(float width, float height)
    {
        Path path = new Path();
        path.moveTo(-.25f, .4666f * intLength);
        path.quadTo(-.69f, 0 * intLength, 0, -.4666f * intLength);
        path.quadTo(.69f, 0 * intLength, .25f, .4666f * intLength);
        path.close();
        path.transform(getMatrix(width, height));

        return path;
    }
}