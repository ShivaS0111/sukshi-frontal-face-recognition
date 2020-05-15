package com.sukshi.sukshicamerademo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.sukshi.sukshicamerademo.FaceDect.previewFaceDetector;

public abstract class FaceHandleActivity extends AppCompatActivity implements FaceDect.OnMultipleFacesDetectedListener, FaceDect.OnCaptureListener {

    private static final String TAG = "Custom Camera";
    public static boolean takePicture;
    public CameraSource mCameraSource;

    // CAMERA VERSION ONE DECLARATIONS
    FaceDect faceDect;
    private Context context;
    // COMMON TO BOTH CAMERAS
    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;
    private boolean wasActivityResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());

        context = getApplicationContext();
        takePicture = false;
        mPreview = previewAuth();
        mGraphicOverlay = previewOverlay();
        createCameraSourceFront();
        startCameraSource();
    }

    abstract int layout();

    abstract CameraSourcePreview previewAuth();

    abstract GraphicOverlay previewOverlay();

    @Override
    public void onMultipleFacesDetected(int n) {
        Log.e("OnFace Detect", n + "---faces detected");
    }

    @Override
    public void onCapture(byte[] data, int angle) {
        stopCameraSource();
        Bitmap OriginalBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotatedbitmap = Bitmap.createBitmap(OriginalBitmap, 0, 0, OriginalBitmap.getWidth(), OriginalBitmap.getHeight(), matrix, true);
        saveFile(rotatedbitmap);
    }

    public void saveFile(Bitmap bitmap) {
        File file = getOutputMediaFile();
        String path = file.getPath();
        Log.e("File Path", path);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
                onSaveFile(file, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onSaveFile(File file, Bitmap bitmap) {
    }

    private void createCameraSourceFront() {
        faceDect = new FaceDect(this, mGraphicOverlay);

        mCameraSource = new CameraSource.Builder(context, previewFaceDetector)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();
        startCameraSource();
    }

    private void startCameraSource() {

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                // Log.e(TAG, "Unable to start caera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    private void stopCameraSource() {
        mPreview.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasActivityResumed) {
            createCameraSourceFront();
        }
        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();

        wasActivityResumed = true;
        stopCameraSource();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCameraSource();
    }

    public File getOutputMediaFile() {
        final String TAG = "CameraPreview";
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Camera");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        long time = System.currentTimeMillis();
        File file = new File(mediaStorageDir.getPath() + File.separator + time + ".jpg");

        return file;
    }
}