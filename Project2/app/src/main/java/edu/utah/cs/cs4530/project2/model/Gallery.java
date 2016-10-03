/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */

public class Gallery
{
    // fields

    private static Gallery gallery;
    private final List<Painting> listPaintings;

    // constructors

    private Gallery()
    {
        listPaintings = new ArrayList<Painting>();
    }

    // methods

    public void addPainting(Painting painting)
    {
        listPaintings.add(painting);
    }

    public static Gallery getGallery()
    {
        if (gallery == null)
        {
            gallery = new Gallery();
        }

        return gallery;
    }

    public Painting getPainting(int index)
    {
        return listPaintings.get(index);
    }

    public int getPaintingCount()
    {
        return listPaintings.size();
    }
}