/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.05
 * Assignment: Project 4 - Networked Battleship
 */

package edu.utah.cs.cs4530.project4.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.utah.cs.cs4530.project4.R;
import edu.utah.cs.cs4530.project4.controller.FragmentStart.OnStartClickListener;
import edu.utah.cs.cs4530.project4.controller.ListFragmentMenu.OnGameClickListener;
import edu.utah.cs.cs4530.project4.controller.ListFragmentMenu.OnGameSetSelectListener;
import edu.utah.cs.cs4530.project4.controller.ListFragmentMenu.OnNewGameClickListener;
import edu.utah.cs.cs4530.project4.model.Battleship;
import edu.utah.cs.cs4530.project4.model.Game;
import edu.utah.cs.cs4530.project4.model.GameIDPlayerID;
import edu.utah.cs.cs4530.project4.model.GameIDPlayerName;
import edu.utah.cs.cs4530.project4.model.GameNamePlayerName;
import edu.utah.cs.cs4530.project4.model.GameSummary;
import edu.utah.cs.cs4530.project4.model.Grids;
import edu.utah.cs.cs4530.project4.model.PlayerID;
import edu.utah.cs.cs4530.project4.model.PlayerIDCell;
import edu.utah.cs.cs4530.project4.model.Shot;
import edu.utah.cs.cs4530.project4.model.Status;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static edu.utah.cs.cs4530.project4.view.LinearLayoutGrid.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ActivityMain extends AppCompatActivity implements OnGameClickListener, OnGameSetSelectListener, OnNewGameClickListener, OnShootListener, OnStartClickListener
{
    // fields
private String stringGameID;
private List<Game> listGames;
private Handler handler;
private Runnable runnableGetGames, runnableGetStatuses;
private Gson gson = new Gson();
private int intGameSet;
private Type type;
private boolean booleanSetSelection;
private String stringGameName, stringPlayerName;
private Map<String, String> mapPlayerIDs;
private Map<String, edu.utah.cs.cs4530.project4.model.Status> mapStatuses;
    private Battleship battleship;
    private boolean booleanGame, booleanStart, booleanSummary, booleanTablet;
    private FragmentGame fragmentGame;
    private FragmentManager fragmentManager;
    private FragmentMenu fragmentMenu;
    private FragmentStart fragmentStart;
    private FragmentSummary fragmentSummary;
    private FrameLayout frameLayoutMenu, frameLayoutGame;
    private int intColumnsCount, intMargin, intPadding, intRowsCount;
    private ListFragmentMenu listFragmentMenu;
    private final String stringFragmentGame = "fragmentGame";
    private final String stringFragmentMenu = "fragmentMenu";
    private final String stringFragmentStart = "fragmentStart";
    private final String stringFragmentSummary = "fragmentSummary";
    private final String stringListFragmentMenu = "listFragmentMenu";
    private final String stringPlayerIDs = "PlayerIDs";
    private final String stringStatuses = "Statuses";

    // methods

    private void loadGame()
    {
        booleanGame = true;

        setFragments();
        setGame();
//        fragmentPlayer.setText(battleship.getStatus(intGame), battleship.getOpponent(intGame), battleship.getPlayer(intGame));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        deserialize();

        if (savedInstanceState != null)
        {
            booleanGame = savedInstanceState.getBoolean("booleanGame");
            booleanStart = savedInstanceState.getBoolean("booleanStart");
            booleanSummary = savedInstanceState.getBoolean("booleanPlayer");
            intGameSet = savedInstanceState.getInt("intGameSet");
            stringGameID = savedInstanceState.getString("stringGameID");
            stringGameName = savedInstanceState.getString("stringGameName");
            stringPlayerName = savedInstanceState.getString("stringGameName");
        }
        else
        {
            stringGameID = stringGameName = stringPlayerName = "";
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

        battleship = Battleship.getBattleship();
        intColumnsCount = battleship.getColumnsCount();
        intMargin = (displayMetrics.heightPixels < displayMetrics.widthPixels ? displayMetrics.heightPixels : displayMetrics.widthPixels) / 10;
        intPadding = intMargin / 5;
        intRowsCount = battleship.getRowsCount();

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
        fragmentGame.setRowsCount(intRowsCount);

        fragmentMenu = fragmentManager.findFragmentByTag(stringFragmentMenu) == null ? new FragmentMenu() : (FragmentMenu)fragmentManager.findFragmentByTag(stringFragmentMenu);
        fragmentMenu.setMargin(intMargin);

        fragmentStart = fragmentManager.findFragmentByTag(stringFragmentStart) == null ? new FragmentStart() : (FragmentStart)fragmentManager.findFragmentByTag(stringFragmentStart);
        fragmentStart.setMargin(intMargin);

        fragmentSummary = fragmentManager.findFragmentByTag(stringFragmentSummary) == null ? new FragmentSummary() : (FragmentSummary)fragmentManager.findFragmentByTag(stringFragmentSummary);
        fragmentSummary.setColumnsCount(intColumnsCount);
        fragmentSummary.setMargin(intMargin);

        listFragmentMenu = fragmentManager.findFragmentByTag(stringListFragmentMenu) == null ? new ListFragmentMenu() : (ListFragmentMenu)fragmentManager.findFragmentByTag(stringListFragmentMenu);
        listFragmentMenu.setPadding(intPadding);

        if (savedInstanceState == null)
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(frameLayoutGame.getId(), fragmentGame, stringFragmentGame);
            fragmentTransaction.add(frameLayoutGame.getId(), fragmentSummary, stringFragmentSummary);
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

        if (listGames == null)
        {
            listGames = new ArrayList<>();
        }

        type = new TypeToken<List<Game>>(){}.getType();
        handler = new Handler();
        runnableGetGames = new Runnable()
        {
            @Override
            public void run()
            {
                new AsyncTaskGetGames().execute();
                handler.postDelayed(runnableGetGames, 5000);
            }
        };
        runnableGetStatuses = new Runnable()
        {
            @Override
            public void run()
            {
                synchronized (mapStatuses)
                {
                    for (String s : mapStatuses.keySet())
                    {
                        new AsyncTaskGetStatuses().execute(s, mapPlayerIDs.get(s));
                    }

                    handler.postDelayed(runnableGetStatuses, 500);
                }
            }
        };

        handler.post(runnableGetGames);
        handler.post(runnableGetStatuses);

        setContentView(linearLayout);
        setFragments();
//        listFragmentMenu.setSelection(intGameSet);
        setGame();
        setLayoutParams();
    }

    @Override
    public void onGameClick(final Game game)
    {
        if (mapPlayerIDs.containsKey(game.getID()))
        {
            if (!game.getID().equalsIgnoreCase(stringGameID) && game.getStatus() == battleship.IN_PROGRESS || game.getStatus() == battleship.OVER)
            {
                booleanGame = true;
                stringGameID = game.getID();

                new AsyncTaskGetGrids().execute(stringGameID, mapPlayerIDs.get(stringGameID));
                new AsyncTaskGetStatuses().execute(stringGameID, mapPlayerIDs.get(stringGameID));
                listFragmentMenu.setGame(stringGameID);
                setFragments();
            }
        }
        else  if (game.getStatus() == battleship.WAITING)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Enter player information and press Start.");
            alertDialog.setTitle("Join Game");

            final EditText editTextPlayerName = new EditText(this);
            editTextPlayerName.setHint("Player Name");
            editTextPlayerName.setText(stringPlayerName);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.addView(editTextPlayerName, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            linearLayout.setOrientation(VERTICAL);
            linearLayout.setPadding(intPadding, intPadding, intPadding, intPadding);

            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }
            });
            alertDialog.setPositiveButton("Start", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    stringPlayerName = editTextPlayerName.getText().toString();

                    new AsyncTaskJoinGame().execute(game.getID(), stringPlayerName);
                    dialog.dismiss();
                }
            });
            alertDialog.setView(linearLayout);
            alertDialog.show();
        }
        else
        {
            stringGameID = game.getID();

            new AsyncTaskGetGameSummary().execute(game.getID());
        }
    }




    private void setGame()
    {
        if (booleanGame && stringGameID != null)
        {
            new AsyncTaskGetGrids().execute(stringGameID, mapPlayerIDs.get(stringGameID));
        }
    }














































    @Override
    public void onShoot(int cell)
    {
        new AsyncTaskShoot().execute(stringGameID, mapPlayerIDs.get(stringGameID), Integer.toString(cell % intColumnsCount), Integer.toString(cell / intColumnsCount));
    }


    @Override
    public void onBackPressed()
    {
        if (booleanGame)
        {
            super.onBackPressed();

            booleanGame = false;
        }
        if (booleanSummary)
        {
            super.onBackPressed();

            booleanSummary = false;
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

    private void setFragments()
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(fragmentGame);
        fragmentTransaction.detach(fragmentStart);
        fragmentTransaction.detach(fragmentSummary);

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
                fragmentTransaction.attach(fragmentGame);
                fragmentTransaction.addToBackStack(stringFragmentGame);
            }
            else if (booleanSummary)
            {
                fragmentTransaction.attach(fragmentSummary);
                fragmentTransaction.addToBackStack(stringFragmentSummary);
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

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putBoolean("booleanGame", booleanGame);
        outState.putBoolean("booleanStart", booleanStart);
        outState.putBoolean("booleanSummary", booleanSummary);
        outState.putInt("intGameSet", intGameSet);
        outState.putString("stringGameID", stringGameID);
        outState.putString("stringGameName", stringGameName);
        outState.putString("stringPlayerName", stringPlayerName);

        super.onSaveInstanceState(outState);
    }


    private void serialize()
    {
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), stringPlayerIDs)); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream))
        {
            objectOutputStream.writeObject(mapPlayerIDs);
        }
        catch(Exception e)
        {
            Log.e("serialize", "Error: Unable to write the player IDs. " + e.getMessage());
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), stringStatuses)); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream))
        {
            objectOutputStream.writeObject(mapStatuses);
        }
        catch(Exception e)
        {
            Log.e("serialize", "Error: Unable to write the statuses. " + e.getMessage());
        }
    }

    private void setGames()
    {
        List<Game> games = new ArrayList<>();

        if (intGameSet == battleship.MY_GAMES)
        {
            for (Game g : listGames)
            {
                if (mapPlayerIDs.containsKey(g.getID()))
                {
                    games.add((g));
                }
            }
        }
        else if (intGameSet == battleship.WAITING || intGameSet == battleship.IN_PROGRESS || intGameSet == battleship.OVER)
        {
            for (Game g : listGames)
            {
                if (g.getStatus() == intGameSet)
                {
                    games.add((g));
                }
            }
        }
        else
        {
            games.addAll(listGames);
        }

        listFragmentMenu.setGames(games);

        if (booleanSetSelection)
        {
            booleanSetSelection = false;

            listFragmentMenu.setSelection(intGameSet);
        }
    }

    @Override
    public void onGameSetSelect(int gameSet)
    {
        booleanSetSelection = true;
        intGameSet = gameSet;

        setGames();
    }



    private boolean isConnected()
    {
        NetworkInfo networkInfo = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void deserialize()
    {
        try (FileInputStream fileInputStream = getApplicationContext().openFileInput(stringPlayerIDs); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream))
        {
            mapPlayerIDs = (Map<String, String>)objectInputStream.readObject();
        }
        catch (Exception e)
        {
            mapPlayerIDs = new HashMap<>();

            Log.e("deserialize", "Error: Unable to read the player IDs. " + e.getMessage());
        }

        try (FileInputStream fileInputStream = getApplicationContext().openFileInput(stringStatuses); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream))
        {
            mapStatuses = (Map<String, Status>)objectInputStream.readObject();
        }
        catch (Exception e)
        {
            mapStatuses = new HashMap<>();

            Log.e("deserialize", "Error: Unable to read the statuses. " + e.getMessage());
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

    @Override
    public void onNewGameClick()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Enter game and player information and press Start.");
        alertDialog.setTitle("New Game");

        final EditText editTextGameName = new EditText(this);
        editTextGameName.setHint("Game Name");
        editTextGameName.setText(stringGameName);

        final EditText editTextPlayerName = new EditText(this);
        editTextPlayerName.setHint("Player Name");
        editTextPlayerName.setText(stringPlayerName);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(editTextGameName, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        linearLayout.addView(editTextPlayerName, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        linearLayout.setOrientation(VERTICAL);
        linearLayout.setPadding(intPadding, intPadding, intPadding, intPadding);

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertDialog.setPositiveButton("Start", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                stringGameName = editTextGameName.getText().toString();
                stringPlayerName = editTextPlayerName.getText().toString();

                new AsyncTaskNewGame().execute(stringGameName, stringPlayerName);
                dialog.dismiss();
            }
        });
        alertDialog.setView(linearLayout);
        alertDialog.show();
    }

    // classes

    private class AsyncTaskGetGameSummary extends AsyncTask<String, Void, String>
    {
        // fields

        private boolean booleanPostExecute;

        // methods

        @Override
        protected String doInBackground(String... params)
        {
            if (isConnected())
            {
                HttpURLConnection httpURLConnection = null;
                StringBuilder stringBuilder = new StringBuilder();

                try
                {
                    URL url = new URL("http://battleship.pixio.com/api/v2/lobby/" + params[0]);
                    httpURLConnection = (HttpURLConnection)url.openConnection();

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        try (InputStream inputStream = httpURLConnection.getInputStream(); InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
                        {
                            String s;

                            while ((s = bufferedReader.readLine()) != null)
                            {
                                stringBuilder.append(s);
                            }

                            booleanPostExecute = true;
                        }
                    }
                    else
                    {
                        Log.i("responseCode", "Error " + httpURLConnection.getResponseCode() + ": Unable to connect to the server. " + httpURLConnection.getResponseMessage());
                    }
                }
                catch (Exception e)
                {
                    Log.i("openConnection", "Error: Unable to connect to the server. " + e.getMessage());
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
            else
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            if (booleanPostExecute)
            {
                Log.i("s", s);
                booleanGame = false;
                booleanSummary = true;
                GameSummary gameSummary = gson.fromJson(s, GameSummary.class);
                listFragmentMenu.setGame(stringGameID);
                setFragments();
            }
        }
    }

    private class AsyncTaskGetGames extends AsyncTask<String, Void, String>
    {
        // fields

        private boolean booleanPostExecute;

        // methods

        @Override
        protected String doInBackground(String... params)
        {
            if (isConnected())
            {
                HttpURLConnection httpURLConnection = null;
                StringBuilder stringBuilder = new StringBuilder();

                try
                {
                    URL url = new URL("http://battleship.pixio.com/api/v2/lobby");
                    httpURLConnection = (HttpURLConnection)url.openConnection();

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        try (InputStream inputStream = httpURLConnection.getInputStream(); InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
                        {
                            String s;

                            while ((s = bufferedReader.readLine()) != null)
                            {
                                stringBuilder.append(s);
                            }

                            booleanPostExecute = true;
                        }
                    }
                    else
                    {
                        Log.e("responseCode", "Error " + httpURLConnection.getResponseCode() + ": Unable to connect to the server. " + httpURLConnection.getResponseMessage());
                    }
                }
                catch (Exception e)
                {
                    Log.e("openConnection", "Error: Unable to connect to the server. " + e.getMessage());
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
            else
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            if (booleanPostExecute)
            {
                listGames = gson.fromJson(s, type);

                setGames();
            }
        }
    }

    private class AsyncTaskGetGrids extends AsyncTask<String, Void, String>
    {
        // fields

        private boolean booleanPostExecute, booleanSetStatus;

        // methods

        @Override
        protected String doInBackground(String... params)
        {
            if (isConnected())
            {
                HttpURLConnection httpURLConnection = null;
                StringBuilder stringBuilder = new StringBuilder();

                try
                {
                    URL url = new URL("http://battleship.pixio.com/api/v2/games/" + params[0] + "/boards?playerId=" + params[1]);
                    httpURLConnection = (HttpURLConnection)url.openConnection();

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        try (InputStream inputStream = httpURLConnection.getInputStream(); InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
                        {
                            String s;

                            while ((s = bufferedReader.readLine()) != null)
                            {
                                stringBuilder.append(s);
                            }

                            booleanPostExecute = true;
                        }
                    }
                    else
                    {
                        Log.e("responseCode", "Error " + httpURLConnection.getResponseCode() + ": Unable to connect to the server. " + httpURLConnection.getResponseMessage());
                    }
                }
                catch (Exception e)
                {
                    Log.e("openConnection", "Error: Unable to connect to the server. " + e.getMessage());
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
            else
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            if (booleanPostExecute)
            {
                Grids grids = gson.fromJson(s, Grids.class);

                List<Set<Integer>> cells = new ArrayList<>();
                cells.add(new HashSet<Integer>());
                cells.add(new HashSet<Integer>());

                List<Set<Integer>> hits = new ArrayList<>();
                hits.add(new HashSet<Integer>());
                hits.add(new HashSet<Integer>());

                List<Set<Integer>> misses = new ArrayList<>();
                misses.add(new HashSet<Integer>());
                misses.add(new HashSet<Integer>());

                List<Set<Integer>> ships = new ArrayList<>();
                ships.add(new HashSet<Integer>());
                ships.add(new HashSet<Integer>());

                for (Grids.Cell c : grids.getOpponentBoard())
                {
                    if (c.getStatus().equalsIgnoreCase("Hit"))
                    {
                        hits.get(0).add(c.getXPos() + c.getYPos() * intColumnsCount);
                    }
                    else if (c.getStatus().equalsIgnoreCase("Miss"))
                    {
                        misses.get(0).add(c.getXPos() + c.getYPos() * intColumnsCount);
                    }
                    else if (c.getStatus().equalsIgnoreCase("Ship"))
                    {
                        ships.get(0).add(c.getXPos() + c.getYPos() * intColumnsCount);
                    }
                    else
                    {
                        cells.get(0).add(c.getXPos() + c.getYPos() * intColumnsCount);
                    }
                }

                for (Grids.Cell c : grids.getPlayerBoard())
                {
                    if (c.getStatus().equalsIgnoreCase("Hit"))
                    {
                        hits.get(1).add(c.getXPos() + c.getYPos() * intColumnsCount);
                    }
                    else if (c.getStatus().equalsIgnoreCase("Miss"))
                    {
                        misses.get(1).add(c.getXPos() + c.getYPos() * intColumnsCount);
                    }
                    else if (c.getStatus().equalsIgnoreCase("Ship"))
                    {
                        ships.get(1).add(c.getXPos() + c.getYPos() * intColumnsCount);
                    }
                    else
                    {
                        cells.get(1).add(c.getXPos() + c.getYPos() * intColumnsCount);
                    }
                }

                fragmentGame.setGame(cells, hits, misses, ships);
            }
        }
    }

    private class AsyncTaskGetStatuses extends AsyncTask<String, Void, String>
    {
        // fields

        private boolean booleanPostExecute, booleanSetStatus;
        private String stringGameID;

        // methods

        @Override
        protected String doInBackground(String... params)
        {
            if (isConnected())
            {
                HttpURLConnection httpURLConnection = null;
                stringGameID = params[0];
                StringBuilder stringBuilder = new StringBuilder();

                try
                {
                    URL url = new URL("http://battleship.pixio.com/api/v2/games/" + stringGameID + "?playerId=" + params[1]);
                    httpURLConnection = (HttpURLConnection)url.openConnection();

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        try (InputStream inputStream = httpURLConnection.getInputStream(); InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
                        {
                            String s;

                            while ((s = bufferedReader.readLine()) != null)
                            {
                                stringBuilder.append(s);
                            }

                            booleanPostExecute = true;
                        }
                    }
                    else
                    {
                        Log.e("responseCode", "Error " + httpURLConnection.getResponseCode() + ": Unable to connect to the server. " + httpURLConnection.getResponseMessage());
                    }
                }
                catch (Exception e)
                {
                    Log.e("openConnection", "Error: Unable to connect to the server. " + e.getMessage());
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
            else
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            if (booleanPostExecute)
            {
                edu.utah.cs.cs4530.project4.model.Status status = gson.fromJson(s, edu.utah.cs.cs4530.project4.model.Status.class);

                if (status.getWinner().equalsIgnoreCase("In Progress"))
                {
                    if (stringGameID.equalsIgnoreCase(ActivityMain.this.stringGameID))
                    {
                        fragmentGame.setStatus(status.getIsYourTurn());

                        if (status.getIsYourTurn())
                        {
                            if (!mapStatuses.get(stringGameID).getIsYourTurn())
                                new AsyncTaskGetGrids().execute(stringGameID, mapPlayerIDs.get(stringGameID));
                        }

                    }

                    synchronized (mapStatuses)
                    {
                        mapStatuses.put(stringGameID, status);
                    }
                }
                else
                {
                    handler.removeCallbacks(runnableGetGames);
                    handler.post(runnableGetGames);

                    synchronized (mapStatuses)
                    {
                        if (mapStatuses.containsKey(stringGameID))
                        {
                            mapStatuses.remove(stringGameID);
                        }
                    }
                }
            }
        }
    }

    private class AsyncTaskJoinGame extends AsyncTask<String, Void, String>
    {
        // fields

        private boolean booleanPostExecute;
        private GameIDPlayerName gameIDPlayerName;

        // methods

        @Override
        protected String doInBackground(String... params)
        {
            if (isConnected())
            {
                gameIDPlayerName = new GameIDPlayerName(params[0], params[1]);
                HttpURLConnection httpURLConnection = null;
                StringBuilder stringBuilder = new StringBuilder();

                try
                {
                    URL url = new URL("http://battleship.pixio.com/api/v2/lobby/" + params[0]);

                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("PUT");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");

                    try (OutputStream outputStream = httpURLConnection.getOutputStream(); OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream); BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);)
                    {
                        bufferedWriter.write(gson.toJson(gameIDPlayerName));
                        bufferedWriter.flush();
                    }

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        try (InputStream inputStream = httpURLConnection.getInputStream(); InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
                        {
                            String s;

                            while ((s = bufferedReader.readLine()) != null)
                            {
                                stringBuilder.append(s);
                            }

                            booleanPostExecute = true;
                        }
                    }
                    else
                    {
                        Log.e("responseCode", "Error " + httpURLConnection.getResponseCode() + ": Unable to connect to the server. " + httpURLConnection.getResponseMessage());
                    }
                }
                catch (Exception e)
                {
                    Log.e("openConnection", "Error: Unable to connect to the server. " + e.getMessage());
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
            else
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            if (booleanPostExecute)
            {
                handler.removeCallbacks(runnableGetGames);

                booleanSetSelection = true;
                PlayerID playerID = gson.fromJson(s, PlayerID.class);

                if (intGameSet == battleship.WAITING || intGameSet == battleship.OVER)
                {
                    intGameSet = battleship.IN_PROGRESS;
                }

                stringGameID = gameIDPlayerName.getGameID();

                new AsyncTaskGetGrids().execute(gameIDPlayerName.getGameID(), playerID.getPlayerID());
                handler.post(runnableGetGames);
                listFragmentMenu.setGame(gameIDPlayerName.getGameID());
                mapPlayerIDs.put(gameIDPlayerName.getGameID(), playerID.getPlayerID());
                mapStatuses.put(gameIDPlayerName.getGameID(), null);
            }
        }
    }

    private class AsyncTaskNewGame extends AsyncTask<String, Void, String>
    {
        // fields

        private boolean booleanPostExecute;

        // methods

        @Override
        protected String doInBackground(String... params)
        {
            if (isConnected())
            {
                HttpURLConnection httpURLConnection = null;
                StringBuilder stringBuilder = new StringBuilder();

                try
                {
                    URL url = new URL("http://battleship.pixio.com/api/v2/lobby");

                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");

                    try (OutputStream outputStream = httpURLConnection.getOutputStream(); OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream); BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);)
                    {
                        bufferedWriter.write(gson.toJson(new GameNamePlayerName(params[0], params[1])));
                        bufferedWriter.flush();
                    }

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        try (InputStream inputStream = httpURLConnection.getInputStream(); InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
                        {
                            String s;

                            while ((s = bufferedReader.readLine()) != null)
                            {
                                stringBuilder.append(s);
                            }

                            booleanPostExecute = true;
                        }
                    }
                    else
                    {
                        Log.e("responseCode", "Error " + httpURLConnection.getResponseCode() + ": Unable to connect to the server. " + httpURLConnection.getResponseMessage());
                    }
                }
                catch (Exception e)
                {
                    Log.e("openConnection", "Error: Unable to connect to the server. " + e.getMessage());
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
            else
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            if (booleanPostExecute)
            {
                handler.removeCallbacks(runnableGetGames);

                booleanSetSelection = true;
                GameIDPlayerID gameIDPlayerID = gson.fromJson(s, GameIDPlayerID.class);

                if (intGameSet == battleship.IN_PROGRESS || intGameSet == battleship.OVER)
                {
                    intGameSet = battleship.WAITING;
                }

                stringGameID = gameIDPlayerID.getGameID();

                handler.post(runnableGetGames);
                listFragmentMenu.setGame(gameIDPlayerID.getGameID());
                mapPlayerIDs.put(gameIDPlayerID.getGameID(), gameIDPlayerID.getPlayerID());
                mapStatuses.put(gameIDPlayerID.getGameID(), null);
            }
        }
    }

    private class AsyncTaskShoot extends AsyncTask<String, Void, String>
    {
        // fields

        private boolean booleanPostExecute;
        private PlayerIDCell playerIDCell;

        // methods

        @Override
        protected String doInBackground(String... params)
        {
            if (isConnected())
            {
                int i = Integer.parseInt(params[2]);
                int j = Integer.parseInt(params[3]);
                Log.i("Heh", "" + params[0] + " " + params[1] + " " + i + " " + j);
                playerIDCell = new PlayerIDCell(params[1], i, j);
                HttpURLConnection httpURLConnection = null;
                StringBuilder stringBuilder = new StringBuilder();

                try
                {
                    URL url = new URL("http://battleship.pixio.com/api/v2/games/" + params[0]);

                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");

                    try (OutputStream outputStream = httpURLConnection.getOutputStream(); OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream); BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);)
                    {
                        bufferedWriter.write(gson.toJson(playerIDCell));
                        bufferedWriter.flush();
                    }

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        try (InputStream inputStream = httpURLConnection.getInputStream(); InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
                        {
                            String s;

                            while ((s = bufferedReader.readLine()) != null)
                            {
                                stringBuilder.append(s);
                            }

                            booleanPostExecute = true;
                        }
                    }
                    else
                    {
                        Log.e("responseCode", "Error " + httpURLConnection.getResponseCode() + ": Unable to connect to the server. " + httpURLConnection.getResponseMessage());
                    }
                }
                catch (Exception e)
                {
                    Log.e("openConnection", "Error: Unable to connect to the server. " + e.getMessage());
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
            else
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            if (booleanPostExecute)
            {
                Shot shot = gson.fromJson(s, Shot.class);

                fragmentGame.addShot(shot.getHit(), playerIDCell.getColumn() + playerIDCell.getRow() * intColumnsCount);
            }
        }
    }
}