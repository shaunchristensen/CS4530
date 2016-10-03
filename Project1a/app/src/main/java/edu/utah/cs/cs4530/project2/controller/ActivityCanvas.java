/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.09.30
 * Assignment: Project 2 - Palette Paint
 */

package edu.utah.cs.cs4530.project2.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;
import java.util.List;

import edu.utah.cs.cs4530.project2.model.Gallery;
import edu.utah.cs.cs4530.project2.model.Painting;
import edu.utah.cs.cs4530.project2.model.Palette;
import edu.utah.cs.cs4530.project2.model.Point;
import edu.utah.cs.cs4530.project2.model.Stroke;
import edu.utah.cs.cs4530.project2.view.LinearLayoutNavigation;
import edu.utah.cs.cs4530.project2.view.ViewCanvas;

import static edu.utah.cs.cs4530.project2.view.LinearLayoutNavigation.*;
import static edu.utah.cs.cs4530.project2.view.ViewCanvas.*;

/**
 * Created by Shaun Christensen on 2016.09.30.
 */
public class ActivityCanvas extends AppCompatActivity implements OnAnimationStartListener, OnAnimationStopListener, OnButtonNextClickListener, OnButtonPlayStopClickListener, OnButtonPreviousClickListener, OnPaintViewClickListener, OnStrokeStartListener, OnStrokeStopListener
{
    // fields

    private Gallery gallery;
    private int intPaintingIndex;
    private LinearLayoutNavigation linearLayoutNavigation;
    private List<Point> listPointsModel;
    private List<PointF> listPointsView;
    private Palette palette;
    private ViewCanvas viewCanvas;

    // methods

    private void enableButtons()
    {
        if (intPaintingIndex < gallery.getPaintingCount())
        {
            linearLayoutNavigation.setButtonNextEnabled(true);
            linearLayoutNavigation.setButtonPlayStopEnabled(true);
        }
        else
        {
            linearLayoutNavigation.setButtonNextEnabled(false);
            linearLayoutNavigation.setButtonPlayStopEnabled(false);
        }


        if (intPaintingIndex > 0)
        {
            linearLayoutNavigation.setButtonPreviousEnabled(true);
        }
        else
        {
            linearLayoutNavigation.setButtonPreviousEnabled(false);
        }
    }

    private void setStrokes()
    {
        if (intPaintingIndex >= 0 && intPaintingIndex < gallery.getPaintingCount())
        {
            viewCanvas.clearStrokes();

            for (Stroke stroke : gallery.getPainting(intPaintingIndex).getStrokes())
            {
                listPointsView = new ArrayList<PointF>();

                for (Point point : stroke.getPoints())
                {
                    listPointsView.add(new PointF(point.getX(), point.getY()));
                }

                viewCanvas.addStroke(stroke.getColor(), listPointsView);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            int color = data.getIntExtra("color", palette.getColor(0));

            linearLayoutNavigation.setViewPaint(color);
            viewCanvas.setColor(color);
        }
    }

    @Override
    public void onAnimationStart()
    {
        linearLayoutNavigation.setAnimationIsStarted(true);
        linearLayoutNavigation.setButtonNextEnabled(false);
        linearLayoutNavigation.setButtonPlayStopBackgroundResource();
        linearLayoutNavigation.setButtonPreviousEnabled(false);
    }

    @Override
    public void onAnimationStop()
    {
        linearLayoutNavigation.setAnimationIsStarted(false);
        linearLayoutNavigation.setButtonNextEnabled(true);
        linearLayoutNavigation.setButtonPlayStopBackgroundResource();
        linearLayoutNavigation.setButtonPreviousEnabled(true);
    }

    @Override
    public void onButtonNextClick()
    {
        intPaintingIndex++;

        enableButtons();
        setStrokes();
    }

    @Override
    public void onButtonPlayStopClick()
    {
        viewCanvas.toggleAnimation();
    }

    @Override
    public void onButtonPreviousClick()
    {
        intPaintingIndex--;

        enableButtons();
        setStrokes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        gallery = Gallery.getGallery();

        palette = new Palette();
        palette.addColor(Color.RED);
        palette.addColor(Color.BLUE);
        palette.addColor(Color.GREEN);
        palette.addColor(Color.YELLOW);
        palette.addColor(Color.rgb(128, 0, 128));

        int padding = (getResources().getDisplayMetrics().heightPixels > getResources().getDisplayMetrics().widthPixels ? getResources().getDisplayMetrics().heightPixels : getResources().getDisplayMetrics().widthPixels) / 100;

        linearLayoutNavigation = new LinearLayoutNavigation(this, palette.getColor(0));
        linearLayoutNavigation.setOnButtonNextClickListener(this);
        linearLayoutNavigation.setOnButtonPlayStopClickListener(this);
        linearLayoutNavigation.setOnButtonPreviousClickListener(this);
        linearLayoutNavigation.setOnPaintViewClickListener(this);
        linearLayoutNavigation.setPadding(0, padding, 0, padding);

        viewCanvas = new ViewCanvas(this, palette.getColor(0), 5000);
        viewCanvas.setOnAnimationStartListener(this);
        viewCanvas.setOnAnimationStopListener(this);
        viewCanvas.setOnStrokeStartListener(this);
        viewCanvas.setOnStrokeStopListener(this);

        enableButtons();
        setStrokes();

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(linearLayoutNavigation, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        linearLayout.addView(viewCanvas, new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        setContentView(linearLayout);
    }

    @Override
    public void onPaintViewClick()
    {
        startActivityForResult(new Intent(this, ActivityPalette.class), 1);
    }

    @Override
    public void onStrokeStart()
    {
        if (intPaintingIndex == gallery.getPaintingCount())
        {
            gallery.addPainting(new Painting(viewCanvas.getWidth() / viewCanvas.getHeight()));

            enableButtons();
        }
    }

    @Override
    public void onStrokeStop(int color, List<PointF> points)
    {
        listPointsModel = new ArrayList<Point>();

        for (PointF pointF : points)
        {
            listPointsModel.add(new Point(pointF.x, pointF.y));
        }

        gallery.getPainting(intPaintingIndex).addStroke(new Stroke(color, listPointsModel.toArray(new Point[listPointsModel.size()])));
    }
}