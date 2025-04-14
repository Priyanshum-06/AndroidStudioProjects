package com.example.project5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class GalleryActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView imageName, imagePath, imageSize, imageDate;
    private Button deleteButton;
    private File currentImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        imageView = findViewById(R.id.imageView);
        imageName = findViewById(R.id.imageName);
        imagePath = findViewById(R.id.imagePath);
        imageSize = findViewById(R.id.imageSize);
        imageDate = findViewById(R.id.imageDate);
        deleteButton = findViewById(R.id.deleteButton);

        Intent intent = getIntent();
        Uri imageUri = intent.getParcelableExtra("imageUri");

        if (imageUri == null) {
            Toast.makeText(this, "No image provided", Toast.LENGTH_LONG).show();
            finish();  // End the activity if there's no image
            return;
        }

        currentImageFile = new File(imageUri.getPath());

        if (!currentImageFile.exists()) {
            Toast.makeText(this, "Image file not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        imageView.setImageURI(imageUri);
        imageName.setText("Name: " + currentImageFile.getName());
        imagePath.setText("Path: " + currentImageFile.getAbsolutePath());
        imageSize.setText("Size: " + currentImageFile.length() + " bytes");
        imageDate.setText("Date: " + currentImageFile.lastModified());

        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this image?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (currentImageFile.delete()) {
                            Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Failed to delete image", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}
