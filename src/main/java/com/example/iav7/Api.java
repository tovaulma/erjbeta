package com.example.iav7;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.AsyncTaskLoader;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Api extends AppCompatActivity {
    Button btnApi;
    String shared = "file";
    EditText editText;
    double[] arr = new double[8];
    String emotion;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        btnApi = findViewById(R.id.buttonApi);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        btnApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileName = getIntent().getStringExtra("fileName");
                detectFace();
                emotion = findHighestEmotion();
                if(emotion==null){
                    AlertDialog.Builder ad = new AlertDialog.Builder(Api.this);
                    ad.setTitle("No emotion found!");
                    ad.setMessage("In order to write an entry, you need an emotion! Do you want to get redirected to taking a picture again?");

                    ad.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            startActivity(new Intent(Api.this, Camera.class));
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

                    Intent intent = new Intent(Api.this, WriteJournalActivity.class);
                    intent.putExtra("foundEmotion", emotion);
                    startActivity(intent);
                }


            }


        });

        Button btnPicture = findViewById(R.id.btnPicture);
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Api.this, Camera.class));
            }
        });

    }


    @Override
    protected void onDestroy() {
        //saving string
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = editText.getText().toString();
        editor.putString("test", value);
        editor.commit();
        super.onDestroy();
    }

    // calling ThreadDetectFace
    public void detectFace() {
        try {

            Response response = new ThreadDetectFace().execute(fileName, getFilesDir().toString()).get();
            String entity = response.body().string().trim();
            // </main>
            // <print>
            /*if (entity != null) {
                // Format and display the JSON response.
                System.out.println("REST Response:\n");

                String jsonString = EntityUtils.toString(entity).trim();
                if (jsonString.charAt(0) == '[') {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    System.out.println(jsonArray.toString(2));

                    textView1.setText(jsonArray.toString());
                } else if (jsonString.charAt(0) == '{') {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    System.out.println(jsonObject.toString(2));

                    textView1.setText(jsonObject.toString());
                } else {
                    System.out.println(jsonString);

                    textView1.setText(jsonString);
                }
            }
             */
            if (entity != null) {
                JSONArray jsonArray = new JSONArray(entity);

                arr[0] = jsonArray.getJSONObject(0).getJSONObject("faceAttributes").getJSONObject("emotion").getDouble("anger");
                arr[2] = jsonArray.getJSONObject(0).getJSONObject("faceAttributes").getJSONObject("emotion").getDouble("disgust");
                arr[3] = jsonArray.getJSONObject(0).getJSONObject("faceAttributes").getJSONObject("emotion").getDouble("fear");
                arr[4] = jsonArray.getJSONObject(0).getJSONObject("faceAttributes").getJSONObject("emotion").getDouble("happiness");
                arr[5] = jsonArray.getJSONObject(0).getJSONObject("faceAttributes").getJSONObject("emotion").getDouble("neutral");
                arr[6] = jsonArray.getJSONObject(0).getJSONObject("faceAttributes").getJSONObject("emotion").getDouble("sadness");
                arr[7] = jsonArray.getJSONObject(0).getJSONObject("faceAttributes").getJSONObject("emotion").getDouble("surprise");
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String findHighestEmotion(){
        double curHighestScore = 0;
        double curHighestPossibleScore = 1 - curHighestScore;
        int curIndex = 0;
        String curEmotion = null;
        for(int i = 0; i < 8; i++){
            if(arr[i]>cur){
                cur = arr[i];
                curIndex = i;
            }
            if(curHighestScore>curHighestPossibleScore) {
                break;
            }
        }

        switch(curIndex) {
            case 0:
                curEmotion = "anger";
                break;
            case 1:
                curEmotion = "contempt";
                break;
            case 2:
                curEmotion = "disgust";
                break;
            case 3:
                curEmotion = "fear";
                break;
            case 4:
                curEmotion = "happiness";
                break;
            case 5:
                curEmotion = "neutral";
                break;
            case 6:
                curEmotion = "sadness";
                break;
            case 7:
                curEmotion = "surprise";
                break;
            default:
                curEmotion = null;
                break;
        }

        if(arr[curIndex]==0){
            return null;
        }else {
            return curEmotion;
        }
    }
}


class ThreadDetectFace extends AsyncTask<String, Void, Response> {
    Response response;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Response doInBackground(String... info) {
        final String subscriptionKey = "2c8834a4c9c4425b87d916b622819804";
        final String endpoint = "facerecia.cognitiveservices.azure.com";
        OkHttpClient client = new OkHttpClient();

        File picfile = new File(info[0]);
        try {
            picfile = new File( info[1] + "/capture/" + picfile.toString());
            byte[] fileContent = Files.readAllBytes(picfile.toPath());
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/octet-stream"),
                    fileContent
            );
            HttpUrl uri = new HttpUrl.Builder()
                    .scheme("https")
                    .host(endpoint)
                    .addPathSegment("face")
                    .addPathSegment("v1.0")
                    .addPathSegment("detect")
                    .addQueryParameter("detectionModel", "detection_01")
                    .addQueryParameter("returnFaceId", "true")
                    .addQueryParameter("returnFaceAttributes", "emotion")
                    .build();
            Request request = new Request.Builder()
                    .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                    .addHeader("Content-Type", "application/octet-stream")
                    .url(uri)
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            response = call.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return response;
    }
}