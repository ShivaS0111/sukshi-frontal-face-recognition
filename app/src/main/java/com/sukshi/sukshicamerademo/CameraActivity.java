package com.sukshi.sukshicamerademo;

/*
 * Vishwam Corp CONFIDENTIAL

 * Vishwam Corp 2018
 * All Rights Reserved.

 * NOTICE:  All information contained herein is, and remains
 * the property of Vishwam Corp. The intellectual and technical concepts contained
 * herein are proprietary to Vishwam Corp
 * and are protected by trade secret or copyright law of U.S.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Vishwam Corp
 */

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
    int layout() {
        return R.layout.activity_camera;
    }

    @Override
    CameraSourcePreview previewAuth() {
        return findViewById(R.id.previewAuth);
    }

    @Override
    GraphicOverlay previewOverlay() {
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
