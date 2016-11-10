/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import edu.utah.cs.cs4530.project4.R;
import edu.utah.cs.cs4530.project4.controller.FragmentPlayer.OnOKClickListener;
import edu.utah.cs.cs4530.project4.controller.FragmentStart.OnStartClickListener;
import edu.utah.cs.cs4530.project4.controller.ListFragmentMenu.OnGameClickListener;
import edu.utah.cs.cs4530.project4.controller.ListFragmentMenu.OnGameStatusSelectListener;
import edu.utah.cs.cs4530.project4.controller.ListFragmentMenu.OnNewGameClickListener;
import edu.utah.cs.cs4530.project4.model.Game;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static edu.utah.cs.cs4530.project4.view.LinearLayoutGrid.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ActivityMain extends AppCompatActivity implements OnGameClickListener, OnNewGameClickListener, OnGameStatusSelectListener, OnShootListener, OnStartClickListener
{
    // fields

    private List<Game> listGames;

private Gson gson = new Gson();

    private boolean booleanGame, booleanPlayer, booleanStart, booleanTablet;
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

    private void deserialize()
    {
        try
        {
            FileInputStream fileInputStream = getApplicationContext().openFileInput(stringBattleship);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//            battleship = (Battleship)objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        }
        catch (Exception e)
        {
//            battleship = Battleship.getBattleship();

            Log.e("deserialize", "Error: Unable to read Battleship. " + e.getMessage());
        }
    }

    private void loadGame()
    {
        booleanGame = true;
        booleanPlayer = false;

        setFragments();
        setGame();
//        fragmentPlayer.setText(battleship.getStatus(intGame), battleship.getOpponent(intGame), battleship.getPlayer(intGame));
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

        listFragmentMenu.invalidateViews();

        setFragments();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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

//        intColumnsCount = battleship.getColumnsCount();
        intMargin = (displayMetrics.heightPixels < displayMetrics.widthPixels ? displayMetrics.heightPixels : displayMetrics.widthPixels) / 10;
        intPadding = intMargin / 5;
//        intRowsCount = battleship.getRowsCount();
//        listPlayers = battleship.getPlayers();

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
//        fragmentGame.setColumnsCount(battleship.getColumnsCount());
        fragmentGame.setPadding(intPadding);
//        fragmentGame.setPlayers(new ArrayList<Integer>(listPlayers));
        fragmentGame.setRowsCount(intRowsCount);

        fragmentMenu = fragmentManager.findFragmentByTag(stringFragmentMenu) == null ? new FragmentMenu() : (FragmentMenu)fragmentManager.findFragmentByTag(stringFragmentMenu);
        fragmentMenu.setMargin(intMargin);

        fragmentPlayer = fragmentManager.findFragmentByTag(stringFragmentPlayer) == null ? new FragmentPlayer() : (FragmentPlayer)fragmentManager.findFragmentByTag(stringFragmentPlayer);
        fragmentPlayer.setColumnsCount(intColumnsCount);
        fragmentPlayer.setMargin(intMargin);

        fragmentStart = fragmentManager.findFragmentByTag(stringFragmentStart) == null ? new FragmentStart() : (FragmentStart)fragmentManager.findFragmentByTag(stringFragmentStart);
        fragmentStart.setMargin(intMargin);

        listFragmentMenu = fragmentManager.findFragmentByTag(stringListFragmentMenu) == null ? new ListFragmentMenu() : (ListFragmentMenu)fragmentManager.findFragmentByTag(stringListFragmentMenu);
//        listFragmentMenu.setGameStrings(getGameStrings());
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






        if (isConnected())
        {
           new test().execute("lobby");
        }

        setContentView(linearLayout);
        setFragments();
        setGame();
        setLayoutParams();
    }

    private boolean isConnected()
    {
        NetworkInfo networkInfo = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private class test extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            HttpURLConnection httpURLConnection = null;
            StringBuilder stringBuilder = new StringBuilder();

            try
            {
                URL url = new URL("http://battleship.pixio.com/api/v2/" + params[0]);

                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setChunkedStreamingMode(0);
                httpURLConnection.setDoOutput(true);

                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpURLConnection.setRequestMethod("POST");

                try
                (
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                )
                {
                    Log.i("Shiiiit", "{\"gameName=\"Hello\",\"playerName\":\"It's Me\"}");
                    bufferedWriter.write("{\"gameName\":\"Hello\",\"playerName\":\"It's Me\"}");
//                    bufferedWriter.flush();
                }
                catch (Exception e)
                {
                    Log.e("Write", "Error: " + e.getMessage());
                }

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    try
                    (
                            InputStream inputStream = httpURLConnection.getInputStream();
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
                    )
                    {
                        String s;

                        while ((s = bufferedReader.readLine()) != null)
                        {
                            stringBuilder.append(s);
                        }
                        Log.i("Hello", "It's me4");
                    }
                    catch (Exception e)
                    {
                        Log.e("Read", "Error: " + e.getMessage());
                    }
                }
                else
                {
                    Log.i("Shit", "" + httpURLConnection.getResponseCode());
                    // shit shit shit shit shit
                }
            }
            catch (Exception e)
            {
            }
            finally
            {
                if (httpURLConnection != null)
                {
                    httpURLConnection.disconnect();
                }
            }

            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s)
        {

//            listGames = gson.fromJson(s, new TypeToken<List<Game>>(){}.getType());
//            listFragmentMenu.setGames(listGames);
        }
    }

    /*
    @Override
    public void onGameClick(int game)
    {
        intGame = game;

        loadGame();
    }

    @Override
    public void onNewGameClick()
    {
//        intGame = battleship.addGame();

//        listFragmentMenu.addGameString(battleship.getGameString(intGame));
        loadGame();
    }
*/
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
        /*
        if (battleship.getStatus(intGame))
        {
            boolean hit = battleship.shoot(intGame, cell);
            booleanPlayer = false;

            setFragments();

            fragmentGame.addShot(hit, cell);
            fragmentGame.setStatus(battleship.getStatus(intGame));
            fragmentGame.setPlayers(battleship.getOpponent(intGame), battleship.getPlayer(intGame));

            fragmentPlayer.setText(hit, battleship.getStatus(intGame), cell, battleship.getOpponent(intGame), battleship.getPlayer(intGame));

            listFragmentMenu.clearItemLongClick();
            listFragmentMenu.setGameString(intGame, battleship.getGameString(intGame));
        }
        */
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
//            objectOutputStream.writeObject(battleship);
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
                    fragmentTransaction.addToBackStack(stringFragmentPlayer);
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
        if (booleanGame)
        {
  //          fragmentGame.setGame(battleship.getStatus(intGame), battleship.getOpponent(intGame), battleship.getPlayer(intGame), getShips(), battleship.getHits(intGame), battleship.getMisses(intGame));
        }
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

    @Override
    public void onGameClick(String id) {

    }

    @Override
    public void onGameStatusSelect(int index) {

    }

    @Override
    public void onNewGameClick() {

    }
}