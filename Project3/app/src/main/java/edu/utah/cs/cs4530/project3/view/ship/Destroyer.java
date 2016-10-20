/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.10
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.view.ship;

import android.content.Context;
import android.graphics.Path;

/**
 * Created by Shaun Christensen on 2016.10.10.
 */
public class Destroyer extends Ship
{
    // constructors

    public Destroyer(Context context, int length, int heading, int left, int top)
    {
        super(context, length, heading, left, top);
    }

    // methods

    @Override
    public Path getPath(float length, float margin)
    {
        Path path = new Path();
        path.moveTo(-.25f, .45f * intLength);
        path.quadTo(-.69f, 0, 0, -.45f * intLength);
        path.quadTo(.69f, 0, .25f, .45f * intLength);
        path.close();
        path.transform(getMatrix(length, margin));

        return path;
    }
}