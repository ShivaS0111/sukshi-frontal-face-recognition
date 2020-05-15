package com.sukshi.sukshicamerademo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class CameraActivity extends FaceHandleActivity {

    ImageView previewImages;
    ImageView camera;
    TextView faceStatus;

    @Override
    public int layout() {
        return R.layout.activity_camera;
    }

    @Override
    public CameraSourcePreview previewAuth() {
        return findViewById(R.id.previewAuth);
    }

    @Override
    public GraphicOverlay previewOverlay() {
        return findViewById(R.id.faceOverlayAuth);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        faceStatus = findViewById(R.id.faceStatus);
        camera = findViewById(R.id.camera);
        camera.setColorFilter(getResources().getColor(R.color.disable), PorterDuff.Mode.SRC_ATOP);

        previewImages = findViewById(R.id.preview);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture = true;
            }
        });
    }

    @Override
    protected void onSaveFile(File file, Bitmap bitmap) {
        super.onSaveFile(file, bitmap);
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onMultipleFacesDetected(int n) {
        super.onMultipleFacesDetected(n);
        if (n > 0) {
            camera.setEnabled(true);
            camera.setColorFilter(null);
            faceStatus.setText(getString(R.string.face_detected));
        } else {
            camera.setEnabled(false);
            camera.setColorFilter(getResources().getColor(R.color.disable), PorterDuff.Mode.SRC_ATOP);
            faceStatus.setText(getString(R.string.face_not_detected));
        }
    }
}
