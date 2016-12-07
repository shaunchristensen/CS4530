/**
 * Author:     Shaun Christensen
 * Course:     CS 4530 - Mobile Application Programming: Android
 * Date:       2016.11.18
 * Assignment: Project F - Lights Out
 * Credits:    Alegrerya Sans Black Italic Font - http://www.1001fonts.com/alegreya-sans-font.html
 *             Built Titling Light Font - http://www.dafont.com/built-titling.font
 *             Tiger Electronics Logo - https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Tiger_Electronics_logo.svg/2000px-Tiger_Electronics_logo.svg.png
 */

package edu.utah.cs.cs4530.lightsout.controller;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.utah.cs.cs4530.lightsout.R;
import edu.utah.cs.cs4530.lightsout.model.LightsOut;
import edu.utah.cs.cs4530.lightsout.model.Puzzle;
import edu.utah.cs.cs4530.lightsout.view.Cell;

import static android.graphics.Typeface.createFromAsset;
import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class AppCompatActivityLightsOut extends AppCompatActivity implements OnCompletionListener, OnGlobalLayoutListener, OnTouchListener
{
    // fields

    private boolean booleanHandler, booleanHelp, booleanMediaPlayer, booleanMode, booleanOff, booleanOn, booleanSelect, booleanSound, booleanStart;
    private Button buttonHelp, buttonOnOff, buttonSelect, buttonSound, buttonStart;
    private Gson gson = new Gson();
    private Handler handler = new Handler();
    private final int intBlinkingLightsCount = 4;
    private final int intBlinkingLightsDuration = 2000;
    private final int intCellsCount = LightsOut.getCellsCount();
    private final int intColumnsCount = LightsOut.getColumnsCount();
    private int intMediaPlayerID, intMode, intMovesCount, intPuzzle, intPuzzlesCount, intSoundPoolID;
    private final int intModesCount = LightsOut.getModesCount();
    private final int intMovesBuffer = LightsOut.getMovesBuffer();
    private final int intRowsCount = LightsOut.getRowsCount();
    private final int intShowOfLightsDuration = 4000;
    private List<Cell> listCells;
    private List<Integer> listHints;
    private List<View> listButtons, listTextViews, listViews;;
    private MediaPlayer mediaPlayer;
    private Puzzle puzzle;
    private Random random;
    private Set<Integer> setCells;
    private SoundPool soundPool;
    private String stringPuzzle = "Puzzle";

    // methods

    private Puzzle createPuzzle()
    {
        List<Integer> cells = new ArrayList<>();
        random = new Random();

        for (int i = 0; i < intCellsCount; i++)
        {
            cells.add(i);
        }

        Collections.shuffle(cells, random);

        return new Puzzle(0, new ArrayList<Integer>(), new HashSet<>(cells.subList(0, random.nextInt(intCellsCount))));
    }

    private Runnable runnableBlinkingLights(final Collection<Integer> cells)
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                blinkingLights(cells);
            }
        };
    }

    private Runnable runnablePlaySound()
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                playSound();
            }
        };
    };

    private Runnable runnableSetBooleanHandler()
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                booleanHandler = false;
            }
        };
    };

    private Runnable runnableStartPuzzle()
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                startPuzzle();
            }
        };
    }

    private Runnable runnableToggleCells(final Collection<Integer> cells)
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                toggleCells(cells);
            }
        };
    }

    private Runnable runnableToggleCells(final int cell)
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                toggleCells(cell);
            }
        };
    }

    private void blinkingLights(Collection<Integer> cells)
    {
        int delay = 0;
        int offset = intBlinkingLightsDuration / (intBlinkingLightsCount * 2);

        for (int i = 0; i < intBlinkingLightsCount; i++)
        {
            handler.postDelayed(runnableToggleCells(cells), delay);
            handler.postDelayed(runnableToggleCells(cells), delay + offset);

            delay += offset * 2;
        }
    }

    private void deserialize()
    {
        try (FileInputStream fileInputStream = getApplicationContext().openFileInput(stringPuzzle); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream))
        {
            LightsOut.setPuzzle(objectInputStream.readInt());

            intPuzzlesCount = LightsOut.getPuzzlesCount();
        }
        catch (Exception e)
        {
            Log.e("deserialize", "Error: Unable to read the puzzle. " + e.getMessage());
        }

        try (InputStream inputStream = getResources().openRawResource(R.raw.puzzles); InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
        {
            String s;
            StringBuilder stringBuilder = new StringBuilder();

            while ((s = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(s);
            }

            LightsOut.setPuzzles((List<Puzzle>)gson.fromJson(stringBuilder.toString(), new TypeToken<List<Puzzle>>(){}.getType()));
        }
        catch (Exception e)
        {
            Log.e("deserialize", "Error: Unable to read the puzzles. " + e.getMessage());
        }
    }

    private void flickerCells()
    {
        if (booleanSelect && booleanSound)
        {
            Set<Integer> cells = new HashSet<>(setCells);

            playSound();
            toggleCells(setCells);
            handler.postDelayed(runnableToggleCells(cells), 1);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        booleanMediaPlayer = false;

        mediaPlayer.setOnCompletionListener(null);
        mediaPlayer.release();

        if (intMediaPlayerID == R.raw.confirm)
        {
            onCompletionConfirm();
        }
        else if (intMediaPlayerID == R.raw.create)
        {
            onCompletionCreate();
        }
        else if (intMediaPlayerID == R.raw.on)
        {
            onCompletionOn();
        }
        else if (intMediaPlayerID == R.raw.solve)
        {
            onCompletionSolve();
        }
    }

    private void onCompletionConfirm()
    {
        if (intMode == 0)
        {
            if (intPuzzle < intCellsCount)
            {
                blinkingLights(Arrays.asList(intPuzzle));
            }
            else
            {
                toggleCell(intCellsCount - 1);
                blinkingLights(Arrays.asList(intPuzzle - intCellsCount));
                handler.postDelayed(runnableToggleCells(Arrays.asList(intCellsCount - 1)), intBlinkingLightsDuration);
            }

            handler.postDelayed(runnableStartPuzzle(), intBlinkingLightsDuration);
            handler.postDelayed(runnableSetBooleanHandler(), intBlinkingLightsDuration);
        }
        else if (intMode == 1)
        {
            startPuzzle();
        }
    }

    private void onCompletionCreate()
    {
        startPuzzle();
    }

    private void onCompletionOn()
    {
        showOfLights();
        handler.postDelayed(runnableToggleCells(Arrays.asList(7, 11, 12, 13, 17)), intShowOfLightsDuration);
        handler.postDelayed(runnableSetBooleanHandler(), intShowOfLightsDuration);
    }

    private void onCompletionSolve()
    {
        if (intMode == 0)
        {
            if (intMovesCount > puzzle.getMoves() + intMovesBuffer)
            {
                blinkingLights(Arrays.asList(0, 4, 6, 8, 12, 16, 18, 20, 24));
                handler.postDelayed(runnableBlinkingLights(Arrays.asList(intPuzzle)), intBlinkingLightsDuration);
                handler.postDelayed(runnableStartPuzzle(), intBlinkingLightsDuration * 2);
                handler.postDelayed(runnableSetBooleanHandler(), intBlinkingLightsDuration * 2);
            }
            else
            {
                int duration;

                if (intMovesCount > puzzle.getMoves())
                {
                    duration = intShowOfLightsDuration;
                    Set<Integer> cells = new HashSet<>();

                    for (int i = 0; i < intMovesCount - puzzle.getMoves(); i++)
                    {
                        cells.add(i);
                    }

                    blinkingLights(cells);
                }
                else
                {
                    duration = intBlinkingLightsDuration;

                    showOfLights();
                }

                if (intPuzzle < intPuzzlesCount - 1)
                {
                    intPuzzle++;
                    puzzle = LightsOut.getPuzzle(intPuzzle);

                    if (intPuzzle > LightsOut.getPuzzle())
                    {
                        LightsOut.setPuzzle(intPuzzle);
                        serialize();
                    }

                    handler.postDelayed(runnableBlinkingLights(Arrays.asList(intPuzzle)), duration);
                    handler.postDelayed(runnableStartPuzzle(), duration + intBlinkingLightsDuration);
                    handler.postDelayed(runnableSetBooleanHandler(), duration + intBlinkingLightsDuration);
                }
                else
                {
                    booleanMode = booleanSelect = booleanStart = false;

                    handler.postDelayed(runnableToggleCells(Arrays.asList(7, 11, 12, 13, 7)), duration);
                    handler.postDelayed(runnableSetBooleanHandler(), duration);
                }
            }
        }
        else
        {
            booleanMode = true;
            booleanSelect = booleanStart = false;

            showOfLights();
            handler.postDelayed(runnableToggleCells(Arrays.asList(intMode)), intShowOfLightsDuration);
            handler.postDelayed(runnableSetBooleanHandler(), intShowOfLightsDuration);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        findViewById(R.id.linearLayout).getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Cell cell;
        int row;
        LinearLayout linearLayout;

        deserialize();
        setContentView(R.layout.lights_out);

        booleanOff = true;

        buttonHelp = (Button)findViewById(R.id.buttonHelp);
        buttonHelp.setOnTouchListener(this);
        buttonHelp.setSoundEffectsEnabled(false);

        buttonOnOff = (Button)findViewById(R.id.buttonOnOff);
        buttonOnOff.setOnTouchListener(this);
        buttonOnOff.setSoundEffectsEnabled(false);

        buttonSelect = (Button)findViewById(R.id.buttonSelect);
        buttonSelect.setOnTouchListener(this);
        buttonSelect.setSoundEffectsEnabled(false);

        buttonSound = (Button)findViewById(R.id.buttonSound);
        buttonSound.setOnTouchListener(this);
        buttonSound.setSoundEffectsEnabled(false);

        buttonStart = (Button)findViewById(R.id.buttonStart);
        buttonStart.setOnTouchListener(this);
        buttonStart.setSoundEffectsEnabled(false);

        listButtons = new ArrayList<>();
        listButtons.add(findViewById(R.id.buttonOnOff));
        listButtons.add(findViewById(R.id.buttonStart));
        listButtons.add(findViewById(R.id.buttonSound));
        listButtons.add(findViewById(R.id.buttonHelp));

        listCells = new ArrayList<>();

        for (int i = 0; i < intCellsCount; i++)
        {
            cell = new Cell(this, i);
            cell.setOnTouchListener(this);
            cell.setSoundEffectsEnabled(false);

            row = i / intColumnsCount;

            if (row == 0)
            {
                linearLayout = (LinearLayout)findViewById(R.id.linearLayoutRow0);
            }
            else if (row == 1)
            {
                linearLayout = (LinearLayout)findViewById(R.id.linearLayoutRow1);
            }
            else if (row == 2)
            {
                linearLayout = (LinearLayout)findViewById(R.id.linearLayoutRow2);
            }
            else if (row == 3)
            {
                linearLayout = (LinearLayout)findViewById(R.id.linearLayoutRow3);
            }
            else
            {
                linearLayout = (LinearLayout)findViewById(R.id.linearLayoutRow4);
            }

            linearLayout.addView(cell);
            listCells.add(cell);
        }

        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);

        listTextViews = new ArrayList<>();
        listTextViews.add(findViewById(R.id.textViewOnOff));
        listTextViews.add(findViewById(R.id.textViewStart));
        listTextViews.add(findViewById(R.id.textViewSound));
        listTextViews.add(findViewById(R.id.textViewHelp));

        listViews = new ArrayList<>();
        listViews.add(findViewById(R.id.viewOnOff));
        listViews.add(findViewById(R.id.viewStart));
        listViews.add(findViewById(R.id.viewSound));
        listViews.add(findViewById(R.id.viewHelp));

        setCells = new HashSet<>();

        Typeface typeFace = createFromAsset(getAssets(), "fonts/Alegreya Sans/Alegreya Sans Black Italic.ttf");

        ((TextView)findViewById(R.id.textViewHelp)).setTypeface(typeFace);
        ((TextView)findViewById(R.id.textViewOnOff)).setTypeface(typeFace);
        ((TextView)findViewById(R.id.textViewSelect)).setTypeface(typeFace);
        ((TextView)findViewById(R.id.textViewSound)).setTypeface(typeFace);
        ((TextView)findViewById(R.id.textViewStart)).setTypeface(typeFace);
    }

    @Override
    public void onGlobalLayout()
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        float height, length, width;
        int column, row;
        LayoutParams layoutParams;

        height = linearLayout.getHeight();
        width = linearLayout.getWidth();

        if (height < width)
        {
            height -= width / 10;
            width /= 2;

            linearLayout.setPadding(0, (int)(width / 10), 0, (int)(width / 10));
        }
        else
        {
            height /= 2;
        }

        length = width / 59;
        height = (height - length * 9.5f) / 5;
        width = (width - length * 12) / 5;

        for (int i = 0; i < listCells.size(); i++)
        {
            column = i % intColumnsCount;
            row = i / intColumnsCount;

            layoutParams = new LayoutParams((int)width, (int)height);
            layoutParams.setMargins((int)(length), (int)(length / 2), (int)(length), (int)(length / 2));

            if (column == 0)
            {
                layoutParams.leftMargin = (int)(length * 2);
            }
            else if (column == intColumnsCount - 1)
            {
                layoutParams.rightMargin= (int)(length * 2);
            }

            if (row == 0)
            {
                layoutParams.topMargin = (int)length;
            }
            else if (row == intColumnsCount - 1)
            {
                layoutParams.bottomMargin = (int)length;
            }

            listCells.get(i).setLayoutParams(layoutParams);
        }

        findViewById(R.id.linearLayoutSelect).setLayoutParams(new LayoutParams((int)(length * 8 + width * 3), MATCH_PARENT));

        for (int i = 0; i < listButtons.size(); i++)
        {
            layoutParams = new LayoutParams((int)(width - length), (int)(length * 2));
            layoutParams.setMargins((int)(length / 2), (int)(length / 2), (int)(length / 2), (int)(length / 2));

            listButtons.get(i).setLayoutParams(layoutParams);
        }

        for (int i = 0; i < listTextViews.size(); i++)
        {
            layoutParams = (LayoutParams)listTextViews.get(i).getLayoutParams();
            layoutParams.setMargins(0, 0, (int)(length * 2), 0);

            listTextViews.get(i).setLayoutParams(layoutParams);
        }

        for (int i = 0; i < listViews.size(); i++)
        {
            layoutParams = new LayoutParams(MATCH_PARENT, (int)length);
            layoutParams.setMargins(0, (int)(length / 2), 0, (int)length);

            listViews.get(i).setLayoutParams(layoutParams);
        }

        findViewById(R.id.tiger).setLayoutParams(new LayoutParams((int)(length * 2000 / 336 * 3), (int)(length * 3)));

        layoutParams = new LayoutParams((int)(length * 18), (int)(length * 12));
        layoutParams.setMargins((int)length, (int)(length * 3), 0, 0);

        findViewById(R.id.linearLayoutLogo).setLayoutParams(layoutParams);

        layoutParams = new LayoutParams((int)(length * 16.5), (int)(length * 4.5));
        layoutParams.setMargins(0, 0, 0, (int)(length / 2));

        findViewById(R.id.imageViewLights).setLayoutParams(layoutParams);

        layoutParams = new LayoutParams((int)(length * 4.5), (int)(length * 7));
        layoutParams.setMargins((int)(length / 2), 0, 0, 0);

        findViewById(R.id.imageViewViewOut).setLayoutParams(layoutParams);

        layoutParams = new LayoutParams((int)length, (int)length);
        layoutParams.setMargins((int)(length / 2), 0, 0, 0);

        findViewById(R.id.imageViewTM).setLayoutParams(layoutParams);

        layoutParams = new LayoutParams((int)(length * 16), (int)(length * 5));
        layoutParams.setMargins((int)length, (int)length, (int)length, (int)length);

        buttonSelect.setLayoutParams(layoutParams);

        layoutParams = (LayoutParams)findViewById(R.id.textViewSelect).getLayoutParams();
        layoutParams.setMargins(0, (int)length, 0, 0);

        findViewById(R.id.textViewSelect).setLayoutParams(layoutParams);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (mediaPlayer != null)
        {
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.release();
            mediaPlayer = null;
        }

        soundPool.unload(intSoundPoolID);
        soundPool.release();
        soundPool = null;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        soundPool = new SoundPool.Builder().setMaxStreams(intCellsCount).build();
        intSoundPoolID = soundPool.load(this, R.raw.press, 1);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if (!booleanHandler && !booleanMediaPlayer)
        {
            if (v == buttonOnOff)
            {
                pressOnOff(event);
            }
            else if (booleanOn && event.getActionMasked() == ACTION_DOWN)
            {
                if (booleanHelp && booleanSelect && v == buttonHelp)
                {
                    pressHelp();
                }
                else if (v == buttonSelect)
                {
                    pressSelect();
                }
                else if (v == buttonSound)
                {
                    pressSound();
                }
                else if (v == buttonStart)
                {
                    pressStart();
                }
                else if (booleanSelect && v instanceof Cell)
                {
                    pressCell(((Cell)v).getCell());
                }
            }
        }

        return false;
    }

    private void playMedia(int ID)
    {
        booleanMediaPlayer = true;
        intMediaPlayerID = ID;

        mediaPlayer = MediaPlayer.create(this, ID);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.start();
    }

    private void playSound()
    {
        if (booleanSound)
        {
            soundPool.play(intSoundPoolID, 1, 1, 1, 0, 1);
        }
    }

    private void pressCell(int cell)
    {
        if (booleanMode)
        {
            if (intPuzzle < 0)
            {
                intPuzzle = cell;

                playSound();
                toggleCell(cell);
            }
            else if (intPuzzle == cell)
            {
                toggleCells(setCells);

                if (intPuzzle + intCellsCount > LightsOut.getPuzzle())
                {
                    intPuzzle = -1;

                    if (booleanSound)
                    {
                        booleanMediaPlayer = true;

                        playMedia(R.raw.error);
                    }
                }
                else
                {
                    intPuzzle += intCellsCount;
                    puzzle = LightsOut.getPuzzle(intPuzzle);

                    playSound();

                    if (intPuzzle < intCellsCount - 1)
                    {
                        toggleCell(intCellsCount - 1);
                    }

                    blinkingLights(Arrays.asList(intPuzzle - intCellsCount));
                    handler.postDelayed(runnableToggleCells(Arrays.asList(intCellsCount - 1)), intBlinkingLightsDuration);
                    handler.postDelayed(runnableStartPuzzle(), intBlinkingLightsDuration);
                    handler.postDelayed(runnableSetBooleanHandler(), intBlinkingLightsDuration);
                }
            }
            else
            {
                if (booleanSound)
                {
                    booleanMediaPlayer = true;

                    playMedia(R.raw.error);
                }

                intPuzzle = -1;

                toggleCells(setCells);
            }
        }
        else if (booleanStart)
        {
            booleanHelp = false;
            intMovesCount++;

            toggleCells(cell);
            flickerCells();
        }
        else
        {
            toggleCell(cell);
            flickerCells();
        }
    }

    private void pressHelp()
    {
        flickerCells();

        if (listHints.size() > 0)
        {
            List<Integer> cell = Arrays.asList(listHints.get(0));

            blinkingLights(cell);
            handler.postDelayed(runnableToggleCells(listHints.get(0)), intBlinkingLightsDuration);
            handler.postDelayed(runnableSetBooleanHandler(), intBlinkingLightsDuration);
            listHints.remove(0);
        }
    }

    private boolean pressOnOff(MotionEvent event)
    {
        if (event.getActionMasked() == ACTION_DOWN)
        {
            if (booleanOn)
            {
                booleanOn = false;

                toggleCells(setCells);
            }

            buttonOnOff.setPressed(true);
        }
        else if (event.getActionMasked() == ACTION_CANCEL || event.getActionMasked() == ACTION_UP)
        {
            if (booleanOff)
            {
                booleanMode = booleanOff = booleanSelect = booleanStart = false;
                booleanHandler = booleanMediaPlayer = booleanOn = booleanSound = true;
                intMode = 0;
                intPuzzle = LightsOut.getPuzzle();

                playMedia(R.raw.on);
            }
            else
            {
                booleanOff = true;
            }

            buttonOnOff.setPressed(false);
        }

        return true;
    }

    private void pressSelect()
    {
        toggleCells(setCells);

        if (booleanSelect)
        {
            playSound();

            if (booleanStart && intMode == 0)
            {
                intPuzzle = -1;
            }
            else if (booleanSelect)
            {
                booleanSelect = false;

                toggleCell(intMode);
            }

            booleanMode = true;
            booleanStart = false;
        }
        else
        {
            if (booleanMode)
            {
                intMode = (intMode + 1) % intModesCount;
            }
            else
            {
                booleanMode = true;
            }

            toggleCell(intMode);
        }
    }

    private void pressSound()
    {
        booleanSound = !booleanSound;

        if (booleanSound)
        {
            flickerCells();
        }
    }

    private void pressStart()
    {
        if (booleanStart)
        {
            startPuzzle();
            flickerCells();
        }
        else if (booleanMode && booleanSelect)
        {
            if (intPuzzle < 0 || intPuzzle > LightsOut.getPuzzle())
            {
                if (booleanSound)
                {
                    booleanMediaPlayer = true;

                    playMedia(R.raw.error);
                }

                intPuzzle = -1;

                toggleCells(setCells);
            }
            else
            {
                playSound();
                toggleCells(setCells);
                blinkingLights(Arrays.asList(intPuzzle));
                handler.postDelayed(runnableStartPuzzle(), intBlinkingLightsDuration);
                handler.postDelayed(runnableSetBooleanHandler(), intBlinkingLightsDuration);

                booleanMode = false;
                booleanStart = true;
                puzzle = LightsOut.getPuzzle(intPuzzle);
            }
        }
        else if (booleanSelect)
        {
            booleanStart = true;
            puzzle = new Puzzle(0, new ArrayList<Integer>(), new HashSet<>(setCells));

            if (booleanSound)
            {
                booleanMediaPlayer = true;

                playMedia(R.raw.create);
            }
            else
            {
                onCompletionCreate();
            }

            toggleCells(setCells);
        }
        else if (booleanMode)
        {
            if (intMode == 0)
            {
                booleanHandler = booleanStart = true;
                intPuzzle = LightsOut.getPuzzle();
                puzzle = LightsOut.getPuzzle(intPuzzle);
            }
            else if (intMode == 1)
            {
                booleanStart = true;
                puzzle = createPuzzle();
            }

            booleanMode = false;
            booleanSelect = true;

            if (booleanSound)
            {
                booleanMediaPlayer = true;

                playMedia(R.raw.confirm);
            }
            else
            {
                onCompletionConfirm();
            }

            toggleCells(setCells);
        }
        else
        {
            if (booleanSound)
            {
                booleanMediaPlayer = true;

                playMedia(R.raw.confirm);
            }
            else
            {
                onCompletionConfirm();
            }

            toggleCells(setCells);

            booleanHandler = booleanSelect = booleanStart = true;
            puzzle = LightsOut.getPuzzle(intPuzzle);
        }
    }

    private void serialize()
    {
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), stringPuzzle)); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream))
        {
            objectOutputStream.writeInt(intPuzzle);
        }
        catch(Exception e)
        {
            Log.e("serialize", "Error: Unable to write the puzzle. " + e.getMessage());
        }
    }

    private void showOfLights()
    {
        int delay = 0;
        int offset = intShowOfLightsDuration / (intCellsCount * 2);
        List<Integer> cell;
        List<Integer> cells = Arrays.asList(0, 5, 10, 15, 20, 21, 22, 23, 24, 19, 14, 9, 4, 3, 2, 1, 6, 11, 16, 17, 18, 13, 8, 7, 12);

        for (int i : cells)
        {
            cell = Arrays.asList(i);

            handler.postDelayed(runnablePlaySound(), delay);
            handler.postDelayed(runnableToggleCells(cell), delay);
            handler.postDelayed(runnableToggleCells(cell), delay + offset);

            delay += offset * 2;
        }
    }

    private void startPuzzle()
    {
        toggleCells(setCells);

        booleanMode = false;
        booleanHelp = booleanSelect = booleanStart = true;
        intMovesCount = 0;
        listHints = puzzle.getHints();

        toggleCells(puzzle.getCells());
    }

    private void toggleCell(int cell)
    {
        listCells.get(cell).toggleCell();

        if (setCells.contains(cell))
        {
            setCells.remove(cell);
        }
        else
        {
            setCells.add(cell);
        }
    }

    private void toggleCells(int cell)
    {
        toggleCell(cell);

        if (cell / intColumnsCount > 0)
        {
            toggleCell(cell - intColumnsCount);
        }

        if (cell % intColumnsCount > 0)
        {
            toggleCell(cell - 1);
        }

        if (cell % intColumnsCount < intColumnsCount - 1)
        {
            toggleCell(cell + 1);
        }

        if (cell / intColumnsCount < intRowsCount - 1)
        {
            toggleCell(cell + intColumnsCount);
        }

        if (setCells.size() == 0)
        {
            booleanHandler = true;

            if (booleanSound)
            {
                booleanMediaPlayer = true;

                playMedia(R.raw.solve);
            }
            else
            {
                onCompletionSolve();
            }
        }
    }

    private void toggleCells(Collection<Integer> cells)
    {
        for (int i : new HashSet<>(cells))
        {
            toggleCell(i);
        }
    }
}