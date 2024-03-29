package com.evans.pillreminder;

import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_COLLECTIONS_USERS;
import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_FIELD_USER_TOKEN;
import static com.evans.pillreminder.helpers.Constants.FCM_TOPIC_UPDATES;
import static com.evans.pillreminder.helpers.Constants.FILENAME_TOKEN_JSON;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;
import static com.evans.pillreminder.helpers.UtilityFunctions.saveDictionary;
import static com.evans.pillreminder.helpers.UtilityFunctions.saveTokenToFirestore;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.evans.pillreminder.db.Medication;
import com.evans.pillreminder.db.MedicationViewModel;
import com.evans.pillreminder.fragments.AddMedicationFragment;
import com.evans.pillreminder.fragments.ContactUsFragment;
import com.evans.pillreminder.fragments.HistoryFragment;
import com.evans.pillreminder.fragments.HomeFragment;
import com.evans.pillreminder.fragments.NotificationsFragment;
import com.evans.pillreminder.fragments.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals("com.evans.pillreminder.fcm.notification")) {
                loadFragment(new NotificationsFragment());
                Log.i(MY_TAG, "onReceive: Fragment Loaded");
            }
            Log.i(MY_TAG, "Broadcast: " + context.toString() + " -> " + Objects.requireNonNull(intent).getClass());
        }
    };
    public MainActivity activity;
    FrameLayout mainFrameLayout;
    AppCompatImageButton imgBtnHome, imgBtnNotification, imgBtnAddMed, imgBtnHistory, imgBtnContactUs, imgBtnProfile;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onResume() {
        super.onResume();

        Log.w(MY_TAG, "On Resume");
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(R.color.colorPurple);

        activity = this;
        Log.d(MY_TAG, "MAIN onCreate: ");

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

//        if (getIntent().getExtras() != null) {
//            Log.w(MY_TAG, "Has getIntent: ");
//            setContentView(R.layout.activity_main);
//            loadFragment(new NotificationsFragment());
//        } else {

        retrieveTheFCMToken();

        //=====================================
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.evans.pillreminder.fcm.notification");
        registerReceiver(mReceiver, filter);
        //=====================================

        DocumentReference document = firestore.collection(DB_FIRESTORE_COLLECTIONS_USERS).document(Objects.requireNonNull(user).getUid());

        document.get().addOnCompleteListener((task -> {
            if (task.getResult().get("doctor") != null) {
                setContentView(R.layout.activity_main_doctor);

                imgBtnHome = findViewById(R.id.mainBNavHome);
                imgBtnNotification = findViewById(R.id.mainBNavNotification);
                imgBtnProfile = findViewById(R.id.mainBNavProfile);

                imgBtnHome.setOnClickListener(this);
                imgBtnNotification.setOnClickListener(this);
                imgBtnProfile.setOnClickListener(this);

                loadFragment(new DoctorHomeFragment());

                if (getIntent().getExtras() != null) {
                    //
//                    setContentView(R.layout.activity_main);
                    loadFragment(new NotificationsFragment());
                }
            } else {
                setContentView(R.layout.activity_main);

                subscribeToTopic(FCM_TOPIC_UPDATES);
                mainFrameLayout = findViewById(R.id.mainFrameLayout);

                imgBtnHome = findViewById(R.id.mainBNavHome);
                imgBtnNotification = findViewById(R.id.mainBNavNotification);
                imgBtnAddMed = findViewById(R.id.mainBNavAddMedication);
                imgBtnHistory = findViewById(R.id.mainBNavHistory);
                imgBtnContactUs = findViewById(R.id.mainBNavContactUs);
                imgBtnProfile = findViewById(R.id.mainBNavProfile);

                imgBtnHome.setOnClickListener(this);
                imgBtnNotification.setOnClickListener(this);
                imgBtnAddMed.setOnClickListener(this);
                imgBtnHistory.setOnClickListener(this);
                imgBtnContactUs.setOnClickListener(this);
                imgBtnProfile.setOnClickListener(this);

                loadFragment(new HomeFragment());

                if (getIntent().getExtras() != null) {
                    //
//                    setContentView(R.layout.activity_main);
                    loadFragment(new NotificationsFragment());
                }
            }
        }));
//        }
//        if (getIntent().getExtras() != null) {
//            //
//            setContentView(R.layout.activity_main);
//            loadFragment(new NotificationsFragment());
//        }
    }

    private void retrieveTheFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(MY_TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    Log.d(MY_TAG, token);

                    saveDictionary(MainActivity.this, Map.of(DB_FIRESTORE_FIELD_USER_TOKEN, token), FILENAME_TOKEN_JSON);
                    saveTokenToFirestore(token);
                });
    }

    private void subscribeToTopic(@NonNull String topicName) {
        FirebaseMessaging.getInstance()
                .subscribeToTopic(topicName)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(MY_TAG, "Subscribed to " + topicName);
                    } else {
                        Log.e(MY_TAG, "Failed to subscribe to " + topicName);
                    }
                });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.mainFrameLayout, fragment);
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment, fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mainBNavHome) {
            loadFragment(new HomeFragment());
        } else if (v.getId() == R.id.mainBNavNotification) {
            loadFragment(new NotificationsFragment());
        } else if (v.getId() == R.id.mainBNavAddMedication) {
            loadFragment(new AddMedicationFragment());
        } else if (v.getId() == R.id.mainBNavHistory) {
            MedicationViewModel medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);

            medicationViewModel.getAllMedications().observe(this, new Observer<List<Medication>>() {
                @Override
                public void onChanged(List<Medication> medications) {
                    //
                }
            });

            LiveData<List<Medication>> allMedications = medicationViewModel.getAllMedications();
            if (allMedications.getValue() != null) {
                Log.w(MY_TAG, "Saved Data: " + allMedications.getValue().size());
            } else {
                Log.e(MY_TAG, "ERR: NULL VALUE REF");
            }
            loadFragment(new HistoryFragment());
        } else if (v.getId() == R.id.mainBNavContactUs) {
            loadFragment(new ContactUsFragment());
        } else if (v.getId() == R.id.mainBNavProfile) {
            loadFragment(new ProfileFragment());
        }
    }
}
