/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.controller;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import edu.utah.cs.cs4530.project2.model.Gallery;
import edu.utah.cs.cs4530.project2.model.Painting;
import edu.utah.cs.cs4530.project2.model.Palette;
import edu.utah.cs.cs4530.project2.model.Point;
import edu.utah.cs.cs4530.project2.model.Stroke;
import edu.utah.cs.cs4530.project2.view.LinearLayoutNavigation;
import edu.utah.cs.cs4530.project2.view.ViewCanvas;

import static android.widget.LinearLayout.*;
import static android.widget.LinearLayout.LayoutParams.*;
import static edu.utah.cs.cs4530.project2.view.LinearLayoutNavigation.*;
import static edu.utah.cs.cs4530.project2.view.ViewCanvas.*;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class ActivityCanvas extends AppCompatActivity implements OnAnimationToggleListener, OnButtonNextClickListener, OnButtonPlayStopClickListener, OnButtonPreviousClickListener, OnViewCanvasTouchListener, OnViewPaintClickListener
{
    // fields

    private Gallery gallery;
    private int intDuration;
    private int intRequestCode;
    private LinearLayoutNavigation linearLayoutNavigation;
    private List<Pair<Integer, List<PointF>>> listStrokes;
    private List<Point> listPointsModel;
    private List<PointF> listPointsView;
    private Palette palette;
    private ViewCanvas viewCanvas;

    // methods

    private List<Pair<Integer, List<PointF>>> getStrokes()
    {
        listStrokes = new ArrayList<Pair<Integer, List<PointF>>>();

        if (gallery.getPaintingIndex() >= 0 && gallery.getPaintingIndex() < gallery.getPaintingCount())
        {
            for (Stroke stroke : gallery.getPainting(gallery.getPaintingIndex()).getStrokes())
            {
                listPointsView = new ArrayList<PointF>();

                for (Point point : stroke.getPoints())
                {
                    listPointsView.add(new PointF(point.getX(), point.getY()));
                }

                listStrokes.add(new Pair<Integer, List<PointF>>(stroke.getColor(), listPointsView));
            }
        }

        return listStrokes;
    }

    private void setButtonsEnabled()
    {
        if (gallery.getPaintingIndex() > 0)
        {
            linearLayoutNavigation.setButtonPreviousEnabled(true);
        }
        else
        {
            linearLayoutNavigation.setButtonPreviousEnabled(false);
        }

        if (gallery.getPaintingIndex() < gallery.getPaintingCount())
        {
            linearLayoutNavigation.setButtonNextEnabled(true);
            linearLayoutNavigation.setButtonPlayStopEnabled(true);
        }
        else
        {
            linearLayoutNavigation.setButtonNextEnabled(false);
            linearLayoutNavigation.setButtonPlayStopEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == intRequestCode)
        {
            linearLayoutNavigation.setViewPaint(palette.getColor(palette.getColorIndex()));
            viewCanvas.setColor(palette.getColor(palette.getColorIndex()));
        }
    }

    @Override
    public void onAnimationToggle(boolean animationIsStarted)
    {
        if (animationIsStarted)
        {
            linearLayoutNavigation.setAnimationIsStarted(true);
            linearLayoutNavigation.setButtonNextEnabled(false);
            linearLayoutNavigation.setButtonPlayStopBackgroundResource();
            linearLayoutNavigation.setButtonPreviousEnabled(false);
        }
        else
        {
            linearLayoutNavigation.setAnimationIsStarted(false);
            linearLayoutNavigation.setButtonPlayStopBackgroundResource();

            setButtonsEnabled();
        }
    }

    @Override
    public void onButtonNextClick()
    {
        gallery.setPaintingIndex(gallery.getPaintingIndex() + 1);
        viewCanvas.setStrokes(getStrokes());

        setButtonsEnabled();
    }

    @Override
    public void onButtonPlayStopClick()
    {
        viewCanvas.toggleAnimation();
    }

    @Override
    public void onButtonPreviousClick()
    {
        gallery.setPaintingIndex(gallery.getPaintingIndex() - 1);
        viewCanvas.setStrokes(getStrokes());

        setButtonsEnabled();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            FileInputStream fileInputStream = getApplicationContext().openFileInput("gallery.dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            gallery = (Gallery)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }
        catch (Exception e)
        {
            gallery = Gallery.getGallery();

            Log.e("ActivityCanvas.onCreate", "Error: Unable to read the gallery. " + e.getMessage());
        }

        try
        {
            FileInputStream fileInputStream = getApplicationContext().openFileInput("palette.dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            palette = (Palette)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }
        catch (Exception e)
        {
            palette = Palette.getPalette();
            palette.addColor(Color.RED);
            palette.addColor(Color.BLUE);
            palette.addColor(Color.GREEN);
            palette.addColor(Color.YELLOW);
            palette.addColor(Color.rgb(128, 0, 128));

            Log.e("ActivityCanvas.onCreate", "Error: Unable to read the palette. " + e.getMessage());
        }

        intDuration = 5000;
        intRequestCode = 4530;

        int padding = (getResources().getDisplayMetrics().heightPixels > getResources().getDisplayMetrics().widthPixels ? getResources().getDisplayMetrics().heightPixels : getResources().getDisplayMetrics().widthPixels) / 100;

        linearLayoutNavigation = new LinearLayoutNavigation(this, palette.getColor(palette.getColorIndex()));
        linearLayoutNavigation.setOnButtonNextClickListener(this);
        linearLayoutNavigation.setOnButtonPlayStopClickListener(this);
        linearLayoutNavigation.setOnButtonPreviousClickListener(this);
        linearLayoutNavigation.setOnViewPaintClickListener(this);
        linearLayoutNavigation.setPadding(0, padding, 0, padding);

        setButtonsEnabled();

        viewCanvas = new ViewCanvas(this, palette.getColor(palette.getColorIndex()), getStrokes(), intDuration);
        viewCanvas.setOnAnimationToggleListener(this);
        viewCanvas.setOnViewCanvasTouchListener(this);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(linearLayoutNavigation, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        linearLayout.addView(viewCanvas, new LayoutParams(MATCH_PARENT, 0, 1));
        linearLayout.setOrientation(VERTICAL);

        setContentView(linearLayout);
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        try
        {
            File file = new File(getFilesDir(), "gallery.dat");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(gallery);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch(Exception e)
        {
            Log.e("ActivityCanvas.onStop", "Error: Unable to write the gallery." + e.getMessage());
        }

        try
        {
            File file = new File(getFilesDir(), "palette.dat");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(palette);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch(Exception e)
        {
            Log.e("ActivityCanvas.onStop", "Error: Unable to write the palette." + e.getMessage());
        }
    }

    @Override
    public void onViewCanvasTouch()
    {
        if (gallery.getPaintingIndex() == gallery.getPaintingCount())
        {
            gallery.addPainting(new Painting());

            setButtonsEnabled();
        }
    }

    @Override
    public void onViewCanvasTouch(int color, List<PointF> points)
    {
        listPointsModel = new ArrayList<Point>();

        for (PointF pointF : points)
        {
            listPointsModel.add(new Point(pointF.x, pointF.y));
        }

        gallery.getPainting(gallery.getPaintingIndex()).addStroke(new Stroke(color, listPointsModel.toArray(new Point[listPointsModel.size()])));
    }

    @Override
    public void onViewPaintClick()
    {
        startActivityForResult(new Intent(this, ActivityPalette.class), intRequestCode);
    }
}