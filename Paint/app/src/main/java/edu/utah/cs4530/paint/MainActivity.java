package edu.utah.cs4530.paint;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Shaun Christensen on 2016.09.19.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        CircleLayout paletteLayout = new CircleLayout(this);
        setContentView(paletteLayout);

        SplotchView redView = new SplotchView(this);
        redView.setSplotchColor(Color.RED);
        paletteLayout.addView(redView);

        SplotchView greenView = new SplotchView(this);
        greenView.setSplotchColor(Color.GREEN);
        paletteLayout.addView(greenView);

        SplotchView blueView = new SplotchView(this);
        blueView.setSplotchColor(Color.BLUE);
        paletteLayout.addView(blueView);

        SplotchView yellowView = new SplotchView(this);
        yellowView.setSplotchColor(Color.YELLOW);
        paletteLayout.addView(yellowView);

        SplotchView magentaView = new SplotchView(this);
        magentaView.setSplotchColor(Color.MAGENTA);
        paletteLayout.addView(magentaView);

        SplotchView cyanView = new SplotchView(this);
        cyanView.setSplotchColor(Color.CYAN);
        paletteLayout.addView(cyanView);
    }
}