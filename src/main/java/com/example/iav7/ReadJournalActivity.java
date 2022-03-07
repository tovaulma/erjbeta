package com.example.iav7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ReadJournalActivity extends AppCompatActivity {
    private CalendarView calendar;
    String day;

    ArrayList<String> journalFileArray = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_journal);

        calendar = findViewById(R.id.calendarViewInRead);
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date(calendar.getDate());
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {
                day = String.format("%02d", (month+1)) + "-" + String.format("%02d", date) + "-" + year;
                System.out.println(day);
            }


        });

        Button btnBackToMenuFromReadJournal = findViewById(R.id.btnBackToMenuFromReadJournal);
        btnBackToMenuFromReadJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReadJournalActivity.this, MenuActivity.class));
            }
        });

        TextView textViewSearch = findViewById(R.id.textViewSearch);
        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listFilesForFolder(getFilesDir());
                StringBuilder textViewStatement = new StringBuilder();
                for(String fileName : journalFileArray){
                    try {
                        textViewStatement.append("\n"+serializeDataIn(fileName).toString());
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                textViewSearch.setText(textViewStatement.toString());
            }
        });
    }

    public JournalEntry serializeDataIn(String fileName) throws IOException, ClassNotFoundException {
        File finalFileName = new File(getFilesDir()+ "/journalentries/" + fileName);
        FileInputStream fis = new FileInputStream(finalFileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        JournalEntry obj = (JournalEntry) ois.readObject();
        ois.close();
        System.out.println(obj.getDate());
        return obj;
    }

    public void listFilesForFolder(File folder) {
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                String[] arr = fileEntry.getName().split(" ");
                String string = fileEntry.getName();
                if(arr[0].equals(day)){
                    journalFileArray.add(string);
                }
            }
        }
    }

}