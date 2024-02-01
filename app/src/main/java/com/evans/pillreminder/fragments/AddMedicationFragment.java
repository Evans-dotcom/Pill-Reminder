package com.evans.pillreminder.fragments;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.evans.pillreminder.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;

public class AddMedicationFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    TextView tvAddMedReminder;
    Button btnAddMedDiscard, btnAddMedSave;
    TextInputEditText editTextAddMedName, editTextAddMedDose, editTextAddMedNote;
    AppCompatSpinner spinnerMedicationForm;

    public AddMedicationFragment() {
        // Required empty public constructor
    }

    public static AddMedicationFragment newInstance(String param1, String param2) {
        AddMedicationFragment fragment = new AddMedicationFragment();
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
        return inflater.inflate(R.layout.fragment_add_medication, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvAddMedReminder = view.findViewById(R.id.tvAddMedReminderTime);
        btnAddMedSave = view.findViewById(R.id.btnAddMedSave);
        btnAddMedDiscard = view.findViewById(R.id.btnAddMedDiscard);
        editTextAddMedName = view.findViewById(R.id.tiEditTextAddMedName);
        spinnerMedicationForm = view.findViewById(R.id.spinnerAddMedForm);
        editTextAddMedDose = view.findViewById(R.id.tiEditTextAddMedDose);
        editTextAddMedNote = view.findViewById(R.id.tiEditTextAddMedNote);

        tvAddMedReminder.setOnClickListener(this);
        btnAddMedSave.setOnClickListener(this);
        btnAddMedDiscard.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvAddMedReminderTime) {
            Calendar now = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), this,
                    now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        } else if (v.getId() == R.id.btnAddMedSave) {
            // verify the fields
            if (!verifyInputs(/*send the focus to the view that has not been filled*/)) {
                return;
            }
            // apply the necessary pill images

            // save data to local database (SQLITE)

            // if connected to internet, upload to the server (FIREBASE)

        } else if (v.getId() == R.id.btnAddMedDiscard) {
            editTextAddMedName.setText("");
            editTextAddMedDose.setText("");
            editTextAddMedNote.setText("");
            // reset spinner to point at element 0
        }
    }

    private boolean verifyInputs() {
        if (Objects.requireNonNull(editTextAddMedName.getText()).toString().equals("")) {
            // change drawable color and add it to the errors
            editTextAddMedName.setError("Provide the name of the medication");
            editTextAddMedName.setTextInputLayoutFocusedRectEnabled(true);
            return false;
        }
        if (Objects.requireNonNull(editTextAddMedDose.getText()).toString().equals("")) {
            // change drawable color and add it to the errors
            editTextAddMedDose.setError("Provide medication dosage as prescribed by the physician");
            editTextAddMedDose.setTextInputLayoutFocusedRectEnabled(true);
            return false;
        }
        if (tvAddMedReminder.getText().equals("")) {
            // change drawable color and add it to the errors
            tvAddMedReminder.setError("Please select reminder time");
            return false;
        }
        if (Objects.requireNonNull(editTextAddMedNote.getText()).toString().equals("")) {
            // change drawable color and add it to the errors
            editTextAddMedNote.setError("Enter a note to self or doctor's comment");
            editTextAddMedNote.setTextInputLayoutFocusedRectEnabled(true);
            return false;
        }
        return true;
    }

    /**
     * Called when the user is done setting a new time and the dialog has
     * closed.
     *
     * @param view      the view associated with this listener
     * @param hourOfDay the hour that was set
     * @param minute    the minute that was set
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        tvAddMedReminder.setText(hourOfDay + ":" + minute);
    }
}
