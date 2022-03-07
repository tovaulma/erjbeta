package com.example.iav7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TrendSettings extends AppCompatActivity {

    private SeekBar multiplier;
    String multiplierStatement;
    private SeekBar anger;
    String angerStatement;
    private SeekBar contempt;
    String contemptStatement;
    private SeekBar disgust;
    String disgustStatement;
    private SeekBar fear;
    String fearStatement;
    private SeekBar happiness;
    String happinessStatement;
    private SeekBar neutral;
    String neutralStatement;
    private SeekBar sadness;
    String sadnessStatement;
    private SeekBar surprise;
    String surpriseStatement;
    private TextView textViewNumber;
    int trendLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_settings);
        multiplier = findViewById(R.id.seekBarMultiplier);
        multiplier.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int x = multiplier.getProgress();
                double y = ((double) x)/10;
                multiplierStatement = "Multiplier=" + y + ";";
            }
        });

        anger = findViewById(R.id.seekBarAnger);
        anger.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int x = anger.getProgress();
                double y = ((double) x)/10;
                angerStatement = "Anger=" + y + ";";
            }
        });

        contempt = findViewById(R.id.seekBarContempt);
        contempt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int x = contempt.getProgress();
                double y = ((double) x)/10;
                contemptStatement = "Contempt=" + y + ";";
            }
        });

        disgust = findViewById(R.id.seekBarDisgust);
        disgust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int x = disgust.getProgress();
                double y = ((double) x)/10;
                disgustStatement = "Disgust=" + y + ";";
            }
        });

        fear = findViewById(R.id.seekBarFear);
        fear.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int x = fear.getProgress();
                double y = ((double) x)/10;
                fearStatement = "Fear=" + y + ";";
            }
        });

        happiness = findViewById(R.id.seekBarHappiness);
        happiness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int x = happiness.getProgress();
                double y = ((double) x)/10;
                happinessStatement = "Happiness=" + y + ";";
            }
        });

        neutral = findViewById(R.id.seekBarNeutral);
        neutral.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int x = neutral.getProgress();
                double y = ((double) x)/10;
                neutralStatement = "Neutral=" + y + ";";
            }
        });

        sadness = findViewById(R.id.seekBarSadness);
        sadness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int x = sadness.getProgress();
                double y = ((double) x)/10;
                sadnessStatement = "Sadness=" + y + ";";

            }
        });

        surprise = findViewById(R.id.seekBarSurprise);
        surprise.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int x = surprise.getProgress();
                double y = ((double) x)/10;
                surpriseStatement = "Surprise=" + y + ";";

            }
        });

        Button btnConfirmChangeSettings = findViewById(R.id.btnConfirmTrendSettings);
        btnConfirmChangeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileOutputStream fos = null;
                String input = mergeStatements();
                try {
                    fos = openFileOutput("trendsetting.txt", Context.MODE_PRIVATE);
                    fos.write(input.getBytes());
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(trendLength);
                String input2 = Integer.toString(trendLength);
                try {
                    fos = openFileOutput("trendLength.txt", Context.MODE_PRIVATE);
                    fos.write(input2.getBytes());
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Context context = getApplicationContext();
                CharSequence text = "Successful";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                startActivity(new Intent(TrendSettings.this, MenuActivity.class));
            }
        });

        Button btnBackToSettingsFromTrendSettings = findViewById(R.id.btnBackToSettingsFromTrendSettings);
        btnBackToSettingsFromTrendSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrendSettings.this, SettingsActivity.class));
            }
        });

        textViewNumber = findViewById(R.id.textViewNumber);
        NumberPicker numberPicker = findViewById(R.id.NumberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(30);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                textViewNumber.setText(String.format("User's Number: %s", i1));
                trendLength = i1;
            }
        });

    }

    static boolean doesTrendSettingsFileExist(){
        File file = new File("/data/data/com.example.iav7/files/trendsetting.txt");
        return file.exists();
    }

    public String mergeStatements(){
        return multiplierStatement + angerStatement + contemptStatement + disgustStatement + fearStatement + happinessStatement + neutralStatement + sadnessStatement + surpriseStatement;
    }



}