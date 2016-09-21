package edu.utah.cs.cs4530.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Shaun Christensen on 2016.09.21.
 */
public class ViewGroupPalette extends ViewGroup implements View.OnClickListener
{
    // fields

    private final ArrayList<ViewPaint> arrayList;
    private OnColorChangeListener onColorChangeListener;

    // constructors

    public ViewGroupPalette(Context context)
    {
        super(context);

        arrayList = new ArrayList<ViewPaint>();
        arrayList.add(new ViewPaint(context, Color.RED));
        arrayList.add(new ViewPaint(context, Color.BLUE));
        arrayList.add(new ViewPaint(context, Color.GREEN));
        arrayList.add(new ViewPaint(context, Color.YELLOW));
        arrayList.add(new ViewPaint(context, Color.argb(255, 128, 0, 128)));

        for (ViewPaint v: arrayList)
        {
            this.addView(v);

            v.setOnClickListener(this);
        }

        ((ViewPaint)arrayList.get(0)).performClick();

        onColorChangeListener = null;

        setWillNotDraw(false);
    }

    // interfaces

    public interface OnColorChangeListener
    {
        void onColorChange(int color);
    }

    // methods

    @Override
    public void onClick(View view)
    {
        if (view instanceof ViewPaint)
        {
            for (int i = 0; i < arrayList.size(); i++)
            {
                ((ViewPaint)arrayList.get(i)).setActive(false);
            }

            ((ViewPaint)view).setActive(true);

            onColorChangeListener.onColorChange(((ViewPaint)view).getColor());
        }
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.argb(255, 222, 184, 135));
        paint.setStyle(Paint.Style.FILL);

        RectF rectF = new RectF();
        rectF.bottom = getHeight() * .9f;
        rectF.left = getWidth() * .1f;
        rectF.right = getWidth() * .9f;
        rectF.top = getHeight() * .1f;

        canvas.drawOval(rectF, paint);

        paint.setColor(Color.argb(255, 139, 115, 85));
        paint.setStrokeWidth(rectF.width() * .01f);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawOval(rectF, paint);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3)
    {
        int radius = (getHeight() < getWidth() ? getHeight() : getWidth()) / 20;

        float theta = (float)(2 * Math.PI / getChildCount());

        PointF pointF;
        Rect rect;
        View view;

        for (int index = 0; index < getChildCount(); index++)
        {
            pointF = new PointF();
            pointF.x = getWidth() * .5f - (getWidth() * .375f - radius) * (float)Math.cos(index * theta);
            pointF.y = getHeight() * .5f - (getHeight() * .375f - radius) * (float)Math.sin(index * theta);

            rect = new Rect();
            rect.bottom = (int)(pointF.y + radius);
            rect.left = (int)(pointF.x - radius);
            rect.right = (int)(pointF.x + radius);
            rect.top = (int)(pointF.y - radius);

            view = getChildAt(index);
            view.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    public void setOnColorChangeListener(OnColorChangeListener listener)
    {
        onColorChangeListener = listener;
    }

}