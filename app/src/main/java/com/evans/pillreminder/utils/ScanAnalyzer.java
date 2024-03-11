package com.evans.pillreminder.utils;

import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.evans.pillreminder.ScannerActivity;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@ExperimentalGetImage
public class ScanAnalyzer implements ImageAnalysis.Analyzer {
    private final ScannerActivity scannerActivity;
    TextRecognizer textRecognizer;
    Context context;
    StringBuilder scratchNumber = new StringBuilder();
    Matcher matcher;
    boolean canScan = true;
    volatile boolean done = false;
    private InputImage globalImage;

    public ScanAnalyzer(Context context) {
        this.context = context;
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        scannerActivity = new ScannerActivity();
    }

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        if (done)
            return;
        Image litImage = imageProxy.getImage();
        InputImage image = InputImage.fromMediaImage(Objects.requireNonNull(litImage), imageProxy.getImageInfo().getRotationDegrees());
        globalImage = image;
        Task<Text> result = textRecognizer.process(image)
                .addOnFailureListener(e -> Log.e(MY_TAG, "AnalyzerErr: " + e.getMessage()))
                .addOnCanceledListener(() -> {
                    Log.w(MY_TAG, "Canceled");
                    imageProxy.close();
                });
        result.addOnCompleteListener(task -> {
            try {
                if (!done) {
                    for (Text.TextBlock block : result.getResult().getTextBlocks()) {
                        String[] blockArray = block.getText().split(" ");
                        Matcher blockArrTextM = Pattern.compile("\\d+").matcher(blockArray[0]);
//                        Log.e(MY_TAG, "analyze: " + block.getText());
                    }
                    Log.e(MY_TAG, "analyze: " + result.getResult().getText());
                    scannerActivity.setScannedText(result.getResult().getText());
                    // save the bitmap image
                    Bitmap bitmapInternal = image.getBitmapInternal();
                    String storagePath = Objects.requireNonNull(Objects.requireNonNull(context.getExternalFilesDir(null))
                            .getParentFile()).getPath() + "/Output/";
                    File rootStorage = new File(storagePath);
                    File myFile = new File(rootStorage.getAbsolutePath() + "/pills.jpg");
                    if (!myFile.exists()) {
                        myFile.createNewFile();
                    }
                    try {
                        Log.i(MY_TAG, "analyze: MESSAGE 0");
//
                        FileOutputStream outputStream = new FileOutputStream(myFile);
                        Log.i(MY_TAG, "analyze: MESSAGE 1");
                        // TODO: fix null value returned by getByteBuffer()
//                        outputStream.write(globalImage.getByteBuffer().array());
                        Objects.requireNonNull(bitmapInternal).compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        Log.i(MY_TAG, "analyze: MESSAGE 2");
                        outputStream.close();
                        Log.i(MY_TAG, "analyze: MESSAGE 3");
                    } catch (Exception e) {
                        Log.e(MY_TAG, "Err1: " + e.getMessage());
                    }
                    // display on the edit text the recognized text
                }
            } catch (Exception e) {
                Log.e(MY_TAG, "ErrTRY: " + e.getMessage());
            }
            imageProxy.close();
        });
    }
}
