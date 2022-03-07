package com.example.iav7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Camera extends AppCompatActivity {

    private static final String TAG = "CameraActivity";

    public static final int REQUEST_TAKE_PHOTO = 10;
    public static final int REQUEST_PERMISSION = 11;

    private Button btnCamera, btnSave;
    private ImageView ivCapture;
    private String mCurrentPhotoPath;
    private String day;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        checkPermission();

        ivCapture = findViewById(R.id.ivCapture);
        btnCamera = findViewById(R.id.btnCapture);
        btnSave = findViewById(R.id.btnSave);


        java.util.Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        day= dateFormat.format(calendar.getTime());
//        loadImgArr();


        btnCamera.setOnClickListener(v -> captureCamera());


        btnSave.setOnClickListener(v -> {

            try {

                BitmapDrawable drawable = (BitmapDrawable) ivCapture.getDrawable();
                Bitmap bitmap = drawable.getBitmap();


                if (bitmap == null) {
                    Toast.makeText(this, "No image to save.", Toast.LENGTH_SHORT).show();
                } else {

                    saveImg();
                    mCurrentPhotoPath = ""; //initialize
                }

            } catch (Exception e) {
                Log.w(TAG, "SAVE ERROR!", e);
            }
            fileName = "image"+day+".jpg";
            Intent intent = new Intent(Camera.this, Api.class);
            intent.putExtra("fileName", fileName);
            startActivity(intent);
        });
    }

    private void captureCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


            File photoFile = null;

            try {

                File tempDir = getCacheDir();


                String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
                String imageFileName = "Capture_" + timeStamp + "_";

                File tempImage = File.createTempFile(
                        imageFileName,
                        ".jpg",
                        tempDir
                );


                mCurrentPhotoPath = tempImage.getAbsolutePath();

                photoFile = tempImage;

            } catch (IOException e) {

                Log.w(TAG, "Error!", e);
            }


            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        getPackageName() + ".fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);


                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private void saveImg() {

        try {

            File storageDir = new File(getFilesDir() + "/capture");

            if (!storageDir.exists())
                storageDir.mkdirs();

            String filename = "image" +day+ ".jpg";


            File file = new File(storageDir, filename);
            boolean deleted = file.delete();
            Log.w(TAG, "Delete Dup Check : " + deleted);
            FileOutputStream output = null;

            try {
                output = new FileOutputStream(file);
                BitmapDrawable drawable = (BitmapDrawable) ivCapture.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, output);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    assert output != null;
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Log.e(TAG, "Captured Saved");
            Toast.makeText(this, "Capture Saved ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.w(TAG, "Capture Saving Error!", e);
            Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();

        }
    }

    private void loadImgArr() {
        try {

            File storageDir = new File(getFilesDir() + "/capture");

            storageDir.mkdir();
            System.out.println(storageDir.exists());
            if(!storageDir.exists()){
                storageDir.mkdir();
            }

            String filename = "image" +day+ ".jpg";

            File file = new File(storageDir, filename);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            ivCapture.setImageBitmap(bitmap);

        } catch (Exception e) {
            Log.w(TAG, "Capture loading Error!", e);
            Toast.makeText(this, "load failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            //after capture
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {

                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getContentResolver(), Uri.fromFile(file));

                        if (bitmap != null) {
                            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);



                            Bitmap rotatedBitmap = null;
                            switch (orientation) {

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotatedBitmap = rotateImage(bitmap, 90);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotatedBitmap = rotateImage(bitmap, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotatedBitmap = rotateImage(bitmap, 270);
                                    break;

                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    rotatedBitmap = bitmap;
                            }


                            ivCapture.setImageBitmap(rotatedBitmap);

                        }
                    }
                    break;
                }
            }

        } catch (Exception e) {
            Log.w(TAG, "onActivityResult Error!", e);
        }
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermission(); //권한체크
    }


    public void checkPermission() {
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (permissionCamera != PackageManager.PERMISSION_GRANTED
                || permissionRead != PackageManager.PERMISSION_GRANTED
                || permissionWrite != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "No Permissions.", Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "No Permissions", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "No Permissions", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
}