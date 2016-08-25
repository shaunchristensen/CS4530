package edu.utah.cs.cs4530.lightswitch;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        View view = new View(this);
        view.setBackgroundColor(Color.BLUE);

        setContentView(view);
    }
}