/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.view.ship;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Path;
import android.view.View;

public abstract class Ship extends View
{
    // fields

    protected final int intHeading;
    protected final int intLeft;
    protected final int intLength;
    protected final int intTop;
    protected Path path;

    // constructors

    public Ship(Context context, int length, int heading, int left, int top)
    {
        super(context);

        intHeading = heading;
        intLeft = left;
        intLength = length;
        intTop = top;
    }

    // methods

    protected float getLeft(float length, float margin)
    {
        return (intLeft + (intHeading == 90 || intHeading == 270 ? intLength / 2f : .5f)) * length - margin / 2;
    }

    protected float getTop(float length, float margin)
    {
        return (intTop + (intHeading == 0 || intHeading == 180 ? intLength / 2f : .5f)) * length - margin / 2;
    }

    protected Matrix getMatrix(float length, float margin)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(intHeading);
        matrix.postScale(length, length);
        matrix.postTranslate(getLeft(length, margin), getTop(length, margin));

        return matrix;
    }

    public abstract Path getPath(float length, float margin);
}