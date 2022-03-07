package com.example.iav7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreatePasscode extends AppCompatActivity {

    private EditText editTextEnterPasscode;
    private EditText editTextConfirmPasscode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_passcode);

        configureCreatePasscodeButton();
    }

    private void configureCreatePasscodeButton() {
        editTextEnterPasscode = findViewById(R.id.editTextEnterPasscode);
        editTextConfirmPasscode = findViewById(R.id.editTextConfirmPasscode);
        Button createPasscode = (Button) findViewById(R.id.btnCreatePasscode);
        createPasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputPasscode = editTextEnterPasscode.getText().toString();

                String inputConfirmPasscode = editTextConfirmPasscode.getText().toString();
                FileOutputStream fos = null;
                if(inputPasscode.equals(inputConfirmPasscode)){
                    if(inputPasscode.length() <= 3){
                        Context context = getApplicationContext();
                        CharSequence text = "Passcode has to consist of at least four digits.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }else {
                        try {
                            fos = openFileOutput("passcode.txt", Context.MODE_PRIVATE);
                            fos.write(inputPasscode.getBytes());
                            fos.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(CreatePasscode.this, LoginActivity.class));
                    }
                }else{
                    System.out.println("Incorrect");
                    Context context = getApplicationContext();
                    CharSequence text = "Passcode confirmation doesn't match, please try again.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

    }

    static boolean doesPasscodeExist(){
        File file = new File("/data/data/com.example.iav7/files/passcode.txt");
        return file.exists();
    }
}