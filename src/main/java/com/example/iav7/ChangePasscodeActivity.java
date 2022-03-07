package com.example.iav7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChangePasscodeActivity extends AppCompatActivity {

    private EditText editTextOldPasscode;
    private EditText editTextNewPasscode;
    private EditText editTextConfirmNewPasscode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passcode);

        Button btnBackToSettings = (Button)findViewById(R.id.btnBackToSettings);
        btnBackToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePasscodeActivity.this, SettingsActivity.class));
            }
        });

        Button btnConfirmChangePasscode = (Button)findViewById(R.id.btnConfirmChangePasscode);
        btnConfirmChangePasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextOldPasscode = findViewById(R.id.editTextOldPasscode);
                editTextNewPasscode = findViewById(R.id.editTextNewPasscode);
                editTextConfirmNewPasscode = findViewById(R.id.editTextConfirmNewPasscode);

                String oldPasscode = editTextOldPasscode.getText().toString();
                String newPasscode = editTextNewPasscode.getText().toString();
                String confirmNewPasscode = editTextConfirmNewPasscode.getText().toString();

                //should i make this into static method?
                StringBuffer buffer = new StringBuffer();
                String realPasscode = null;
                FileInputStream fis = null;
                try {
                    fis = openFileInput("passcode.txt");
                    BufferedReader iReader = new BufferedReader(new InputStreamReader((fis)));

                    realPasscode = iReader.readLine();
                    while(realPasscode != null)
                    {
                        buffer.append(realPasscode);
                        realPasscode = iReader.readLine();
                    }
                    iReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String finalRealPasscode = buffer.toString();
                FileOutputStream fos = null;
                if(finalRealPasscode.equals(newPasscode)) {
                    Context context = getApplicationContext();
                    CharSequence text = "You cannot use the same passcode, please try again.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else if(!oldPasscode.equals(finalRealPasscode)){
                    Context context = getApplicationContext();
                    CharSequence text = "Incorrect passcode, please try again.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else if(!newPasscode.equals(confirmNewPasscode)){
                    Context context = getApplicationContext();
                    CharSequence text = "Passcode confirmation doesn't match, please try again.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else if((oldPasscode.equals(finalRealPasscode))&&(newPasscode.equals(confirmNewPasscode))){
                    try {
                        fos = openFileOutput("passcode.txt", Context.MODE_PRIVATE);
                        fos.write(newPasscode.getBytes());
                        fos.close();
                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Context context = getApplicationContext();
                    CharSequence text = "Passcode change successful.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });

    }
}