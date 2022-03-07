package com.example.iav7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnOpenFromSettings = (Button)findViewById(R.id.btnGoToMainPageFromSettings);
        btnOpenFromSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, MenuActivity.class));
            }
        });

        Button btnChangePasscode = (Button)findViewById(R.id.btnChangePasscode);
        btnChangePasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, ChangePasscodeActivity.class));
            }
        });

        Button btnTrendSettings = (Button)findViewById(R.id.btnTrendSettings);
        btnTrendSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, TrendSettings.class));
            }
        });

        Button btnBackup = findViewById(R.id.btnBackup);
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, BackupActivity.class));
            }
        });
    }


}