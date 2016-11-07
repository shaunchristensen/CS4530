/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.view.ship;

import android.content.Context;
import android.graphics.Path;

public class Submarine extends Ship
{
    // constructors

    public Submarine(Context context, int length, int heading, int left, int top)
    {
        super(context, length, heading, left, top);
    }

    // methods

    @Override
    public Path getPath(float length, float margin)
    {
        Path path = new Path();
        path.moveTo(0, -1.4f / 3 * intLength);
        path.cubicTo(.75f, -1.4f / 3 * intLength, .25f, 1.4f / 3 * intLength, 0, 1.4f / 3 * intLength);
        path.cubicTo(-.25f, 1.4f / 3 * intLength, -.75f, -1.4f / 3 * intLength, 0, -1.4f / 3 * intLength);
        path.close();
        path.transform(getMatrix(length, margin));

        return path;
    }
}