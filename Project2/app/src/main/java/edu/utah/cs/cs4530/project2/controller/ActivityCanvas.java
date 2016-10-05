/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.controller;

import android.content.Intent;
import android.content.res.Configuration;
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

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;
import static android.graphics.Color.rgb;
import static android.view.ViewTreeObserver.*;
import static android.widget.LinearLayout.*;
import static android.widget.LinearLayout.LayoutParams.*;
import static edu.utah.cs.cs4530.project2.view.LinearLayoutNavigation.*;
import static edu.utah.cs.cs4530.project2.view.ViewCanvas.*;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class ActivityCanvas extends AppCompatActivity implements OnAnimationToggleListener, OnButtonNextClickListener, OnButtonPlayStopClickListener, OnButtonPreviousClickListener, OnGlobalLayoutListener, OnViewCanvasTouchListener, OnViewPaintClickListener
{
    // fields

    private Gallery gallery;
    private int intDuration;
    private int intRequestCode;
    private float floatHeight;
    private float floatWidth;
    private LinearLayout linearLayoutCanvas;
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
                    listPointsView.add(new PointF(point.getX() * floatWidth, point.getY() * floatHeight));
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
        linearLayoutNavigation.setAnimationIsStarted(animationIsStarted);
        linearLayoutNavigation.setButtonPlayStopBackgroundResource();

        if (animationIsStarted)
        {
            linearLayoutNavigation.setButtonNextEnabled(false);
            linearLayoutNavigation.setButtonPreviousEnabled(false);
        }
        else
        {
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
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        viewCanvas.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        for (File file : getFilesDir().listFiles())
        {
            if (!file.getName().startsWith("instant"))
            {
                Log.i("File", file.getName());
//                file.delete();
            }
        }

        try
        {
            FileInputStream fileInputStream = getApplicationContext().openFileInput("gallery");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            gallery = (Gallery) objectInputStream.readObject();
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
            FileInputStream fileInputStream = getApplicationContext().openFileInput("palette");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            palette = (Palette) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }
        catch (Exception e)
        {
            palette = Palette.getPalette();
            palette.addColor(RED);
            palette.addColor(BLUE);
            palette.addColor(GREEN);
            palette.addColor(YELLOW);
            palette.addColor(rgb(128, 0, 128));

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

        viewCanvas = new ViewCanvas(this, palette.getColor(palette.getColorIndex()), intDuration);
        viewCanvas.getViewTreeObserver().addOnGlobalLayoutListener(this);
        viewCanvas.setBackgroundColor(WHITE);
        viewCanvas.setOnAnimationToggleListener(this);
        viewCanvas.setOnViewCanvasTouchListener(this);

        linearLayoutCanvas = new LinearLayout(this);
        linearLayoutCanvas.addView(viewCanvas);
        linearLayoutCanvas.setBackgroundColor(rgb(128, 128, 128));

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(linearLayoutNavigation, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        linearLayout.addView(linearLayoutCanvas, new LayoutParams(MATCH_PARENT, 0, 1));
        linearLayout.setOrientation(VERTICAL);

        setButtonsEnabled();
        setContentView(linearLayout);
    }

    @Override
    public void onGlobalLayout()
    {
        if (floatHeight != viewCanvas.getHeight() || floatWidth != viewCanvas.getWidth())
        {
            floatHeight = viewCanvas.getHeight();
            floatWidth = viewCanvas.getWidth();

            if (gallery.getPaintingCount() > 0)
            {
                int padding;

                if (floatHeight / gallery.getPainting(gallery.getPaintingIndex()).getHeight() < floatWidth / gallery.getPainting(gallery.getPaintingIndex()).getWidth())
                {
                    padding = (int)((floatWidth - gallery.getPainting(gallery.getPaintingIndex()).getWidth() * floatHeight / gallery.getPainting(gallery.getPaintingIndex()).getHeight()) / 2);

                    linearLayoutCanvas.setPadding(padding, 0, padding, 0);
                }
                else
                {
                    padding = (int)((floatHeight - gallery.getPainting(gallery.getPaintingIndex()).getHeight() * floatWidth / gallery.getPainting(gallery.getPaintingIndex()).getWidth()) / 2);

                    linearLayoutCanvas.setPadding(0, padding, 0, padding);
                }
            }
            else
            {
                linearLayoutCanvas.setPadding(0, 0, 0, 0);
            }

            viewCanvas.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            viewCanvas.setStrokes(getStrokes());
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), "gallery"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(gallery);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch(Exception e)
        {
            Log.e("ActivityCanvas.onStop", "Error: Unable to open the gallery. " + e.getMessage());
        }

        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), "palette"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(palette);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch(Exception e)
        {
            Log.e("ActivityCanvas.onStop", "Error: Unable to write the palette. " + e.getMessage());
        }
    }

    @Override
    public void onViewCanvasTouch(int height, int width)
    {
        if (gallery.getPaintingIndex() == gallery.getPaintingCount())
        {
            gallery.addPainting(new Painting(height, width));

            setButtonsEnabled();
        }
    }

    @Override
    public void onViewCanvasTouch(int color, List<PointF> points)
    {
        listPointsModel = new ArrayList<Point>();

        for (PointF pointF : points)
        {
            listPointsModel.add(new Point(pointF.x / floatWidth, pointF.y / floatHeight));
        }

        gallery.getPainting(gallery.getPaintingIndex()).addStroke(new Stroke(color, listPointsModel.toArray(new Point[listPointsModel.size()])));
    }

    @Override
    public void onViewPaintClick()
    {
        startActivityForResult(new Intent(this, ActivityPalette.class), intRequestCode);
    }
}