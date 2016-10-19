package edu.utah.cs.cs4530.project3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.LinearLayout;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.rgb;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;

/**
 * Created by Shaun on 2016.10.13.
 */

public class LinearLayoutTemp extends LinearLayout
{
    Paint paint;
    Ship shipCarrier;
    Ship shipBattleship;
    Ship shipCruiser;
    Ship shipSubmarine;
    Ship shipDestroyer;

    public LinearLayoutTemp(Context context)
    {
        super(context);

        paint = new Paint(ANTI_ALIAS_FLAG);

        shipCarrier = new Carrier(context, 5, 0, 0, 0);
        shipBattleship = new Battleship(context, 4, 0, 9, 90);
        shipCruiser = new Cruiser(context, 3, 9, 9, 180);
        shipSubmarine = new Submarine(context, 3, 9, 0, 270);
        shipDestroyer = new Destroyer(context, 2, 5, 5, 90);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        float width = (getHeight() < getWidth() ? getHeight() : getWidth()) / 10;

        paint.setColor(rgb(0, 0, 0));
        paint.setStrokeWidth(width / 50);
        paint.setStyle(FILL);

        canvas.drawLine(width * 1, 0, width * 1, getHeight(), paint);
        canvas.drawLine(width * 2, 0, width * 2, getHeight(), paint);
        canvas.drawLine(width * 3, 0, width * 3, getHeight(), paint);
        canvas.drawLine(width * 4, 0, width * 4, getHeight(), paint);
        canvas.drawLine(width * 5, 0, width * 5, getHeight(), paint);
        canvas.drawLine(width * 6, 0, width * 6, getHeight(), paint);
        canvas.drawLine(width * 7, 0, width * 7, getHeight(), paint);
        canvas.drawLine(width * 8, 0, width * 8, getHeight(), paint);
        canvas.drawLine(width * 9, 0, width * 9, getHeight(), paint);
        canvas.drawLine(width * 10, 0, width * 10, getHeight(), paint);

        canvas.drawLine(0, width * 1, getWidth(), width * 1, paint);
        canvas.drawLine(0, width * 2, getWidth(), width * 2, paint);
        canvas.drawLine(0, width * 3, getWidth(), width * 3, paint);
        canvas.drawLine(0, width * 4, getWidth(), width * 4, paint);
        canvas.drawLine(0, width * 5, getWidth(), width * 5, paint);
        canvas.drawLine(0, width * 6, getWidth(), width * 6, paint);
        canvas.drawLine(0, width * 7, getWidth(), width * 7, paint);
        canvas.drawLine(0, width * 8, getWidth(), width * 8, paint);
        canvas.drawLine(0, width * 9, getWidth(), width * 9, paint);
        canvas.drawLine(0, width * 10, getWidth(), width * 10, paint);

        paint.setColor(rgb(132, 132, 130));

        canvas.drawPath(shipCarrier.getPath(width, width), paint);
        canvas.drawPath(shipBattleship.getPath(width, width), paint);
        canvas.drawPath(shipCruiser.getPath(width, width), paint);
        canvas.drawPath(shipSubmarine.getPath(width, width), paint);
        canvas.drawPath(shipDestroyer.getPath(width, width), paint);

        paint.setColor(BLACK);
        paint.setStyle(STROKE);

        canvas.drawPath(shipCarrier.getPath(width, width), paint);
        canvas.drawPath(shipBattleship.getPath(width, width), paint);
        canvas.drawPath(shipCruiser.getPath(width, width), paint);
        canvas.drawPath(shipSubmarine.getPath(width, width), paint);
        canvas.drawPath(shipDestroyer.getPath(width, width), paint);

        paint.setStyle(FILL);

        for (int i = 0; i < 100; i++)
        {
            if (i == 0 || i == 7 || i == 9 || i == 54 || i == 9 || i == 90 || i == 91 || i == 99)
            {
                paint.setColor(RED);
            }
            else if (i == 14 || i == 21 || i == 28 || i == 35 || i == 42 || i == 49 || i == 56 || i == 63 || i == 70 || i == 77 || i == 84 || i == 99)
            {
                paint.setColor(WHITE);
            }
            else
            {
                paint.setColor(BLACK);
            }

            canvas.drawCircle(i % 10 * width + width / 2, i / 10 * width + width / 2, width / 10, paint);
        }
    }
}
