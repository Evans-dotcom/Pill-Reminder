package com.evans.pillreminder.fragments;

import static com.evans.pillreminder.helpers.Constants.MONTHS;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.R;
import com.evans.pillreminder.adapters.MedicationRecyclerAdapter;
import com.evans.pillreminder.db.MedicationViewModel;

import java.util.Calendar;

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
        });

        selectedDate.setOnClickListener(this);
        btnPrevDate.setOnClickListener(this);
        btnNextDate.setOnClickListener(this);

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
