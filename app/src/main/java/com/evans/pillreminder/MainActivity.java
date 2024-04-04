package com.evans.pillreminder;

import static com.evans.pillreminder.helpers.Constants.CHANNEL_FIREBASE_CLOUD_MESSAGING_NOTIFICATION_ID_INCOMING;
import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_COLLECTIONS_USERS;
import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_FIELD_USER_TOKEN;
import static com.evans.pillreminder.helpers.Constants.FCM_TOPIC_UPDATES;
import static com.evans.pillreminder.helpers.Constants.FILENAME_TOKEN_JSON;
import static com.evans.pillreminder.helpers.Constants.FIREBASE_CLOUD_MESSAGING_NOTIFICATION_POP_UP_ID_INCOMING;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;
import static com.evans.pillreminder.helpers.Constants.OFFLINE_NOTIFICATION_REMINDER_ID;
import static com.evans.pillreminder.helpers.UtilityFunctions.saveDictionary;
import static com.evans.pillreminder.helpers.UtilityFunctions.saveTokenToFirestore;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.evans.pillreminder.db.Medication;
import com.evans.pillreminder.db.MedicationViewModel;
import com.evans.pillreminder.fragments.AddMedicationFragment;
import com.evans.pillreminder.fragments.ContactUsFragment;
import com.evans.pillreminder.fragments.HistoryFragment;
import com.evans.pillreminder.fragments.HomeFragment;
import com.evans.pillreminder.fragments.NotificationsFragment;
import com.evans.pillreminder.fragments.ProfileFragment;
import com.evans.pillreminder.helpers.Constants;
import com.evans.pillreminder.services.NotificationBroadcast;
import com.evans.pillreminder.services.NotificationWorker;
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
            if (intent != null && Objects.equals(intent.getAction(), "com.evans.pillreminder.fcm.notification")) {
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

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(R.color.colorPurple);

        activity = this;
        Log.d(MY_TAG, "MAIN onCreate: ");

        registerNotification();

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //==================================================================================
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = String.valueOf(FIREBASE_CLOUD_MESSAGING_NOTIFICATION_POP_UP_ID_INCOMING);
            String channelName = CHANNEL_FIREBASE_CLOUD_MESSAGING_NOTIFICATION_ID_INCOMING;

            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        //==================================================================================

        // FIXME
        if (user == null) {
            Log.i(MY_TAG, "MainUserNull");
            FirebaseFirestore.getInstance().clearPersistence();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Log.i(MY_TAG, "MainUser: " + user.getDisplayName());
        }
        Log.i(MY_TAG, "MainPass: " + user);

//        if (getIntent().getExtras() != null) {
//            Log.w(MY_TAG, "Has getIntent: ");
//            setContentView(R.layout.activity_main);
//            loadFragment(new NotificationsFragment());
//        } else {

        retrieveTheFCMToken();

        //=====================================
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.evans.pillreminder.fcm.notification");
        NotificationBroadcast notificationBroadcast = new NotificationBroadcast();
        registerReceiver(notificationBroadcast, filter, Context.RECEIVER_NOT_EXPORTED);
        //=====================================


        //=====================================
        WorkManager workManager = WorkManager.getInstance();
        workManager.enqueue(new OneTimeWorkRequest.Builder(NotificationWorker.class).build());
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
                    Intent intent = getIntent();
//                    Bundle extras = getIntent().getExtras();
                    String senderID = intent.getStringExtra("senderID");
                    String messageID = intent.getStringExtra("messageID");
                    String message = intent.getStringExtra("message");
                    long sentTime = intent.getLongExtra("sentTime", 0);

//                    Object clone = extras.clone();
//                    extras.setClassLoader(RemoteMessage.class.getClassLoader());
//                    Parcelable message2 = extras.getParcelable("msg");
                    Log.d(MY_TAG, intent + " > " + intent.getStringExtra("message") + "Received Message: " + message + " ID: " + messageID + " senderID: " + senderID + " sentTime: " + sentTime);

                    Bundle extras1 = getIntent().getExtras();
                    if (extras1 == null) {
                        Log.i(MY_TAG, "EXTRA: NULL");
                        return;
                    }
                    String senderID1 = extras1.getString("senderID");
                    String messageID1 = extras1.getString("messageID");
                    String message1 = extras1.getString("message");
                    long sentTime1 = extras1.getLong("sentTime");

                    Log.d(MY_TAG, "Received Message: " + message1 + " ID: " + messageID1 + " senderID: " + senderID1 + " sentTime: " + sentTime1);


//                    loadFragment(new NotificationsFragment());
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

    private void registerNotification() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = Constants.OFFLINE_NOTIFICATIONS_REMINDER_NAME;
            String description = Constants.OFFLINE_NOTIFICATIONS_REMINDER_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.OFFLINE_NOTIFICATION_REMINDER_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        triggerNotification(this);
    }

    private void triggerNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.OFFLINE_NOTIFICATION_REMINDER_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Upcoming")
                .setContentText("This feature will be implemented soon!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                        int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // notificationId is a unique int for each notification that you must define.
//        notify(NOTIFICATION_ID, builder.build());
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(Integer.parseInt(OFFLINE_NOTIFICATION_REMINDER_ID), builder.build());
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
