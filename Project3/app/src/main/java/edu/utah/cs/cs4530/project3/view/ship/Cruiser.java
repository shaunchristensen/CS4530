/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.10
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.view.ship;

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
    public Path getPath(float length, float margin)
    {
        Path path = new Path();
        path.moveTo(-.25f, 1.4f / 3 * intLength);
        path.quadTo(-.69f, 0, 0, -1.4f / 3 * intLength);
        path.quadTo(.69f, 0, .25f, 1.4f / 3 * intLength);
        path.close();
        path.transform(getMatrix(length, margin));

        return path;
    }
}