package com.evans.pillreminder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Size;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.evans.pillreminder.utils.ScanAnalyzer;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@ExperimentalGetImage
public class ScannerActivity extends AppCompatActivity {

    public static boolean dataChanged;
    // TODO: display an overlay on the camera view
    EditText recognizedMedText;
    ImageButton btnCameraFlash;
    PreviewView cameraPreview;
    CameraControl cameraControl;
    private boolean flashOn;

    private String scannedText;
    private ProcessCameraProvider cameraProvider;

    public String getScannedText() {
        return scannedText;
    }

    public void setScannedText(String scannedText) {
        this.scannedText = scannedText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
//        recognizedMedText = findViewById(R.id.recognizedCreditText);
        btnCameraFlash = findViewById(R.id.btnMedCamFlash);
        cameraPreview = findViewById(R.id.medCamPreview);

        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            int CAMERA_PERMISSION_CODE = 1;
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

        btnCameraFlash.setOnClickListener(v -> {
            if (flashOn) {
                // TODO: replace with off flash drawable
                btnCameraFlash.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_flash_on_24, null));
                cameraControl.enableTorch(false);
                flashOn = false;
            } else {
                btnCameraFlash.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_flash_on_24, null));
                cameraControl.enableTorch(true);
                flashOn = true;
            }
        });

        bindToCamera();

        new Thread(() -> {
            while (true) {
                if (dataChanged) {
                    StringBuilder rectifiedString = new StringBuilder();

                    runOnUiThread(() -> {
                        recognizedMedText.setText(rectifiedString);
                        cameraProvider.unbindAll();
//                        onBackPressed();
                    });
                    startActivity(new Intent(this, MainActivity.class));
                    this.finish();
                    dataChanged = false;
                }
            }
        }).start();
    }

    private void bindToCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        this.cameraProvider = cameraProvider;
        ScanAnalyzer imageAnalyzer = new ScanAnalyzer(this);

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
//                .setTargetResolution(new Size(176, 144))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(getMainExecutor(), imageAnalyzer);
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());
        ViewPort viewPort = cameraPreview.getViewPort();

        if (viewPort != null) {
            UseCaseGroup useCaseGroup = new UseCaseGroup.Builder()
                    .addUseCase(preview)
                    .addUseCase(imageAnalysis)
                    .setViewPort(viewPort)
                    .build();
            this.cameraProvider.unbindAll();
            Camera camera = this.cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup);//, imageAnalysis,preview);
            cameraControl = camera.getCameraControl();
            cameraControl.setLinearZoom(0f);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        flashOn = false;
    }
}
