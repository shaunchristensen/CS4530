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

    protected final float floatLeft;
    protected final float floatTop;
    protected final int intHeading;
    protected final int intLength;
    protected Path path;

    // constructors

    public Ship(Context context, int length, int heading, float left, float top)
    {
        super(context);

        floatLeft = left;
        floatTop = top;
        intHeading = heading;
        intLength = length;
    }

    // methods

    protected float getLeft(float length, float margin)
    {
        return floatLeft * length - margin / 2;
    }

    protected Matrix getMatrix(float length, float margin)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(intHeading);
        matrix.postScale(length, length);
        matrix.postTranslate(getLeft(length, margin), getTop(length, margin));

        return matrix;
    }

    protected abstract Path getPath(float length, float margin);

    protected float getTop(float length, float margin)
    {
        return floatTop * length - margin / 2;
    }
}