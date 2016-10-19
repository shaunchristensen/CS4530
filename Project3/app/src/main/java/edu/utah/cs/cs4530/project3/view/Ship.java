/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.10
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.view;

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

    protected float getLeft(float width)
    {
        return (intLeft + 1) * width;
    }

    protected Matrix getMatrix(float width, float height)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(intHeading);
        matrix.postScale(width, height);
        matrix.postTranslate(getLeft(width), getTop(height));

        return matrix;
    }

    protected abstract Path getPath(float width, float height);

    protected float getTop(float height)
    {
        return (intTop + 1) * height;
    }
}