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
import java.util.Arrays;
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
import edu.utah.cs.cs4530.project4.model.Cells;
import edu.utah.cs.cs4530.project4.model.Game;
import edu.utah.cs.cs4530.project4.model.GameIDPlayerID;
import edu.utah.cs.cs4530.project4.model.GameIDPlayerName;
import edu.utah.cs.cs4530.project4.model.GameNamePlayerName;
import edu.utah.cs.cs4530.project4.model.Player;
import edu.utah.cs.cs4530.project4.model.Summary;
import edu.utah.cs.cs4530.project4.model.PlayerID;
import edu.utah.cs.cs4530.project4.model.PlayerIDCell;
import edu.utah.cs.cs4530.project4.model.Shot;
import edu.utah.cs.cs4530.project4.model.Turn;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static edu.utah.cs.cs4530.project4.view.LinearLayoutGrid.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ActivityMain extends AppCompatActivity implements OnGameClickListener, OnGameSetSelectListener, OnNewGameClickListener, OnShootListener, OnStartClickListener
{
    // fields

    private final Battleship battleship = Battleship.getBattleship();
    private boolean booleanGame, booleanGetCells, booleanSetSelection, booleanStart, booleanStatus, booleanSummary, booleanTablet, booleanTurn;
    private FragmentGame fragmentGame;
    private FragmentManager fragmentManager;
    private FragmentMenu fragmentMenu;
    private FragmentStart fragmentStart;
    private FragmentSummary fragmentSummary;
    private FrameLayout frameLayoutMenu, frameLayoutGame;
    private final Gson gson = new Gson();
    private final Handler handler = new Handler();
    private final int intColumnsCount = battleship.getColumnsCount();
    private int intGameSet, intMargin, intPadding;
    private final int intRowsCount = battleship.getRowsCount();
    private List<Game> listGames;
    private List<Integer> listPlayers = battleship.getPlayers();
    private List<Set<Integer>> listHits, listMisses, listShips;
    private ListFragmentMenu listFragmentMenu;
    private Map<String, Player> mapPlayers;
    private Map<String, Summary> mapSummaries;
    private Map<String, Turn> mapTurns;
    private Runnable runnableGetGames = new Runnable()
    {
        @Override
        public void run()
        {
            new AsyncTaskGetGames().execute();
            handler.postDelayed(runnableGetGames, 5000);
        }
    };
    private Runnable runnableGetSummaries = new Runnable()
    {
        @Override
        public void run()
        {
            synchronized (mapSummaries)
            {
                for (String s : mapSummaries.keySet())
                {
                    new AsyncTaskGetSummary().execute(s);
                }

                handler.postDelayed(runnableGetSummaries, 500);
            }
        }
    };
    private Runnable runnableGetTurns = new Runnable()
    {
        @Override
        public void run()
        {
            synchronized (mapTurns)
            {
                for (String s : mapTurns.keySet())
                {
                    if (mapPlayers.containsKey(s))
                    {
                        new AsyncTaskGetTurn().execute(s, mapPlayers.get(s).getPlayerID());
                    }
                }

                handler.postDelayed(runnableGetTurns, 500);
            }
        }
    };
    private final String stringFragmentGame = "fragmentGame";
    private final String stringFragmentMenu = "fragmentMenu";
    private final String stringFragmentStart = "fragmentStart";
    private final String stringFragmentSummary = "fragmentSummary";
    private String stringGameID, stringGameName, stringOpponent, stringPlayer, stringPlayerName, stringWinner;
    private final String stringListFragmentMenu = "listFragmentMenu";
    private final String stringPost = "POST";
    private final String stringPut = "PUT";
    private final String stringPlayers = "Players";
    private final String stringSummaries = "Summaries";
    private final String stringTurns = "Turns";
    private final String stringURL = "http://battleship.pixio.com/api/v2/";
    private final Type typeCells = new TypeToken<List<Set<Integer>>>(){}.getType();
    private final Type typeGames = new TypeToken<List<Game>>(){}.getType();

    // methods

    private void deserialize()
    {
        try (FileInputStream fileInputStream = getApplicationContext().openFileInput(stringPlayers); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream))
        {
            mapPlayers = (Map<String, Player>)objectInputStream.readObject();
        }
        catch (Exception e)
        {
            mapPlayers = new HashMap<>();

            Log.e("deserialize", "Error: Unable to read the players. " + e.getMessage());
        }

        try (FileInputStream fileInputStream = getApplicationContext().openFileInput(stringSummaries); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream))
        {
            mapSummaries = (Map<String, Summary>)objectInputStream.readObject();
        }
        catch (Exception e)
        {
            mapSummaries = new HashMap<>();

            Log.e("deserialize", "Error: Unable to read the summaries. " + e.getMessage());
        }

        try (FileInputStream fileInputStream = getApplicationContext().openFileInput(stringTurns); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream))
        {
            mapTurns = (Map<String, Turn>)objectInputStream.readObject();
        }
        catch (Exception e)
        {
            mapTurns = new HashMap<>();

            Log.e("deserialize", "Error: Unable to read the player names. " + e.getMessage());
        }
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

    @Override
    public void onBackPressed()
    {
        if (booleanGame || booleanSummary)
        {
            super.onBackPressed();

            booleanGame = booleanSummary = false;
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

        listFragmentMenu.setGame("");
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
            booleanGetCells = savedInstanceState.getBoolean("booleanGetCells");
            booleanSetSelection = savedInstanceState.getBoolean("booleanSetSelection");
            booleanStart = savedInstanceState.getBoolean("booleanStart");
            booleanStatus = savedInstanceState.getBoolean("booleanStatus");
            booleanSummary = savedInstanceState.getBoolean("booleanSummary");
            booleanTurn = savedInstanceState.getBoolean("booleanTurn");
            intGameSet = savedInstanceState.getInt("intGameSet");
            listGames = gson.fromJson(savedInstanceState.getString("listGames"), typeGames);
            listHits = gson.fromJson(savedInstanceState.getString("listHits"), typeCells);
            listMisses = gson.fromJson(savedInstanceState.getString("listMisses"), typeCells);
            listShips = gson.fromJson(savedInstanceState.getString("listShips"), typeCells);
            stringGameID = savedInstanceState.getString("stringGameID");
            stringGameName = savedInstanceState.getString("stringGameName");
            stringOpponent = savedInstanceState.getString("stringOpponent");
            stringPlayer = savedInstanceState.getString("stringPlayer");
            stringPlayerName = savedInstanceState.getString("stringPlayerName");
            stringWinner = savedInstanceState.getString("stringWinner");
        }
        else
        {
            listGames = new ArrayList<>();
            listHits = new ArrayList<>();
            listMisses = new ArrayList<>();
            listShips = new ArrayList<>();

            for (int i : listPlayers)
            {
                listHits.add(new HashSet<Integer>());
                listMisses.add(new HashSet<Integer>());
                listShips.add(new HashSet<Integer>());
            }

            stringGameID = stringGameName = stringOpponent = stringPlayer = stringPlayerName = stringWinner = "";
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

        intMargin = (displayMetrics.heightPixels < displayMetrics.widthPixels ? displayMetrics.heightPixels : displayMetrics.widthPixels) / 10;
        intPadding = intMargin / 5;
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
        fragmentGame.setPlayers(new ArrayList<>(battleship.getPlayers()));
        fragmentGame.setRowsCount(intRowsCount);

        fragmentMenu = fragmentManager.findFragmentByTag(stringFragmentMenu) == null ? new FragmentMenu() : (FragmentMenu)fragmentManager.findFragmentByTag(stringFragmentMenu);
        fragmentMenu.setMargin(intMargin);

        fragmentStart = fragmentManager.findFragmentByTag(stringFragmentStart) == null ? new FragmentStart() : (FragmentStart)fragmentManager.findFragmentByTag(stringFragmentStart);
        fragmentStart.setMargin(intMargin);

        fragmentSummary = fragmentManager.findFragmentByTag(stringFragmentSummary) == null ? new FragmentSummary() : (FragmentSummary)fragmentManager.findFragmentByTag(stringFragmentSummary);
        fragmentSummary.setMargin(intMargin);

        listFragmentMenu = fragmentManager.findFragmentByTag(stringListFragmentMenu) == null ? new ListFragmentMenu() : (ListFragmentMenu)fragmentManager.findFragmentByTag(stringListFragmentMenu);
        listFragmentMenu.setGames(listGames);
        listFragmentMenu.setPadding(intPadding);

        if (savedInstanceState == null)
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(frameLayoutGame.getId(), fragmentGame, stringFragmentGame);
            fragmentTransaction.add(frameLayoutGame.getId(), fragmentStart, stringFragmentStart);
            fragmentTransaction.add(frameLayoutGame.getId(), fragmentSummary, stringFragmentSummary);

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

        handler.post(runnableGetGames);
        handler.post(runnableGetSummaries);
        handler.post(runnableGetTurns);
        setContentView(linearLayout);
        setFragments();
        setGame();
        setLayoutParams();
    }

    @Override
    public void onGameSetSelect(int gameSet)
    {
        booleanSetSelection = true;
        intGameSet = gameSet;

        setGames();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putBoolean("booleanGame", booleanGame);
        outState.putBoolean("booleanGetCells", booleanGetCells);
        outState.putBoolean("booleanSetSelection", booleanSetSelection);
        outState.putBoolean("booleanStart", booleanStart);
        outState.putBoolean("booleanStatus", booleanStatus);
        outState.putBoolean("booleanSummary", booleanSummary);
        outState.putBoolean("booleanTurn", booleanTurn);
        outState.putInt("intGameSet", intGameSet);
        outState.putString("listGames", gson.toJson(listGames, typeGames));
        outState.putString("listHits", gson.toJson(listHits, typeCells));
        outState.putString("listMisses", gson.toJson(listMisses, typeCells));
        outState.putString("listShips", gson.toJson(listShips, typeCells));
        outState.putString("stringGameID", stringGameID);
        outState.putString("stringGameName", stringGameName);
        outState.putString("stringOpponent", stringOpponent);
        outState.putString("stringPlayer", stringPlayer);
        outState.putString("stringPlayerName", stringPlayerName);
        outState.putString("stringWinner", stringWinner);

        super.onSaveInstanceState(outState);
    }

    private void serialize()
    {
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), stringPlayers)); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream))
        {
            objectOutputStream.writeObject(mapPlayers);
        }
        catch(Exception e)
        {
            Log.e("serialize", "Error: Unable to write the players. " + e.getMessage());
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), stringSummaries)); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream))
        {
            objectOutputStream.writeObject(mapSummaries);
        }
        catch(Exception e)
        {
            Log.e("serialize", "Error: Unable to write the summaries. " + e.getMessage());
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), stringTurns)); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream))
        {
            objectOutputStream.writeObject(mapTurns);
        }
        catch(Exception e)
        {
            Log.e("serialize", "Error: Unable to write the turns. " + e.getMessage());
        }
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

    private void setGame()
    {
        if (booleanGame)
        {
            fragmentGame.setGame(stringOpponent, stringPlayer, stringWinner, booleanStatus, booleanTurn, listHits, listMisses, listShips);
        }
    }

    private void setGames()
    {
        List<Game> games = new ArrayList<>();

        if (intGameSet == battleship.MY_GAMES)
        {
            for (Game g : listGames)
            {
                if (mapPlayers.containsKey(g.getGameID()))
                {
                    games.add((g));
                }
            }
        }
        else if (intGameSet == battleship.WAITING || intGameSet == battleship.IN_PROGRESS || intGameSet == battleship.GAME_OVER)
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
    public void onGameClick(final Game game)
    {
        if (mapPlayers.containsKey(game.getGameID()))
        {
            if (!game.getGameID().equalsIgnoreCase(stringGameID) && game.getStatus() == battleship.IN_PROGRESS || game.getStatus() == battleship.GAME_OVER)
            {
                booleanGame = true;
                stringGameID = game.getGameID();

                new AsyncTaskGetCells().execute(stringGameID, mapPlayers.get(stringGameID).getPlayerID());
                new AsyncTaskGetTurn().execute(stringGameID, mapPlayers.get(stringGameID).getPlayerID());
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

                    new AsyncTaskJoinGame().execute(game.getGameID(), stringPlayerName);
                    dialog.dismiss();
                }
            });
            alertDialog.setView(linearLayout);
            alertDialog.show();
        }
        else
        {
            booleanGame = false;
            booleanSummary = true;



            stringGameID = game.getGameID();

            new AsyncTaskGetSummary().execute(game.getGameID());
            listFragmentMenu.setGame(stringGameID);
            setFragments();
        }
    }



/*    private String getRowColumnString(int cell)
    {
        int row = cell / intColumnsCount + 1;

        Stack<Integer> stack = new Stack<>();
        StringBuilder stringBuilder = new StringBuilder();

        while (row > 0)
        {
            stack.push(row % 26);

            row /= 26;
        }

        while (stack.size() > 0)
        {
            stringBuilder.append((char)(stack.pop() + 64));
        }

        stringBuilder.append(cell % intColumnsCount + 1);

        return stringBuilder.toString();
    }*/






    @Override
    public void onShoot(final int cell)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Shots Fired");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                new AsyncTaskShoot().execute(stringGameID, mapPlayers.get(stringGameID).getPlayerID(), Integer.toString(cell));
                dialog.dismiss();
            }
        });
        alertDialog.setTitle("Shot");
        alertDialog.show();
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






































    private class AsyncTaskJoinGame extends AsyncTask<String, Void, Void>
    {
        // fields

        private GameIDPlayerName gameIDPlayerName;
        private String stringGameID;

        // methods

        @Override
        protected Void doInBackground(String... params)
        {
            stringGameID = params[0];
            gameIDPlayerName = new GameIDPlayerName(stringGameID, params[1]);

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    new AsyncTaskRequest()
                    {
                        @Override
                        protected void onPostExecute(String s)
                        {
                            processResponse(s);
                        }
                    }.execute(stringURL + "lobby/" + stringGameID, stringPut, gameIDPlayerName);
                }
            });

            return null;
        }

        private void processResponse(String s)
        {
            handler.removeCallbacks(runnableGetGames);

            booleanSetSelection = true;
            PlayerID playerID = gson.fromJson(s, PlayerID.class);

            if (intGameSet == battleship.WAITING || intGameSet == battleship.GAME_OVER)
            {
                intGameSet = battleship.IN_PROGRESS;
            }

            ActivityMain.this.stringGameID = stringGameID;

            new AsyncTaskGetSummary().execute(stringGameID);
            handler.post(runnableGetGames);
            listFragmentMenu.setGame(stringGameID);

            synchronized (mapPlayers)
            {
                mapPlayers.put(stringGameID, new Player(1, playerID.getPlayerID()));
            }

            synchronized (mapSummaries)
            {
                mapSummaries.put(stringGameID, null);
            }
        }
    }

    private class AsyncTaskShoot extends AsyncTask<String, Void, Void>
    {
        // fields

        private PlayerIDCell playerIDCell;
        private String stringGameID;

        // methods

        @Override
        protected Void doInBackground(String... params)
        {
            playerIDCell = new PlayerIDCell(params[1], Integer.parseInt(params[2]));
            stringGameID = params[0];

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    new AsyncTaskRequest()
                    {
                        @Override
                        protected void onPostExecute(String s)
                        {
                            processResponse(s);
                        }
                    }.execute(stringURL + "games/" + stringGameID, stringPost, playerIDCell);
                }
            });

            return null;
        }

        private void processResponse(String s)
        {
            booleanTurn = false;
            Shot shot = gson.fromJson(s, Shot.class);

            fragmentGame.addShot(shot.getHit(), playerIDCell.getCell());
        }

            /*
            if (isConnected())
            {
                playerIDCell = new PlayerIDCell(params[1], Integer.parseInt(params[2]), Integer.parseInt(params[3]));
                HttpURLConnection httpURLConnection = null;
                StringBuilder stringBuilder = new StringBuilder();

                try
                {
                    URL url = new URL("games/" + params[0]);

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
            */

    }




























    private class AsyncTaskGetSummary extends AsyncTask<String, Void, Void>
    {
        // methods

        @Override
        protected Void doInBackground(final String... params)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    new AsyncTaskRequest()
                    {
                        @Override
                        protected void onPostExecute(String s)
                        {
                            processResponse(s);
                        }
                    }.execute(stringURL + "lobby/" + params[0], null, null);
                }
            });

            return null;
        }

        private void processResponse(String s)
        {
            // handle when new game and waiting for player

            Summary summary = gson.fromJson(s, Summary.class);
            String gameID = summary.getGameID();

            if (mapPlayers.containsKey(gameID))
            {
                if (summary.getStatus() != battleship.WAITING)
                {
                    synchronized (mapSummaries)
                    {
                        if (mapSummaries.containsKey(gameID))
                        {
                            mapSummaries.remove(gameID);
                        }
                    }

                    synchronized (mapPlayers)
                    {
                        mapPlayers.get(gameID).setPlayerNames(Arrays.asList(summary.getPlayer1ID(), summary.getPlayer2ID()));
                    }

                    if (gameID.equalsIgnoreCase(stringGameID))
                    {
                        booleanStatus = booleanTurn = false;
                        stringOpponent = mapPlayers.get(gameID).getOpponentName();
                        stringPlayer = mapPlayers.get(gameID).getPlayerName();

                        new AsyncTaskGetTurn().execute(gameID, mapPlayers.get(gameID).getPlayerID());
                    }

                    synchronized (mapTurns)
                    {
                        mapTurns.put(gameID, null);
                    }
                }
            }
            else
            {
                fragmentSummary.setText(summary.getGameName(), summary.getPlayer1ID(), summary.getPlayer2ID(), summary.getShots(), summary.getStatus(summary.getStatus()), summary.getWinner());
            }
        }
    }

    private class AsyncTaskGetTurn extends AsyncTask<String, Void, Void>
    {
        // fields

        private String stringGameID, stringPlayerID;

        // methods

        @Override
        protected Void doInBackground(String... params)
        {
            stringGameID = params[0];
            stringPlayerID = params[1];

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    new AsyncTaskRequest()
                    {
                        @Override
                        protected void onPostExecute(String s)
                        {
                            processResponse(s);
                        }
                    }.execute(stringURL + "games/" + stringGameID + "?playerId=" + stringPlayerID, null, null);
                }
            });

            return null;
        }

        private void processResponse(String s)
        {
            Turn turn = gson.fromJson(s, Turn.class);
            boolean status = turn.getWinner().equalsIgnoreCase("In Progress");

            if (status)
            {
                synchronized (mapTurns)
                {
                    mapTurns.put(stringGameID, turn);
                }
            }
            else
            {
                synchronized (mapTurns)
                {
                    if (mapTurns.containsKey(stringGameID))
                    {
                        mapTurns.remove(stringGameID);
                    }
                }

                synchronized (listGames)
                {
                    for (Game g : listGames)
                    {
                        if (g.getGameID().equalsIgnoreCase(stringGameID) && g.getStatus() != battleship.GAME_OVER)
                        {
                            g.setStatus(false);
                            setGames();
                        }
                    }
                }
            }

            if (stringGameID.equalsIgnoreCase(ActivityMain.this.stringGameID))
            {
                if (!status || (!booleanTurn && turn.getTurn()))
                {
                    booleanGetCells = true;
                }

                booleanStatus = status;
                booleanTurn = turn.getTurn();
                stringWinner = turn.getWinner();
            }

            if(booleanGetCells)
            {
                booleanGetCells = false;

                new AsyncTaskGetCells().execute(stringGameID, stringPlayerID);
            }
        }
    }


































    // classes

    private class AsyncTaskGetCells extends AsyncTask<String, Void, Void>
    {
        // methods

        @Override
        protected Void doInBackground(final String... params)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    new AsyncTaskRequest()
                    {
                        @Override
                        protected void onPostExecute(String s)
                        {
                            processResponse(s);
                        }
                    }.execute(stringURL + "games/" + params[0] + "/boards?playerId=" + params[1], null, null);
                }
            });

            return null;
        }

        private void processResponse(String s)
        {
            Cells cells = gson.fromJson(s, Cells.class);

            for (int i : battleship.getPlayers())
            {
                listHits.get(i).clear();
                listMisses.get(i).clear();
                listShips.get(i).clear();

                for (Cells.Cell c : cells.getCells(i))
                {
                    if (c.getCellSet() == battleship.HIT)
                    {
                        listHits.get(i).add(c.getCell());
                    }
                    else if (c.getCellSet() == battleship.MISS)
                    {
                        listMisses.get(i).add(c.getCell());
                    }
                    else if (c.getCellSet() == battleship.SHIP)
                    {
                        listShips.get(i).add(c.getCell());
                    }
                }
            }

            fragmentGame.setGame(stringOpponent, stringPlayer, stringWinner, booleanStatus, booleanTurn, listHits, listMisses, listShips);
        }
    }

    private class AsyncTaskGetGames extends AsyncTask<String, Void, Void>
    {
        // methods

        @Override
        protected Void doInBackground(String... params)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    new AsyncTaskRequest()
                    {
                        @Override
                        protected void onPostExecute(String s)
                        {
                            processResponse(s);
                        }
                    }.execute(stringURL + "lobby", null, null);
                }
            });

            return null;
        }

        private void processResponse(String s)
        {
            listGames = gson.fromJson(s, typeGames);

            setGames();
        }
    }

    private class AsyncTaskNewGame extends AsyncTask<String, Void, Void>
    {
        // fields

        private GameNamePlayerName gameNamePlayerName;

        // methods

        @Override
        protected Void doInBackground(String... params)
        {
            gameNamePlayerName = new GameNamePlayerName(params[0], params[1]);

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    new AsyncTaskRequest()
                    {
                        @Override
                        protected void onPostExecute(String s)
                        {
                            processResponse(s);
                        }
                    }.execute(stringURL + "lobby/", stringPost, gameNamePlayerName);
                }
            });

            return null;
        }

        private void processResponse(String s)
        {
            handler.removeCallbacks(runnableGetGames);
            handler.removeCallbacks(runnableGetSummaries);

            booleanGame = booleanSetSelection = true;
            booleanSummary = false;
            GameIDPlayerID gameIDPlayerID = gson.fromJson(s, GameIDPlayerID.class);

            if (intGameSet == battleship.IN_PROGRESS || intGameSet == battleship.GAME_OVER)
            {
                intGameSet = battleship.WAITING;
            }

            stringGameID = gameIDPlayerID.getGameID();

            synchronized (mapPlayers)
            {
                mapPlayers.put(gameIDPlayerID.getGameID(), new Player(0, gameIDPlayerID.getPlayerID()));
            }

            synchronized (mapSummaries)
            {
                mapSummaries.put(gameIDPlayerID.getGameID(), null);
            }

            listFragmentMenu.setGame(gameIDPlayerID.getGameID());
            handler.post(runnableGetGames);
            handler.post(runnableGetSummaries);
        }
    }

    private class AsyncTaskRequest extends AsyncTask<Object, Void, String>
    {
        // methods

        @Override
        protected String doInBackground(Object... params)
        {
            NetworkInfo networkInfo = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected())
            {
                HttpURLConnection httpURLConnection = null;
                StringBuilder stringBuilder = new StringBuilder();

                try
                {
                    URL url = new URL(params[0].toString());
                    httpURLConnection = (HttpURLConnection)url.openConnection();

                    if (params[1] != null && params[2] != null)
                    {
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setRequestMethod(params[1].toString());
                        httpURLConnection.setRequestProperty("Content-Type", "application/json");

                        try (OutputStream outputStream = httpURLConnection.getOutputStream(); OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream); BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);)
                        {
                            bufferedWriter.write(gson.toJson(params[2]));
                            bufferedWriter.flush();
                        }
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

                            return stringBuilder.toString();
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
            }

            return null;
        }
    }
}