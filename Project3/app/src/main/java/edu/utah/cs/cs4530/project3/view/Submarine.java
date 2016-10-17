/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.10
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.view;

import android.content.Context;
import android.graphics.Path;

/**
 * Created by Shaun Christensen on 2016.10.10.
 */
public class Submarine extends Ship
{
    // constructors

    public Submarine(Context context, int row, int column, int heading)
    {
        super(context, row, column, heading, 3);
    }

    // methods

    @Override
    protected Path getPath(float width, float height)
    {
        Path path = new Path();
        path.moveTo(0, -1.4f);
        path.cubicTo(.75f, -1.4f, .25f, 1.4f, 0, 1.4f);
        path.cubicTo(-.25f, 1.4f, -.75f, -1.4f, 0, -1.4f);
        path.close();
        path.transform(getMatrix(width, height));

        return path;
    }
}