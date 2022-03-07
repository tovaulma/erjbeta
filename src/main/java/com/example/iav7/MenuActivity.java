package com.example.iav7;

import static com.example.iav7.TrendSettings.doesTrendSettingsFileExist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);

        Button btnOpen = (Button)findViewById(R.id.btnOpen);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);

            }
        });

        Button btnClose = (Button)findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        Button btnGoToWriteJournal = (Button)findViewById(R.id.btnGoToWriteJournal);
        btnGoToWriteJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, Api.class));
            }
        });

        Button btnGoToReadJournal = (Button)findViewById(R.id.btnGoToReadEntry);
        btnGoToReadJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, ReadJournalActivity.class));
            }
        });

        Button btnGoToTrend = (Button)findViewById(R.id.btnGoToTrend);
        btnGoToTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(doesTrendSettingsFileExist()==false){
                    AlertDialog.Builder ad = new AlertDialog.Builder(MenuActivity.this);
                    ad.setTitle("No Trend Setting File Found!");
                    ad.setMessage("In order to view trend, you need to create a trend setting file. Do you want to get redirected to creating a trend setting file?");

                    ad.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            startActivity(new Intent(MenuActivity.this, TrendSettings.class));
                        }
                    });

                    ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    ad.show();
                }else {
                    startActivity(new Intent(MenuActivity.this, TrendActivity.class));
                }
            }
        });

        Button btnGoToSettings = (Button)findViewById(R.id.btnGoToSettings);
        btnGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
            }
        });
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

}