package com.evans.pillreminder;

import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.adapters.DoctorAppointmentAdapter;

public class DoctorHomeFragment extends Fragment {

    private RecyclerView appointmentRecyclerView;

    public DoctorHomeFragment() {
        // Required empty public constructor
    }

    public static DoctorHomeFragment newInstance(String param1, String param2) {
        DoctorHomeFragment fragment = new DoctorHomeFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appointmentRecyclerView = view.findViewById(R.id.homeScheduleRecyclerView);

        DoctorAppointmentAdapter doctorAppointmentAdapter = new DoctorAppointmentAdapter();
        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentRecyclerView.setHasFixedSize(true);
        appointmentRecyclerView.setAdapter(doctorAppointmentAdapter);
        Log.i(MY_TAG, "Appointments");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_home, container, false);
    }
}