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
public class Cruiser extends Ship
{
    // constructors

    public Cruiser(Context context, int row, int column, int heading)
    {
        super(context, row, column, heading, 3);
    }

    // methods

    @Override
    protected Path getPath(float width, float height)
    {
        Path path = new Path();
        path.moveTo(-.25f, 1.4f);
        path.quadTo(-.69f, 0, 0, -1.4f);
        path.quadTo(.69f, 0, .25f, 1.4f);
        path.close();
        path.transform(getMatrix(width, height));

        return path;
    }
}