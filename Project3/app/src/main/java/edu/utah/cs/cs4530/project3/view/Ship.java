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

/**
 * Created by Shaun Christensen on 2016.10.10.
 */
public abstract class Ship extends View
{
    // fields

    protected final int intColumn;
    protected final int intHeading;
    protected final int intLength;
    protected final int intRow;

    protected Path path;

    // constructors

    public Ship(Context context, int row, int column, int heading, int length)
    {
        super(context);

        intRow = row;
        intColumn = column;
        intHeading = ((heading % 360 + 45) / 90) * 90;
        intLength = length;
    }

    // methods

    protected float getLeft(float width)
    {
        float left = intColumn + .5f;

        if (intHeading == 90)
        {
            left -= (intLength - 1) * .5f;
        }
        else if (intHeading == 270)
        {
            left += (intLength - 1) * .5f;
        }

        return left * width;
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
        float top = intRow + .5f;

        if (intHeading == 0)
        {
            top += (intLength - 1) * .5f;
        }
        else if (intHeading == 180)
        {
            top -= (intLength - 1) * .5f;
        }

        return top * height;
    }
}