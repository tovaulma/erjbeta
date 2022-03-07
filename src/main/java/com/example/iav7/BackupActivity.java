package com.example.iav7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class BackupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        Button btnBackToSettingsFromBackup = findViewById(R.id.btnBackToSettingsFromBackup);
        btnBackToSettingsFromBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BackupActivity.this, SettingsActivity.class));
            }
        });

        Button btnExport = findViewById(R.id.btnExport);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    File filePath = new File (getFilesDir() + "/journalentries");
                    File[] srcFiles = filePath.listFiles();
                    FileOutputStream fos = new FileOutputStream(Environment.getExternalStoragePublic
                            Directory(Environment.DIRECTORY_DOWNLOADS) + "/multiCompressed.zip");
                    ZipOutputStream zipOut = new ZipOutputStream(fos);
                    for (File srcFile : srcFiles) {
                        FileInputStream fis = new FileInputStream(srcFile);
                        ZipEntry zipEntry = new ZipEntry(srcFile.getName());
                        zipOut.putNextEntry(zipEntry);

                        byte[] bytes = new byte[1024];
                        int length;
                        while((length = fis.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                        fis.close();
                    }
                    zipOut.close();
                    fos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button btnImport = findViewById(R.id.btnImport);
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputStream is;
                ZipInputStream zis;
                try
                {
                    //https://stackoverflow.com/questions/3382996/how-to-unzip-files-programmatically-in-android
                    String filename;
                    is = new FileInputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/multiCompressed.zip");
                    zis = new ZipInputStream(new BufferedInputStream(is));
                    ZipEntry ze;
                    byte[] buffer = new byte[1024];
                    int count;

                    while ((ze = zis.getNextEntry()) != null)
                    {
                        filename = ze.getName();

                        // Need to create directories if not exists, or
                        // it will generate an Exception...
                        if (ze.isDirectory()) {
                            File fmd = new File(getFilesDir() + "/journalentries/" + filename);
                            fmd.mkdirs();
                            continue;
                        }

                        FileOutputStream fout = new FileOutputStream(getFilesDir() + "/journalentries/" + filename);

                        while ((count = zis.read(buffer)) != -1)
                        {
                            fout.write(buffer, 0, count);
                        }

                        fout.close();
                        zis.closeEntry();
                    }

                    zis.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();

                }

            }
        });

    }

}