package com.evans.pillreminder.helpers;

import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_COLLECTIONS_USERS;
import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_FIELD_USER_TOKEN;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class UtilityFunctions {

    /**
     * Store User data in the file system
     */
    public static void saveDictionary(Activity activity, Map<String, Object> data, String fileName) {
        Context context = activity.getApplicationContext();
        JSONObject json = new JSONObject();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            try {
                json.put(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                Log.e(MY_TAG, "Map Mapping error: " + Objects.requireNonNull(e.getMessage()));
            }
        }

        String storagePath = Objects.requireNonNull(Objects.requireNonNull(context.getExternalFilesDir(null))
                .getParentFile()).getPath() + "/SaveData/";

        Log.i(MY_TAG, "PATH: " + Objects.requireNonNull(Objects.requireNonNull(context.getExternalFilesDir(null)).getParentFile()).getPath());

        File file = new File(storagePath + fileName);

        if (!Objects.requireNonNull(file.getParentFile()).exists())
            if (!file.getParentFile().mkdirs()) {
                Log.e(MY_TAG, "Folder not created");
            }
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(json.toString(4).getBytes());
            outputStream.close();
            Log.i(MY_TAG, "File Saved");
//            activity.runOnUiThread(() -> Toast.makeText(activity, "File Saved",
//                    Toast.LENGTH_SHORT).show());
        } catch (IOException | JSONException e) {
            Log.e(MY_TAG, Objects.requireNonNull(e.getMessage()));
//                throw new RuntimeException(e);
        }
    }

    public static void saveTokenToFirestore(String data) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DocumentReference mDocument = db.collection(DB_FIRESTORE_COLLECTIONS_USERS).document(Objects.requireNonNull(auth.getUid()));

        mDocument.update(DB_FIRESTORE_FIELD_USER_TOKEN, data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(MY_TAG, "Token saved to Firestore");
                    } else {
                        Log.e(MY_TAG, "Token not saved to Firestore: " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public static Map<String, Object> readDictionaryFile(Activity context, String fileName) {
        Map<String, Object> userData = new HashMap<>();
        JSONObject json = new JSONObject();

        String storagePath = Objects.requireNonNull(Objects.requireNonNull(context.getExternalFilesDir(null))
                .getParentFile()).getPath() + "/SaveData/";

        Log.i(MY_TAG, "PATH: " + Objects.requireNonNull(Objects.requireNonNull(context.getExternalFilesDir(null)).getParentFile()).getPath());

        File file = new File(storagePath + fileName);

        try {
            FileInputStream inputStream = new FileInputStream(file);

            byte[] readData = new byte[inputStream.available()];
            int read = inputStream.read(readData);

            StringBuilder stringBuilder = new StringBuilder();

            for (byte b : readData) {
                stringBuilder.append((char) b);
            }

            try {
                // create json object using data in string builder
                json = new JSONObject(stringBuilder.toString());


                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Log.i(MY_TAG, "readDictionaryFile: " + json.get(key));
                    userData.put(key, json.get(key));
                }


            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            Log.i(MY_TAG, "readDictionaryFile: " + json);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userData;
    }

}
