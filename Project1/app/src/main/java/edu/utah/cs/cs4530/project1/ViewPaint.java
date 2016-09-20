package edu.utah.cs.cs4530.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by Shaun Christensen on 2016.09.20.
 */
public class ViewPaint extends View implements View.OnClickListener
{
    // fields

    private final int intColor;
    private boolean booleanSelected;
    private OnPaintSelectedListener onPaintSelectedListener;
    private Paint paint;
    private RectF rectF;

    // constructors

   public ViewPaint(Context context, int color)
   {
        super(context);

        intColor = color;
        onPaintSelectedListener = null;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
   }

    // interfaces

    public interface OnPaintSelectedListener
    {
        void onPaintSelected(int color);
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

    public void setOnPaintSelectedListener(OnPaintSelectedListener listener)
    {
        onPaintSelectedListener = listener;
    }

    @Override
    public void onClick(View v)
    {
        setSelected(true);
        onPaintSelectedListener.onPaintSelected(intColor);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        rectF.bottom = getHeight() / 10;
        rectF.left = getWidth() / 10;
        rectF.right = getWidth() / 10;
        rectF.top = getHeight() / 10;

        paint.setColor(intColor);
        paint.setStrokeWidth(rectF.width() / 10);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2, paint);

        if (booleanSelected)
        {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);

            canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() * 6 / 10, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);

        int height = getSuggestedMinimumHeight();
        int width = getSuggestedMinimumWidth();

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