package com.example.iav7;

import static com.example.iav7.CreatePasscode.doesPasscodeExist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        configureLoginButton();
        configureGoToCreatePasscodeButton();
    }

    private void configureLoginButton() {
        EditText editTextPasscode = findViewById(R.id.editTextPasscode);
        Button loginButton = (Button) findViewById(R.id.btnLogin);
//load in a file -> read first line -> if not empty -> read next line
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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = editTextPasscode.getText().toString();
                if(doesPasscodeExist()==false){
                    AlertDialog.Builder ad = new AlertDialog.Builder(LoginActivity.this);
                    ad.setTitle("You do not have a passcode!");
                    ad.setMessage("In order to login, you need to create a passcode. Do you want to get redirected to creating a passcode?");

                    ad.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            startActivity(new Intent(LoginActivity.this, CreatePasscode.class));
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
                    if (finalRealPasscode.equals(input)) {
                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = "Incorrect passcode, please try again.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            }
        });
    }

    private void configureGoToCreatePasscodeButton() {
        Button goToCreatePasscode = (Button) findViewById(R.id.btnGoToCreatePasscode);
        goToCreatePasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(doesPasscodeExist()==false) {
                    startActivity(new Intent(LoginActivity.this, CreatePasscode.class));
                }else{
                    Context context = getApplicationContext();
                    CharSequence text = "You already have a passcode.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
    }
}