package edu.utah.cs.cs4530.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

/**
 * Created by Shaun Christensen on 2016.09.20.
 */
public class ViewPaint extends View
{
    // fields

    private final int intColor;
    private boolean booleanSelected;
    private Paint paint;
    private RectF rectF;

    // constructors

   public ViewPaint(Context context, int color)
   {
        super(context);

        booleanSelected = true;
        intColor = color;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
   }

    // methods

    public boolean getSelected()
    {
        return booleanSelected;
    }

    public void setSelected(boolean selected)
    {
        if (booleanSelected != selected)
        {
            booleanSelected = selected;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        rectF.bottom = getHeight() * .8f;
        rectF.left = getWidth() * .2f;
        rectF.right = getWidth() * .8f;
        rectF.top = getHeight() * .2f;

        paint.setColor(intColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2, paint);

        if (booleanSelected)
        {
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(rectF.width() * .1f);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() * .6f, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getSuggestedMinimumHeight();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
        int width = getSuggestedMinimumWidth();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSpec;
            width = height;
        }

        if (widthMode == MeasureSpec.EXACTLY)
        {
            height = width;
            width = widthSpec;
        }

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }
}