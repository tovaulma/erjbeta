package com.example.iav7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class WriteJournalActivity extends AppCompatActivity {

    //private CalendarView calendar;
    private EditText editTextDescription;
    String day;
    String description;

    @Override
    protected void  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_journal);
        //change it to emotion;
        String apiResult = getIntent().getStringExtra("foundEmotion");

        Button btnOpenMenuFromWriteJournal = (Button)findViewById(R.id.btnGoToMainPageFromWriteJournal);
        btnOpenMenuFromWriteJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WriteJournalActivity.this, MenuActivity.class));
            }
        });

        TextView textViewShowResult = findViewById(R.id.textViewShowResult);
        textViewShowResult.setText("Your detected emotion was: "+apiResult);

        Button btnEnterJournal = (Button)findViewById(R.id.btnEnterJournal);
        btnEnterJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                day= dateFormat.format(calendar.getTime());

                editTextDescription = findViewById(R.id.editTextDescription);
                description = editTextDescription.getText().toString();

                JournalEntry obj = new JournalEntry(apiResult, description, day);
 //               JournalEntry obj = new JournalEntry("anger", description, day);
                try {
                    serializeDataOut(obj);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Context context = getApplicationContext();
                CharSequence text = "Successful";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                startActivity(new Intent(WriteJournalActivity.this, MenuActivity.class));
            }
        });

/*        CalendarView calendarView = findViewById(R.id.calendarView);
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date(calendarView.getDate());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {
                day = String.format("%02d", (month+1)) + "-" + String.format("%02d", date) + "-" + year;
            }
        });
*/

    }

    public void serializeDataOut(JournalEntry obj)throws IOException {
        File filepath = new File(getFilesDir() + "/journalentries");
        if(!filepath.exists()){
            filepath.mkdirs();
        }
        File finalFileName = new File(filepath.toString()+ "/" + day + ".txt");

        FileOutputStream fos = new FileOutputStream(finalFileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.close();
    }
}