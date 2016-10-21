/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.10
 * Assignment: Project 3 - MVC Battleship
 */

// ITC Machine Bold font http://www.onlinewebfonts.com
package edu.utah.cs.cs4530.project3.controller;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.utah.cs.cs4530.project3.model.Battleship;
import edu.utah.cs.cs4530.project3.view.Cell;
import edu.utah.cs.cs4530.project3.view.LinearLayoutBattleship;
import edu.utah.cs.cs4530.project3.view.ship.Carrier;
import edu.utah.cs.cs4530.project3.view.ship.Cruiser;
import edu.utah.cs.cs4530.project3.view.ship.Destroyer;
import edu.utah.cs.cs4530.project3.view.ship.Ship;
import edu.utah.cs.cs4530.project3.view.ship.Submarine;

public class ActivityMain extends AppCompatActivity implements LinearLayoutBattleship.OnShootListener
{
    // fields

    Battleship battleship;
    LinearLayoutBattleship linearLayoutBattleship;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        battleship = Battleship.getBattleship();
        battleship.startGame();

        TextView textView = new TextView(this);
        textView.setTextColor(Color.rgb(132, 132, 130));
        textView.setText("Battleship");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ITC Machine Bold.ttf"));

        linearLayoutBattleship = new LinearLayoutBattleship(this, battleship.getRows(), battleship.getColumns(), battleship.getPlayers(), this);
        linearLayoutBattleship.loadGame(battleship.getStatus(), battleship.getOpponent(), battleship.getPlayer(), getShips(), battleship.getHits(), battleship.getMisses());

        setContentView(linearLayoutBattleship);
    }

    private List<List<Ship>> getShips()
    {
        List<List<Ship>> ships = new ArrayList<>();

        for (int i : battleship.getPlayers())
        {
            ships.add(new ArrayList<Ship>());

            for (edu.utah.cs.cs4530.project3.model.ship.Ship s : battleship.getShips(i))
            {
                if (s.getClass().equals(edu.utah.cs.cs4530.project3.model.ship.Battleship.class))
                {
                    ships.get(i).add(new edu.utah.cs.cs4530.project3.view.ship.Battleship(this, s.getLength(), s.getHeading(), s.getLeft(), s.getTop()));
                }
                else if (s.getClass().equals(edu.utah.cs.cs4530.project3.model.ship.Carrier.class))
                {
                    ships.get(i).add(new Carrier(this, s.getLength(), s.getHeading(), s.getLeft(), s.getTop()));
                }
                else if (s.getClass().equals(edu.utah.cs.cs4530.project3.model.ship.Cruiser.class))
                {
                    ships.get(i).add(new Cruiser(this, s.getLength(), s.getHeading(), s.getLeft(), s.getTop()));
                }
                else if (s.getClass().equals(edu.utah.cs.cs4530.project3.model.ship.Destroyer.class))
                {
                    ships.get(i).add(new Destroyer(this, s.getLength(), s.getHeading(), s.getLeft(), s.getTop()));
                }
                else
                {
                    ships.get(i).add(new Submarine(this, s.getLength(), s.getHeading(), s.getLeft(), s.getTop()));
                }
            }
        }

        return ships;
    }

    @Override
    public void onShoot(int cell)
    {
        if (battleship.getStatus())
        {
            linearLayoutBattleship.addShot(battleship.shoot(cell), cell);
            linearLayoutBattleship.setPlayers(battleship.getOpponent(), battleship.getPlayer());
            linearLayoutBattleship.setStatus(battleship.getStatus());
        }
    }
}