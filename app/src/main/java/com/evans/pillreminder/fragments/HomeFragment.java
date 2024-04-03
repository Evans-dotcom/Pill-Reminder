package com.evans.pillreminder.fragments;

import static com.evans.pillreminder.helpers.Constants.MONTHS;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;
import static com.evans.pillreminder.helpers.Constants.OFFLINE_NOTIFICATION_REMINDER_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.MainActivity;
import com.evans.pillreminder.R;
import com.evans.pillreminder.adapters.MedicationRecyclerAdapter;
import com.evans.pillreminder.db.MedicationViewModel;
import com.evans.pillreminder.helpers.Constants;
import com.evans.pillreminder.services.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    RecyclerView medicationRecyclerView;
    TextView selectedDate;
    AppCompatImageButton btnPrevDate, btnNextDate;
    private MedicationViewModel medicationViewModel;
    private Calendar today;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedDate = view.findViewById(R.id.selectedDate);
        btnNextDate = view.findViewById(R.id.homeFragCalendarNext);
        btnPrevDate = view.findViewById(R.id.homeFragCalendarPrev);

        // view model
        medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        medicationViewModel.getAllMedications().observe(getViewLifecycleOwner(), medications -> {
            // update the UI
        });
        // end view model

        today = Calendar.getInstance();
        selectedDate.setText(today.get(Calendar.DAY_OF_MONTH) + " " +
                MONTHS[today.get(Calendar.MONTH)]
                + " " + today.get(Calendar.YEAR));

        medicationRecyclerView = view.findViewById(R.id.homePlanRecyclerView);
        medicationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        medicationRecyclerView.setHasFixedSize(true);

        MedicationRecyclerAdapter medicationRecyclerAdapter = new MedicationRecyclerAdapter();
        medicationRecyclerView.setAdapter(medicationRecyclerAdapter);

        medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        medicationViewModel.getAllMedications().observe(getViewLifecycleOwner(), medications -> {
            // update the cached copy of the words in the adapter
            medicationRecyclerAdapter.setMedications(medications);
            medicationRecyclerAdapter.notifyDataSetChanged(); // FIXME

            // TODO: iterate all mediactaion and set the time
            AlarmManager alarmManager = (AlarmManager) this.requireActivity().getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(this.requireActivity(), AlarmReceiver.class);

            //
            Log.i(MY_TAG, "doWork: is running in the background: " + (medications));

            if (!medications.isEmpty()) {
                medications.forEach(med -> {
                    //
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    try {
                        long timeMillis = Objects.requireNonNull(format.parse(med.getMedicationReminderTime())).getTime();

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity().getApplicationContext(),
                                0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent);
                        // TODO: +- 20 minutes
                        if (System.currentTimeMillis() - timeMillis <= (20 * 60 * 1000) ||
                                System.currentTimeMillis() <= timeMillis + (20 * 60 * 1000)) {
                            triggerNotification(this.requireActivity().getApplicationContext());
                        }
                    } catch (ParseException e) {
//                    throw new RuntimeException(e);
                        e.printStackTrace();
                    }
                });
            }
        });

        selectedDate.setOnClickListener(this);
        btnPrevDate.setOnClickListener(this);
        btnNextDate.setOnClickListener(this);

    }

    private void triggerNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.OFFLINE_NOTIFICATION_REMINDER_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
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
        Log.i(MY_TAG, "triggerNotification: Alarm triggered");
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.selectedDate) {
            Calendar today = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                    this, today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else if (v.getId() == R.id.homeFragCalendarPrev) {
            int yesterdayDate = today.get(Calendar.DAY_OF_MONTH) - 1;
            Log.i(MY_TAG, "DATE: " + yesterdayDate);
            today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), yesterdayDate);

            String date = today.get(Calendar.DAY_OF_MONTH) + " " + MONTHS[today.get(Calendar.MONTH)]
                    + " " + today.get(Calendar.YEAR);

            selectedDate.setText(date);
        } else if (v.getId() == R.id.homeFragCalendarNext) {
            int yesterdayDate = today.get(Calendar.DAY_OF_MONTH) + 1;

            today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), yesterdayDate);

            String date = today.get(Calendar.DAY_OF_MONTH) + " " + MONTHS[today.get(Calendar.MONTH)]
                    + " " + today.get(Calendar.YEAR);

            selectedDate.setText(date);
        }
    }

    /**
     * @param view       the picker associated with the dialog
     * @param year       the selected year
     * @param month      the selected month (0-11 for compatibility with
     *                   {@link Calendar#MONTH})
     * @param dayOfMonth the selected day of the month (1-31, depending on
     *                   month)
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int i = view.getFirstDayOfWeek();
        Log.d(MY_TAG, "onDateSet: " + i);
        selectedDate.setText(/*DAYS_OF_WEEK[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)] + ", " + */
                dayOfMonth + " " + MONTHS[month] + " " + year);
    }
}
