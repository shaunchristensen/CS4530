/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.10.10
 * Assignment: Project 3 - MVC Battleship
 */

package edu.utah.cs.cs4530.project3.controller;

import android.content.res.Configuration;
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

import static android.graphics.Color.RED;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static edu.utah.cs.cs4530.project3.view.LinearLayoutGrid.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ActivityMain extends AppCompatActivity implements OnGameClickListener, OnNewGameClickListener, OnOKClickListener, OnShootListener, OnStartClickListener
{
    // fields

    private boolean booleanGame, booleanPlayer, booleanStart, booleanTablet;
    private Battleship battleship;
    private FragmentGame fragmentGame;
    private FragmentManager fragmentManager;
    private FragmentMenu fragmentMenu;
    private FragmentPlayer fragmentPlayer;
    private FragmentStart fragmentStart;
    private FrameLayout frameLayoutMenu, frameLayoutGame;
    private int intColumnsCount, intGame, intMargin, intPadding, intRowsCount;
    private List<Integer> listPlayers;
    private ListFragmentMenu listFragmentMenu;
    private final String stringBattleship = "Battleship";
    private final String stringFragmentGame = "fragmentGame";
    private final String stringFragmentMenu = "fragmentMenu";
    private final String stringFragmentPlayer = "fragmentPlayer";
    private final String stringFragmentStart = "fragmentStart";
    private final String stringListFragmentMenu = "listFragmentMenu";

    // methods

    private List<List<Ship>> getShips()
    {
        List<List<Ship>> ships = new ArrayList<>();

        for (int i : listPlayers)
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

    private List<String> getGameStrings()
    {
        List<String> gameStrings = new ArrayList<>();

        for (int i = 0; i < battleship.getGamesCount(); i++)
        {
            gameStrings.add(battleship.getGameString(i));
        }

        return gameStrings;
    }

    private void deserialize()
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

            Log.e("deserialize", "Error: Unable to read Battleship. " + e.getMessage());
        }
    }

    private void loadGame()
    {
        booleanGame = true;
        booleanPlayer = false;

        setFragments();
        setGame();

        fragmentPlayer.setText(battleship.getStatus(intGame), battleship.getOpponent(intGame), battleship.getPlayer(intGame));
    }

    @Override
    public void onBackPressed()
    {
        if (booleanPlayer)
        {
            booleanPlayer = false;
        }
        else if (booleanGame)
        {
            super.onBackPressed();

            booleanGame = false;

            listFragmentMenu.clearSelection();
        }
        else if (booleanStart)
        {
            super.onBackPressed();

            booleanStart = false;

            setLayoutParams();
        }
        else
        {
            finish();
        }

        setFragments();
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

        deserialize();

        if (savedInstanceState != null)
        {
            booleanGame = savedInstanceState.getBoolean("booleanGame");
            booleanPlayer = savedInstanceState.getBoolean("booleanPlayer");
            booleanStart = savedInstanceState.getBoolean("booleanStart");
            intGame = savedInstanceState.getInt("intGame");
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        if (sqrt(pow(displayMetrics.widthPixels/ displayMetrics.xdpi, 2) + pow(displayMetrics.heightPixels / displayMetrics.ydpi, 2)) >= 6)
        {
            booleanTablet = true;
        }
        else
        {
            booleanTablet = false;
        }

        intColumnsCount = battleship.getColumnsCount();
        intMargin = (displayMetrics.heightPixels < displayMetrics.widthPixels ? displayMetrics.heightPixels : displayMetrics.widthPixels) / 10;
        intPadding = intMargin / 5;
        intRowsCount = battleship.getRowsCount();
        listPlayers = battleship.getPlayers();

        LinearLayout linearLayout = new LinearLayout(this);

        if (booleanTablet)
        {
            frameLayoutMenu = new FrameLayout(this);
            frameLayoutMenu.setId(R.id.frameLayoutMenu);

            linearLayout.addView(frameLayoutMenu);
        }
        else
        {
            frameLayoutMenu = null;
        }

        frameLayoutGame = new FrameLayout(this);
        frameLayoutGame.setId(R.id.frameLayoutGame);

        linearLayout.addView(frameLayoutGame);

        fragmentManager = getSupportFragmentManager();

        fragmentGame = fragmentManager.findFragmentByTag(stringFragmentGame) == null ? new FragmentGame() : (FragmentGame)fragmentManager.findFragmentByTag(stringFragmentGame);
        fragmentGame.setColumnsCount(battleship.getColumnsCount());
        fragmentGame.setPadding(intPadding);
        fragmentGame.setPlayers(new ArrayList<Integer>(listPlayers));
        fragmentGame.setRowsCount(intRowsCount);

        fragmentMenu = fragmentManager.findFragmentByTag(stringFragmentMenu) == null ? new FragmentMenu() : (FragmentMenu)fragmentManager.findFragmentByTag(stringFragmentMenu);
        fragmentMenu.setMargin(intMargin);

        fragmentPlayer = fragmentManager.findFragmentByTag(stringFragmentPlayer) == null ? new FragmentPlayer() : (FragmentPlayer)fragmentManager.findFragmentByTag(stringFragmentPlayer);
        fragmentPlayer.setColumnsCount(intColumnsCount);
        fragmentPlayer.setMargin(intMargin);

        fragmentStart = fragmentManager.findFragmentByTag(stringFragmentStart) == null ? new FragmentStart() : (FragmentStart)fragmentManager.findFragmentByTag(stringFragmentStart);
        fragmentStart.setMargin(intMargin);

        listFragmentMenu = fragmentManager.findFragmentByTag(stringListFragmentMenu) == null ? new ListFragmentMenu() : (ListFragmentMenu)fragmentManager.findFragmentByTag(stringListFragmentMenu);
        listFragmentMenu.setGameStrings(getGameStrings());
        listFragmentMenu.setPadding(intPadding);

        if (savedInstanceState == null)
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(frameLayoutGame.getId(), fragmentGame, stringFragmentGame);
            fragmentTransaction.add(frameLayoutGame.getId(), fragmentPlayer, stringFragmentPlayer);
            fragmentTransaction.add(frameLayoutGame.getId(), fragmentStart, stringFragmentStart);

            if (booleanTablet)
            {
                fragmentTransaction.add(frameLayoutGame.getId(), fragmentMenu, stringFragmentMenu);
                fragmentTransaction.add(frameLayoutMenu.getId(), listFragmentMenu, stringListFragmentMenu);
            }
            else
            {
                fragmentTransaction.add(frameLayoutGame.getId(), listFragmentMenu, stringListFragmentMenu);
            }

            fragmentTransaction.commit();

            fragmentManager.executePendingTransactions();
        }

        setContentView(linearLayout);
        setFragments();
        setGame();
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

        listFragmentMenu.addGameString(battleship.getGameString(intGame));

        loadGame();
    }

    @Override
    public void onOKClick(Fragment fragment)
    {
        booleanPlayer = true;

        setFragments();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putBoolean("booleanGame", booleanGame);
        outState.putBoolean("booleanPlayer", booleanPlayer);
        outState.putBoolean("booleanStart", booleanStart);
        outState.putInt("intGame", intGame);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onShoot(int cell)
    {
        if (battleship.getStatus(intGame))
        {
            booleanPlayer = false;

            setFragments();

            boolean hit = battleship.shoot(intGame, cell);

            fragmentGame.addShot(hit, cell);
            fragmentGame.setStatus(battleship.getStatus(intGame));
            fragmentGame.setPlayers(battleship.getOpponent(intGame), battleship.getPlayer(intGame));
            fragmentPlayer.setText(hit, battleship.getStatus(intGame), cell, battleship.getOpponent(intGame), battleship.getPlayer(intGame));
            listFragmentMenu.setGameString(intGame, battleship.getGameString(intGame));
        }
    }

    @Override
    public void onStartClick(Fragment fragment)
    {
        booleanStart = true;

        setFragments();
        setLayoutParams();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        serialize();
    }

    private void serialize()
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
            Log.e("serialize", "Error: Unable to write Battleship. " + e.getMessage());
        }
    }

    private void setFragments()
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(fragmentGame);
        fragmentTransaction.detach(fragmentPlayer);
        fragmentTransaction.detach(fragmentStart);

        if (booleanTablet)
        {
            fragmentTransaction.detach(fragmentMenu);
        }
        else
        {
            fragmentTransaction.detach(listFragmentMenu);
        }

        if (booleanStart)
        {
            if (booleanGame)
            {
                if (booleanPlayer)
                {
                    fragmentTransaction.attach(fragmentGame);
                }
                else
                {
                    fragmentTransaction.attach(fragmentPlayer);
                    fragmentTransaction.addToBackStack(stringListFragmentMenu);
                }
            }
            else
            {
                if (booleanTablet)
                {
                    fragmentTransaction.attach(fragmentMenu);
                }

                fragmentTransaction.attach(listFragmentMenu);
                fragmentTransaction.addToBackStack(stringListFragmentMenu);
            }
        }
        else
        {
            fragmentTransaction.attach(fragmentStart);
        }

        fragmentTransaction.commit();

        fragmentManager.executePendingTransactions();
    }

    private void setGame()
    {
        fragmentGame.setGame(battleship.getStatus(intGame), battleship.getOpponent(intGame), battleship.getPlayer(intGame), getShips(), battleship.getHits(intGame), battleship.getMisses(intGame));
    }

    private void setLayoutParams()
    {
        if (booleanStart && booleanTablet)
        {
            frameLayoutGame.setLayoutParams(new LayoutParams(0, MATCH_PARENT, 3));

            frameLayoutMenu.setLayoutParams(new LayoutParams(0, MATCH_PARENT, 1));
            frameLayoutMenu.setPadding(intPadding, intPadding, 0, intPadding);
        }
        else
        {
            if (booleanTablet)
            {
                frameLayoutMenu.setLayoutParams(new LayoutParams(0, 0));
            }

            frameLayoutGame.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        }

        frameLayoutGame.setPadding(intPadding, intPadding, intPadding, intPadding);
    }
}