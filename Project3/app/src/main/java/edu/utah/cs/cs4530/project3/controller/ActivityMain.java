/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.10
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import edu.utah.cs.cs4530.project3.R;
import edu.utah.cs.cs4530.project3.controller.FragmentPlayer.OnOKClickListener;
import edu.utah.cs.cs4530.project3.controller.FragmentStart.OnStartClickListener;
import edu.utah.cs.cs4530.project3.controller.ListFragmentMenu.OnGameClickListener;
import edu.utah.cs.cs4530.project3.controller.ListFragmentMenu.OnNewGameClickListener;
import edu.utah.cs.cs4530.project3.model.Battleship;
import edu.utah.cs.cs4530.project3.view.ship.Carrier;
import edu.utah.cs.cs4530.project3.view.ship.Cruiser;
import edu.utah.cs.cs4530.project3.view.ship.Destroyer;
import edu.utah.cs.cs4530.project3.view.ship.Ship;
import edu.utah.cs.cs4530.project3.view.ship.Submarine;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static edu.utah.cs.cs4530.project3.view.LinearLayoutGrid.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ActivityMain extends AppCompatActivity implements OnGameClickListener, OnNewGameClickListener, OnOKClickListener, OnShootListener, OnStartClickListener
{
    // fields

    private boolean booleanGame, booleanPlayer, booleanStart, booleanStatus, booleanTablet;
    private Battleship battleship;
    private FragmentGame fragmentGame;
    private FragmentManager fragmentManager;
    private FragmentMenu fragmentMenu;
    private FragmentPlayer fragmentPlayer;
    private FragmentStart fragmentStart;
    private FragmentSummary fragmentSummary;
    private FrameLayout frameLayoutMenu, frameLayoutGame;
    private int intGame, intMargin;
    private ListFragmentMenu listFragmentMenu;
    private final String stringBattleship = "Battleship";
    private final String stringFragmentGame = "fragmentGame";
    private final String stringFragmentMenu = "fragmentMenu";
    private final String stringFragmentPlayer = "fragmentPlayer";
    private final String stringFragmentStart = "fragmentStart";
    private final String stringfragmentSummary = "fragmentSummary";
    private final String stringListFragmentMenu = "listFragmentMenu";

    // methods

    private List<List<Ship>> getShips()
    {
        List<List<Ship>> ships = new ArrayList<>();

        for (int i : battleship.getPlayers())
        {
            ships.add(new ArrayList<Ship>());

            for (edu.utah.cs.cs4530.project3.model.ship.Ship s : battleship.getShips(intGame, i))
            {
                switch (s.getClass().getSimpleName())
                {
                    case "Battleship": ships.get(i).add(new edu.utah.cs.cs4530.project3.view.ship.Battleship(this, s.getLength(), s.getHeading(), s.getLeft(), s.getTop()));
                        break;
                    case "Carrier": ships.get(i).add(new Carrier(this, s.getLength(), s.getHeading(), s.getLeft(), s.getTop()));
                        break;
                    case "Cruiser": ships.get(i).add(new Cruiser(this, s.getLength(), s.getHeading(), s.getLeft(), s.getTop()));
                        break;
                    case "Destroyer": ships.get(i).add(new Destroyer(this, s.getLength(), s.getHeading(), s.getLeft(), s.getTop()));
                        break;
                    default: ships.get(i).add(new Submarine(this, s.getLength(), s.getHeading(), s.getLeft(), s.getTop()));
                        break;
                }
            }
        }

        return ships;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        for (File f : getFilesDir().listFiles())
        {
            if (f.getName().equals("Battleship"))
            {
//                f.delete();
            }
        }

        deserialize("onCreate");

        if (savedInstanceState != null)
        {
            booleanGame = savedInstanceState.getBoolean("booleanGame");
            booleanPlayer = savedInstanceState.getBoolean("booleanPlayer");
            booleanStart = savedInstanceState.getBoolean("booleanStart");
            booleanStatus = savedInstanceState.getBoolean("booleanStatus");
            intGame = savedInstanceState.getInt("intGame");
        }

        fragmentManager = getSupportFragmentManager();
        fragmentGame = fragmentManager.findFragmentByTag(stringFragmentGame) == null ? new FragmentGame() : (FragmentGame)fragmentManager.findFragmentByTag(stringFragmentGame);
        fragmentMenu = fragmentManager.findFragmentByTag(stringFragmentMenu) == null ? new FragmentMenu() : (FragmentMenu)fragmentManager.findFragmentByTag(stringFragmentMenu);
        fragmentPlayer = fragmentManager.findFragmentByTag(stringFragmentPlayer) == null ? new FragmentPlayer() : (FragmentPlayer)fragmentManager.findFragmentByTag(stringFragmentPlayer);
        fragmentStart = fragmentManager.findFragmentByTag(stringFragmentStart) == null ? new FragmentStart() : (FragmentStart)fragmentManager.findFragmentByTag(stringFragmentStart);
        fragmentSummary = fragmentManager.findFragmentByTag(stringfragmentSummary) == null ? new FragmentSummary() : (FragmentSummary)fragmentManager.findFragmentByTag(stringfragmentSummary);
        listFragmentMenu = fragmentManager.findFragmentByTag(stringListFragmentMenu) == null ? new ListFragmentMenu() : (ListFragmentMenu)fragmentManager.findFragmentByTag(stringListFragmentMenu);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        LinearLayout linearLayout = new LinearLayout(this);

        intMargin = (displayMetrics.heightPixels < displayMetrics.widthPixels ? displayMetrics.heightPixels : displayMetrics.widthPixels) / 50;

        if (sqrt(pow(displayMetrics.widthPixels/ displayMetrics.xdpi, 2) + pow(displayMetrics.heightPixels / displayMetrics.ydpi, 2)) >= 6)
        {
            booleanTablet = true;

            frameLayoutMenu = new FrameLayout(this);
            frameLayoutMenu.setId(R.id.frameLayoutMenu);

            linearLayout.addView(frameLayoutMenu);
        }
        else
        {
            booleanTablet = false;
            frameLayoutMenu = null;
        }

        frameLayoutGame = new FrameLayout(this);
        frameLayoutGame.setId(R.id.frameLayoutGame);

        linearLayout.addView(frameLayoutGame);

        replaceFragments();
        setContentView(linearLayout);
        setLayoutParams();
    }

    private void deserialize(String tag)
    {
        try
        {
            FileInputStream fileInputStream = getApplicationContext().openFileInput(stringBattleship);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            battleship = (Battleship)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }
        catch (Exception e)
        {
            battleship = Battleship.getBattleship();


            Log.e(tag, "Error: Unable to read Battleship. " + e.getMessage());
        }
    }

    private void loadGame()
    {
        booleanGame = true;
        booleanPlayer = false;
        booleanStatus = battleship.getStatus(intGame);

        listFragmentMenu.invalidate();

        replaceFragments();
        setLayoutParams();

        fragmentPlayer.setText(false, false, booleanStatus, 0, battleship.getOpponent(intGame), battleship.getPlayer(intGame));
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        if (booleanPlayer)
        {
            booleanPlayer = false;
        }
        else if (booleanGame)
        {
            booleanGame = false;
        }
        else if (booleanStart)
        {
            booleanStart = false;
        }
        else
        {
            return;
        }

        replaceFragments();
        setLayoutParams();
    }

    @Override
    public void onGameClick(int game)
    {
        intGame = game;

        loadGame();
    }

    @Override
    public void onNewGameClick()
    {
        intGame = battleship.newGame();

        loadGame();
    }

    @Override
    public void onOKClick(Fragment fragment)
    {
        if (booleanStatus)
        {
            booleanPlayer = true;

            replaceFragments();
            setLayoutParams();

            fragmentGame.loadGame(battleship.getStatus(intGame), battleship.getOpponent(intGame), battleship.getPlayer(intGame), getShips(), battleship.getHits(intGame), battleship.getMisses(intGame));
        }
        else
        {
            replaceFragments();
            setLayoutParams();

            fragmentSummary.setText(battleship.getPlayer(intGame), battleship.getHitsCount(intGame, 0), battleship.getMissesCount(intGame, 0), battleship.getHitsCount(intGame, 1), battleship.getMissesCount(intGame, 1));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putBoolean("booleanGame", booleanGame);
        outState.putBoolean("booleanPlayer", booleanPlayer);
        outState.putBoolean("booleanStart", booleanStart);
        outState.putBoolean("booleanStatus", booleanStatus);
        outState.putInt("intGame", intGame);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onShoot(int cell)
    {
        boolean hit = battleship.shoot(intGame, cell);

        booleanPlayer = false;

        replaceFragments();
        setLayoutParams();

        booleanStatus = battleship.getStatus(intGame);;

        fragmentGame.addShot(hit, cell);
        fragmentPlayer.setText(true, hit, booleanStatus, cell, battleship.getOpponent(intGame), battleship.getPlayer(intGame));
//        fragmentGame.setStatus(booleanStatus);
//        fragmentGame.setPlayers(battleship.getOpponent(intGame), battleship.getPlayer(intGame));
    }

    @Override
    public void onStartClick(Fragment fragment)
    {
        booleanStart = true;

        replaceFragments();
        setLayoutParams();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        serialize("onStop");
    }

    private void replaceFragments()
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (booleanStart)
        {
            if (booleanGame)
            {
                if (booleanStatus)
                {
                    if (booleanPlayer)
                    {
                        fragmentTransaction.replace(frameLayoutGame.getId(), fragmentGame, stringFragmentGame);
                        fragmentTransaction.addToBackStack(null);
                    }
                    else
                    {
                        fragmentTransaction.replace(frameLayoutGame.getId(), fragmentPlayer, stringFragmentPlayer);
                    }
                }
                else
                {
                    fragmentTransaction.replace(frameLayoutGame.getId(), fragmentSummary, stringfragmentSummary);
                }
            }
            else if (booleanTablet)
            {
                fragmentTransaction.replace(frameLayoutMenu.getId(), listFragmentMenu, stringListFragmentMenu);
                fragmentTransaction.replace(frameLayoutGame.getId(), fragmentMenu, stringFragmentMenu);
                fragmentTransaction.addToBackStack(null);
            }
            else
            {
                fragmentTransaction.replace(frameLayoutGame.getId(), listFragmentMenu, stringListFragmentMenu);
                fragmentTransaction.addToBackStack(null);
            }
        }
        else
        {
            fragmentTransaction.replace(frameLayoutGame.getId(), fragmentStart, stringFragmentStart);
        }

        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    private void serialize(String tag)
    {
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), stringBattleship));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(battleship);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch(Exception e)
        {
            Log.e(tag, "Error: Unable to write Battleship. " + e.getMessage());
        }
    }

    private void setLayoutParams()
    {
        LayoutParams layoutParamsGame, layoutParamsMenu;

        if (booleanStart && booleanTablet)
        {
            layoutParamsGame = new LayoutParams(0, MATCH_PARENT, 3);

            layoutParamsMenu = new LayoutParams(0, MATCH_PARENT, 1);
            layoutParamsMenu.setMargins(intMargin, intMargin, 0, intMargin);

            frameLayoutMenu.setLayoutParams(layoutParamsMenu);
        }
        else
        {
            if (booleanTablet)
            {
                layoutParamsMenu = new LayoutParams(0, 0);
                layoutParamsMenu.setMargins(0, 0, 0, 0);

                frameLayoutMenu.setLayoutParams(layoutParamsMenu);
            }

            layoutParamsGame = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        }

        layoutParamsGame.setMargins(intMargin, intMargin, intMargin, intMargin);
        frameLayoutGame.setLayoutParams(layoutParamsGame);
    }
}