/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.10
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.view.ship;

import android.content.Context;
import android.graphics.Path;

public class Battleship extends Ship
{
    // constructors

    public Battleship(Context context, int length, int heading, int left, int top)
    {
        super(context, length, heading, left, top);
    }

    // methods

    @Override
    public Path getPath(float length, float margin)
    {
        Path path = new Path();
        path.moveTo(0, -.475f * intLength);
        path.cubicTo(.56f, -.2125f * intLength, .5f, .475f * intLength, 0, .475f * intLength);
        path.cubicTo(-.5f, .475f * intLength, -.56f, -.2125f * intLength, 0, -.475f * intLength);
        path.close();
        path.transform(getMatrix(length, margin));

        return path;
    }
}