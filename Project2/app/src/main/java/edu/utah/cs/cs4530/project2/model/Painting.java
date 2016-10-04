/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class Painting implements Serializable
{
    // fields

    private final List<Stroke> listStrokes;

    // constructors

    public Painting()
    {
        listStrokes = new ArrayList<Stroke>();
    }

    // methods

    public void addStroke(Stroke stroke)
    {
        listStrokes.add(stroke);
    }

    public List<Stroke> getStrokes()
    {
        return listStrokes;
    }
}