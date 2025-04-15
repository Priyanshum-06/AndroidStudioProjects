package com.example.project5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;

import com.example.mad_project.R;

public class MainActivity extends AppCompatActivity implements CameraManager.CameraCallback {

    Button btnClickPhoto;
    ImageView imageView;
    private PermissionManager permissionManager;
    private String[] permissions = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private CameraManager cameraManager;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnClickPhoto = findViewById(R.id.btnClickPhoto);
        imageView = findViewById(R.id.imageView);

        permissionManager = PermissionManager.getInstance(this);
        cameraManager = CameraManager.getInstance(this);

        btnClickPhoto.setOnClickListener(v -> {
            if (!permissionManager.checkPermissions(permissions)) {
                permissionManager.askPermissions(MainActivity.this, permissions, 100);
            } else {
                //permission granted
                cameraManager.openCamera();
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode,
                                              @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraManager.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Log.d("TAG","1");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                //imageView.setImageBitmap(bitmap);

                cameraManager.setPic(imageView);
                cameraManager.addPicToGallery(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            permissionManager.handlePermissionResult(MainActivity.this, 100, permissions,
                    grantResults);

            //permission granted
            cameraManager.openCamera();
        }
    }

    @Override public void getPhotoUri(Uri photoUri, Intent takePictureIntent,
                                      int REQUEST_IMAGE_CAPTURE) {
        this.photoUri = photoUri;
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
}

