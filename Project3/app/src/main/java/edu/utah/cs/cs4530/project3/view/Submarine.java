/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.10
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.view;

import android.content.Context;
import android.graphics.Path;

public class Submarine extends Ship
{
    // constructors

    public Submarine(Context context, int length, int heading, float left, float top)
    {
        super(context, length, heading, left, top);
    }

    // methods

    @Override
    protected Path getPath(float length, float margin)
    {
        Path path = new Path();
        path.moveTo(0, -.4666f * intLength);
        path.cubicTo(.75f, -.4666f * intLength, .25f, .4666f * intLength, 0, .4666f * intLength);
        path.cubicTo(-.25f, .4666f * intLength, -.75f, -.4666f * intLength, 0, -.4666f * intLength);
        path.close();
        path.transform(getMatrix(length, margin));

        return path;
    }
}