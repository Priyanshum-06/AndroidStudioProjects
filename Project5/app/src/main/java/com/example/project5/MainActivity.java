package com.example.project5;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 102;

    private Button takePhotoBtn, openGalleryBtn;
    private GridView galleryGridView;
    private ArrayList<File> imageFiles;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePhotoBtn = findViewById(R.id.takePhotoButton);
        openGalleryBtn = findViewById(R.id.openGalleryButton);
        galleryGridView = findViewById(R.id.galleryGridView);

        imageFiles = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imageFiles);
        galleryGridView.setAdapter(imageAdapter);

        takePhotoBtn.setOnClickListener(v -> {
            if (hasPermissions()) {
                openCamera();
            } else {
                requestPermissions();
            }
        });

        openGalleryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        });

        loadImages();
    }

    private boolean hasPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }


    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Create a folder for storing images
            File folder = new File(getExternalFilesDir(null), "Gallery");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Create a photo file
            File photoFile = new File(folder, "photo_" + System.currentTimeMillis() + ".jpg");
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);

            // Pass the photo URI to the camera app
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);

        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d("ImageApp", "Image captured successfully");
            loadImages();
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            loadImages();
        }
    }


    private void loadImages() {
        File folder = new File(getExternalFilesDir(null), "Gallery"); // Accessing the Gallery folder
        Log.d("ImageApp", "Loading images from folder: " + folder.getAbsolutePath());
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                imageFiles.clear();
                for (File file : files) {
                    if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                        imageFiles.add(file);
                        Log.d("ImageApp", "Image found: " + file.getAbsolutePath());
                    }
                }
                imageAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permissions denied. Please grant all permissions.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
