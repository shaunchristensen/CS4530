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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
    private final int intBuffer = LightsOut.getBuffer();
    private final int intColumns = LightsOut.getColumns();
    private final int intModes = LightsOut.getModes();
    private final int intRows = LightsOut.getRows();
    private List<Cell> listCells;
    private List<View> listButtons, listTextViews, listViews;;
    private Set<Integer> setCells;


    private MediaPlayer mediaPlayer;
    private int intMediaPlayerID, intMode, intMoves, intSoundPoolID;
    private Integer integerPuzzle;
    private Handler handler = new Handler();
    private SoundPool soundPool;
    private Gson gson = new Gson();
    private Type typePuzzles = new TypeToken<List<Puzzle>>(){}.getType();
    private String stringPuzzle = "Puzzle";
    private final int intBlinkCount = 4;
    private final int intBlinkDuration = 2000;
    private final int intShowOfLightsDuration = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        deserialize();
        setContentView(R.layout.lights_out);

        Cell cell;
        int row;
        LinearLayout linearLayout;

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

        for (int i = 0; i < intColumns * intRows; i++)
        {
            cell = new Cell(this, i);
            cell.setOnTouchListener(this);
            cell.setSoundEffectsEnabled(false);

            row = i / intColumns;

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

        if (cell / intColumns > 0)
        {
            toggleCell(cell - intColumns);
        }

        if (cell % intColumns > 0)
        {
            toggleCell(cell - 1);
        }

        if (cell % intColumns < intColumns - 1)
        {
            toggleCell(cell + 1);
        }

        if (cell / intColumns < intRows - 1)
        {
            toggleCell(cell + intColumns);
        }

        if (setCells.size() == 0)
        {
            if (intMode == 0 || intMode == 1)
            {
                integerPuzzle++;

                if (intMode == 0 && integerPuzzle > LightsOut.getPuzzle())
                {
                    LightsOut.setPuzzle(integerPuzzle);
                    serialize();
                }
            }

            booleanHandler = booleanMediaPlayer = true;

            playMedia(R.raw.solve);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        findViewById(R.id.linearLayout).getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout()
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        float height = linearLayout.getHeight();
        float width = linearLayout.getWidth();

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

        float length = width / 59;

        height = (height - length * 9.5f) / 5;
        width = (width - length * 12) / 5;

        int column, row;
        LayoutParams layoutParams;

        for (int i = 0; i < listCells.size(); i++)
        {
            column = i % intColumns;
            row = i / intColumns;

            layoutParams = new LayoutParams((int)width, (int)height);
            layoutParams.setMargins((int)(length), (int)(length / 2), (int)(length), (int)(length / 2));

            if (column == 0)
            {
                layoutParams.leftMargin = (int)(length * 2);
            }
            else if (column == intColumns - 1)
            {
                layoutParams.rightMargin= (int)(length * 2);
            }

            if (row == 0)
            {
                layoutParams.topMargin = (int)length;
            }
            else if (row == intColumns - 1)
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

        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
        }

        mediaPlayer.setOnCompletionListener(null);
        mediaPlayer.release();
        mediaPlayer = null;

        soundPool.unload(intSoundPoolID);
        soundPool.release();
        soundPool = null;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        soundPool = new SoundPool.Builder().setMaxStreams(intColumns).build();
        intSoundPoolID = soundPool.load(this, R.raw.press, 1);
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        booleanMediaPlayer = false;

        mediaPlayer.setOnCompletionListener(null);
        mediaPlayer.release();

        if (intMediaPlayerID == R.raw.confirm)
        {
            toggleCells();
        }
        else if (intMediaPlayerID == R.raw.create)
        {
            booleanStart = true;
        }
        else if (intMediaPlayerID == R.raw.on)
        {
            toggleCells();
        }
        else if (intMediaPlayerID == R.raw.solve)
        {
            toggleCells();
        }
    }

    private void toggleCells(Collection<Integer> cells)
    {
        for (int i : cells)
        {
            listCells.get(i).toggleCell();
        }
    }

    private Runnable runnableBooleanHandler()
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

    private void playMedia(int ID)
    {
        if (booleanSound)
        {
            booleanMediaPlayer = true;
            intMediaPlayerID = ID;

            mediaPlayer = MediaPlayer.create(this, ID);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
        }
    }

    private void playSound()
    {
        if (booleanSound)
        {
            soundPool.play(intSoundPoolID, 1, 1, 1, 0, 1);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if (!booleanHandler && !booleanMediaPlayer)
        {
            if (v == buttonOnOff)
            {
                if (event.getActionMasked() == ACTION_DOWN)
                {
                    if (booleanOn)
                    {
                        booleanOn = false;
                        integerPuzzle = null;

                        toggleCells(setCells);
                    }

                    buttonOnOff.setPressed(true);
                }
                else if (event.getActionMasked() == ACTION_CANCEL || event.getActionMasked() == ACTION_UP)
                {
                    if (booleanOff)
                    {
                        booleanMode = booleanOff = booleanSelect = booleanStart = false;
                        booleanHandler = booleanMediaPlayer = booleanSound = true;

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
            else if (booleanOn && event.getActionMasked() == ACTION_DOWN)
            {
                // check hints size > 0
                if (booleanHelp && booleanSelect && v == buttonHelp)
                {
                    // click, flash hint cell 4 times, then press hint cell
//                    pressCell(12);
                }
                else if (v == buttonSelect)
                {
                    toggleCells(setCells);

                    if (booleanSelect && booleanStart)
                    {
                        booleanMode = true;
                        booleanStart = false;
                        integerPuzzle = null;

                        playSound();
                    }
                    else if (booleanSelect)
                    {
                        booleanSelect = false;

                        playSound();
                        toggleCell(intMode);
                    }
                    else
                    {
                        if (booleanMode)
                        {
                            intMode = (intMode + 1) % intModes;
                        }
                        else
                        {
                            booleanMode = true;
                        }

                        toggleCell(intMode);
                    }
                }
                else if (v == buttonSound)
                {
                    booleanSound = !booleanSound;

                    if (booleanSound && booleanSelect)
                    {
                        playSound();
                        toggleCells(setCells);
                        handler.postDelayed(runnableToggleCells(setCells), 1);
                    }
                }
                else if (v == buttonStart)
                {
                    if (integerPuzzle == null)
                    {
                        integerPuzzle = LightsOut.getPuzzle();
                    }

                    booleanMode = false;
                    toggleCells(setCells);
                    toggleCells();
                    // fix later
                }
                else if (booleanSelect && v instanceof Cell)
                {
                    Cell cell = (Cell)v;

                    if (booleanMode)
                    {
                        if (integerPuzzle == null)
                        {
                            integerPuzzle = cell.getCell();

                            playSound();
                            toggleCell(cell.getCell());
                        }
                        else
                        {
                            if (integerPuzzle == cell.getCell())
                            {
                                integerPuzzle += intColumns * intRows;

                                toggleCells();
                            }
                            else
                            {
                                integerPuzzle = null;

                                playMedia(R.raw.error);
                                toggleCells(setCells);
                            }
                        }
                    }
                    else if (booleanStart)
                    {
                        toggleCells(cell.getCell());
                    }
                    else
                    {
                        toggleCell(cell.getCell());
                    }
                }
            }
        }

        return false;
    }

    private void toggleCells()
    {
        int delay = 0;
        int offset;
        List<Integer> cells;


        if (booleanOn)
        {
            int cell, count;

            if (booleanStart)
            {
                // over, equal, under

            }
            else
            {
                offset = intBlinkDuration / (intBlinkCount * 2);

                toggleCells(setCells);

                if (integerPuzzle > intColumns * intRows)
                {
                    cells = Arrays.asList(integerPuzzle - intColumns * intRows);

                    listCells.get(listCells.size() - 1).toggleCell();
                }
                else
                {
                    cells = Arrays.asList(integerPuzzle);
                }

                for (int i = 0; i < intBlinkCount; i++)
                {
                    handler.postDelayed(runnableToggleCells(cells), delay);
                    handler.postDelayed(runnableToggleCells(cells), delay + offset);

                    delay += offset * 2;
                }

                handler.postDelayed(runnableBooleanHandler(), intBlinkDuration);
            }
            // if puzzle > size show cross, else blink/start puzzle
        }
        else
        {
            booleanOn = true;
            List<Integer> cell;
            cells = Arrays.asList(0, 5, 10, 15, 20, 21, 22, 23, 24, 19, 14, 9, 4, 3, 2, 1, 6, 11, 16, 17, 18, 13, 8, 7, 12);
            offset = intShowOfLightsDuration / (intColumns * intRows * 2);

            for (int i : cells)
            {
                cell = Arrays.asList(i);

                handler.postDelayed(runnablePlaySound(), delay);
                handler.postDelayed(runnableToggleCells(cell), delay);
                handler.postDelayed(runnableToggleCells(cell), delay + offset);

                delay += offset * 2;
            }

            handler.postDelayed(runnableToggleCells(Arrays.asList(7, 11, 12, 13, 17)), intShowOfLightsDuration);
            handler.postDelayed(runnableBooleanHandler(), intShowOfLightsDuration);
        }
    }

    private void deserialize()
    {
        try (FileInputStream fileInputStream = getApplicationContext().openFileInput(stringPuzzle); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream))
        {
            LightsOut.setPuzzle(objectInputStream.readInt());
        }
        catch (Exception e)
        {
            Log.e("deserialize", "Error: Unable to read the puzzle. " + e.getMessage());
        }

        try (InputStream inputStream = getResources().openRawResource(R.raw.puzzles0); InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
        {
            String s;
            StringBuilder stringBuilder = new StringBuilder();

            while ((s = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(s);
            }

            LightsOut.setPuzzles((List<Puzzle>)gson.fromJson(stringBuilder.toString(), typePuzzles));
        }
        catch (Exception e)
        {
            Log.e("deserialize", "Error: Unable to read the puzzles. " + e.getMessage());
        }
    }

    private void serialize()
    {
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), stringPuzzle)); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream))
        {
            objectOutputStream.writeInt(integerPuzzle);
        }
        catch(Exception e)
        {
            Log.e("serialize", "Error: Unable to write the puzzle. " + e.getMessage());
        }
    }
}