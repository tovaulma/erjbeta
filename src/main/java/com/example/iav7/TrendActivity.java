package com.example.iav7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class TrendActivity extends AppCompatActivity {

    public double multiplier;
    public double anger;
    public double contempt;
    public double disgust;
    public double fear;
    public double happiness;
    public double neutral;
    public double sadness;
    public double surprise;

    private int trendLength;
    private int numberOfItemsInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);

        TextView textViewList = findViewById(R.id.textViewList);
        //QuotesFromFirebase.selectOneQuoteFromAnger();
        /*
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("quotesForAnger");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numberOfItemsInt = (int) snapshot.getChildrenCount();
                Random random = new Random();
                int randomNumber = random.nextInt(numberOfItemsInt);
                String str = Integer.toString(randomNumber);
                Iterable<DataSnapshot> it = snapshot.getChildren();
                Iterator<DataSnapshot> it2 = it.iterator();
                DataSnapshot ds = null;
                while(it2.hasNext()){
                    ds = it2.next();
                    if(ds.getKey().equals(str)){
                        textViewList.setText(ds.getValue(String.class));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */

        Button btnGoToMainPageFromTrend = (Button) findViewById(R.id.btnGoToMainPageFromTrend);
        btnGoToMainPageFromTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrendActivity.this, MenuActivity.class));
            }
        });

        StringBuffer buffer = new StringBuffer();
        String settingFromFile = null;
        FileInputStream fis = null;
        try {
            fis = openFileInput("trendsetting.txt");
            BufferedReader iReader = new BufferedReader(new InputStreamReader((fis)));

            settingFromFile = iReader.readLine();

            buffer.append(settingFromFile);
            iReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (settingFromFile != null) {
            String[] arr = settingFromFile.split("[=;]");
            multiplier = Double.parseDouble(arr[1]);
            anger = Double.parseDouble(arr[3]);
            contempt = Double.parseDouble(arr[5]);
            disgust = Double.parseDouble(arr[7]);
            fear = Double.parseDouble(arr[9]);
            happiness = Double.parseDouble(arr[11]);
            neutral = Double.parseDouble(arr[13]);
            sadness = Double.parseDouble(arr[15]);
            surprise = Double.parseDouble(arr[17]);
        }

        Button btnAnalyzeTrend = findViewById(R.id.btnAnalyzeTrend);
        btnAnalyzeTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(getFilesDir() + "/journalentries");

                String[] arr = new String[file.list().length];
                int i = 0;
                for (File fileEntry : file.listFiles()) {

                    String string = fileEntry.getName();
                    arr[i] = string;
                    i++;
                }
                readTrendLength();

                quickSort(arr, 0, arr.length - 1);


                JournalEntry[] selectedEntries = new JournalEntry[trendLength];

                for (int e = 0; e < selectedEntries.length; e++) {
                    String cur = arr[arr.length - (e + 1)];
                    try {
                        selectedEntries[e] = serializeDataIn(cur);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                }

                String[] emotionsFromSelectedEntries = new String[trendLength];
                for (int e = 0; e < emotionsFromSelectedEntries.length; e++) {
                    emotionsFromSelectedEntries[e] = selectedEntries[e].getEmotion();
                }
                double curEmotionMultiplier = 0;
                double[] multiplierAppliedArray = new double[8];
                int howManyDaysInARow = 1;
                for (int e = 0; e < emotionsFromSelectedEntries.length - 1; e++) {
                    String curEmotion = emotionsFromSelectedEntries[e];
                    String nextEmotion = emotionsFromSelectedEntries[e + 1];

                    switch (curEmotion) {
                        case "anger":
                            curEmotionMultiplier = anger;
                            break;
                        case "contempt":
                            curEmotionMultiplier = contempt;
                            break;
                        case "disgust":
                            curEmotionMultiplier = disgust;
                            break;
                        case "fear":
                            curEmotionMultiplier = fear;
                            break;
                        case "happiness":
                            curEmotionMultiplier = happiness;
                            break;
                        case "neutral":
                            curEmotionMultiplier = neutral;
                            break;
                        case "sadness":
                            curEmotionMultiplier = sadness;
                            break;
                        case "surprise":
                            curEmotionMultiplier = surprise;
                            break;
                    }

                    if (curEmotion.equals(nextEmotion)) {
                        howManyDaysInARow++;
                        if(e==emotionsFromSelectedEntries.length-2){
                            switch (curEmotion) {
                                case "anger":
                                    multiplierAppliedArray[0] = Math.pow(multiplier, (howManyDaysInARow - 1)) * (howManyDaysInARow * curEmotionMultiplier);
                                    break;
                                case "contempt":
                                    multiplierAppliedArray[1] = Math.pow(multiplier, (howManyDaysInARow - 1)) * (howManyDaysInARow * curEmotionMultiplier);
                                    break;
                                case "disgust":
                                    multiplierAppliedArray[2] = Math.pow(multiplier, (howManyDaysInARow - 1)) * (howManyDaysInARow * curEmotionMultiplier);
                                    break;
                                case "fear":
                                    multiplierAppliedArray[3] = Math.pow(multiplier, (howManyDaysInARow - 1)) * (howManyDaysInARow * curEmotionMultiplier);
                                    break;
                                case "happiness":
                                    multiplierAppliedArray[4] = Math.pow(multiplier, (howManyDaysInARow - 1)) * (howManyDaysInARow * curEmotionMultiplier);
                                    break;
                                case "neutral":
                                    multiplierAppliedArray[5] = Math.pow(multiplier, (howManyDaysInARow - 1)) * (howManyDaysInARow * curEmotionMultiplier);
                                    break;
                                case "sadness":
                                    multiplierAppliedArray[6] = Math.pow(multiplier, (howManyDaysInARow - 1)) * (howManyDaysInARow * curEmotionMultiplier);
                                    break;
                                case "surprise":
                                    multiplierAppliedArray[7] = Math.pow(multiplier, (howManyDaysInARow - 1)) * (howManyDaysInARow * curEmotionMultiplier);
                                    break;
                            }
                        }
                    } else {
                        switch (curEmotion) {
                            case "anger":
                                multiplierAppliedArray[0] = Math.pow(multiplier,(howManyDaysInARow-1)) * (howManyDaysInARow*curEmotionMultiplier);
                                break;
                            case "contempt":
                                multiplierAppliedArray[1] = Math.pow(multiplier,(howManyDaysInARow-1)) * (howManyDaysInARow*curEmotionMultiplier);
                                break;
                            case "disgust":
                                multiplierAppliedArray[2] = Math.pow(multiplier,(howManyDaysInARow-1)) * (howManyDaysInARow*curEmotionMultiplier);
                                break;
                            case "fear":
                                multiplierAppliedArray[3] = Math.pow(multiplier,(howManyDaysInARow-1)) * (howManyDaysInARow*curEmotionMultiplier);
                                break;
                            case "happiness":
                                multiplierAppliedArray[4] = Math.pow(multiplier,(howManyDaysInARow-1)) * (howManyDaysInARow*curEmotionMultiplier);
                                break;
                            case "neutral":
                                multiplierAppliedArray[5] = Math.pow(multiplier,(howManyDaysInARow-1)) * (howManyDaysInARow*curEmotionMultiplier);
                                break;
                            case "sadness":
                                multiplierAppliedArray[6] = Math.pow(multiplier,(howManyDaysInARow-1)) * (howManyDaysInARow*curEmotionMultiplier);
                                break;
                            case "surprise":
                                multiplierAppliedArray[7] = Math.pow(multiplier,(howManyDaysInARow-1)) * (howManyDaysInARow*curEmotionMultiplier);
                                break;

                        }
                        howManyDaysInARow = 1;
                    }
                }
                int curHighestIndex = 0;
                for(int e = 1; e < multiplierAppliedArray.length; e++){
                    if(multiplierAppliedArray[e] > multiplierAppliedArray[curHighestIndex]){
                        curHighestIndex = e;
                    }
                }
                System.out.println(curHighestIndex);

                switch (curHighestIndex) {
                    case 0:
                        //anger
                        QuotesFromFirebase.selectOneQuoteFromRefAndDisplay(QuotesFromFirebase.refForAnger, textViewList);
                        break;
                    case 1:
                        //contempt
                        QuotesFromFirebase.selectOneQuoteFromRefAndDisplay(QuotesFromFirebase.refForContempt, textViewList);
                        break;
                    case 2:
                        //disgust
                        QuotesFromFirebase.selectOneQuoteFromRefAndDisplay(QuotesFromFirebase.refForDisgust, textViewList);
                        break;
                    case 3:
                        //fear
                        QuotesFromFirebase.selectOneQuoteFromRefAndDisplay(QuotesFromFirebase.refForFear, textViewList);
                        break;
                    case 4:
                        //happiness
                        QuotesFromFirebase.selectOneQuoteFromRefAndDisplay(QuotesFromFirebase.refForHappiness, textViewList);
                        break;
                    case 5:
                        //neutral
                        QuotesFromFirebase.selectOneQuoteFromRefAndDisplay(QuotesFromFirebase.refForNeutral, textViewList);
                        break;
                    case 6:
                        //sadness
                        QuotesFromFirebase.selectOneQuoteFromRefAndDisplay(QuotesFromFirebase.refForSadness, textViewList);
                        break;
                    case 7:
                        //surprise
                        QuotesFromFirebase.selectOneQuoteFromRefAndDisplay(QuotesFromFirebase.refForSurprise, textViewList);
                        break;
                }
            }
        });
    }

    private void readTrendLength () {
        StringBuffer buffer = new StringBuffer();
        String trendLength = null;
        FileInputStream fis = null;
        try {
            fis = openFileInput("trendLength.txt");
            BufferedReader iReader = new BufferedReader(new InputStreamReader((fis)));

            trendLength = iReader.readLine();
            while (trendLength != null) {
                buffer.append(trendLength);
                trendLength = iReader.readLine();
            }
            iReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String finalTrendLength = buffer.toString();

        this.trendLength = Integer.parseInt(finalTrendLength);
    }

    public void swap (String[]arr,int i, int j){
        String temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public int partition (String[]arr,int low, int high){

        // pivot
        String pivot = arr[high];

        // Index of smaller element and
        // indicates the right position
        // of pivot found so far
        int i = (low - 1);

        for (int j = low; j <= high - 1; j++) {

            // If current element is smaller
            // than the pivot
            if (arr[j].compareTo(pivot) < 0) {

                // Increment index of
                // smaller element
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }

    public void quickSort (String[]arr,int low, int high){
        if (low < high) {
            int pi = partition(arr, low, high);

            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }


    public JournalEntry serializeDataIn (String fileName) throws IOException, ClassNotFoundException {
        File finalFileName = new File(getFilesDir() + "/journalentries/" + fileName);
        FileInputStream fis = new FileInputStream(finalFileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        JournalEntry obj = (JournalEntry) ois.readObject();
        ois.close();


        System.out.println(obj.getDate());
        return obj;
    }

    public static class QuotesFromFirebase {
        /*
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("quotesForAnger");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numberOfItemsInt = (int) snapshot.getChildrenCount();
                Random random = new Random();
                int randomNumber = random.nextInt(numberOfItemsInt);
                String str = Integer.toString(randomNumber);
                Iterable<DataSnapshot> it = snapshot.getChildren();
                Iterator<DataSnapshot> it2 = it.iterator();
                DataSnapshot ds = null;
                while(it2.hasNext()){
                    ds = it2.next();
                    if(ds.getKey().equals(str)){
                        textViewList.setText(ds.getValue(String.class));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
         */
        static DatabaseReference refForAnger = FirebaseDatabase.getInstance().getReference().child("quotesForAnger");
        static DatabaseReference refForContempt = FirebaseDatabase.getInstance().getReference().child("quotesForContempt");
        static DatabaseReference refForDisgust = FirebaseDatabase.getInstance().getReference().child("quotesForDisgust");
        static DatabaseReference refForFear = FirebaseDatabase.getInstance().getReference().child("quotesForFear");
        static DatabaseReference refForHappiness = FirebaseDatabase.getInstance().getReference().child("quotesForHappiness");
        static DatabaseReference refForNeutral = FirebaseDatabase.getInstance().getReference().child("quotesForNeutral");
        static DatabaseReference refForSadness = FirebaseDatabase.getInstance().getReference().child("quotesForSadness");
        static DatabaseReference refForSurprise = FirebaseDatabase.getInstance().getReference().child("quotesForSurprise");

        static void selectOneQuoteFromRefAndDisplay(DatabaseReference ref, TextView tv){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int numberOfItemsInt = (int) snapshot.getChildrenCount();
                    Random random = new Random();
                    int randomNumber = random.nextInt(numberOfItemsInt);
                    String str = Integer.toString(randomNumber);
                    Iterable<DataSnapshot> it = snapshot.getChildren();
                    Iterator<DataSnapshot> it2 = it.iterator();
                    DataSnapshot ds = null;
                    while(it2.hasNext()){
                        ds = it2.next();
                        if(ds.getKey().equals(str)){
                            tv.setText(ds.getValue(String.class));
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
