package com.evans.pillreminder.fragments;

import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.evans.pillreminder.R;
import com.evans.pillreminder.db.Medication;
import com.evans.pillreminder.db.MedicationViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class AddMedicationFragment extends Fragment implements View.OnClickListener {
    TextView tvAddMedReminder, tvAddMedDate;
    Button btnAddMedDiscard, btnAddMedSave;
    TextInputLayout tiEditTextAddMedName, editTextAddMedDose,
            editTextAddMedNote, editTextAddMedFor;
    AppCompatSpinner spinnerMedicationForm, spinnerMedPeriod;
    MedicationViewModel medicationViewModel;
    Button btnCameraAddPrescription;

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
        tiEditTextAddMedName = view.findViewById(R.id.tiEditTextAddMedName);
        spinnerMedicationForm = view.findViewById(R.id.spinnerAddMedForm);
        spinnerMedPeriod = view.findViewById(R.id.spinnerMedPeriods);
        editTextAddMedDose = view.findViewById(R.id.tiEditTextAddMedDose);
        editTextAddMedNote = view.findViewById(R.id.tiLayoutAddMedNote);
        tvAddMedDate = view.findViewById(R.id.tvAddMedDateIssued);
        editTextAddMedFor = view.findViewById(R.id.tiEditTextMedFor);
        btnCameraAddPrescription = view.findViewById(R.id.btnCameraAddPrescription);

        btnCameraAddPrescription.setOnClickListener(this);

        spinnerMedicationForm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] medicationForms = getResources().getStringArray(R.array.medication_forms);
                if (medicationForms[position].equalsIgnoreCase("suppositories")) {
                    Objects.requireNonNull(editTextAddMedDose.getEditText()).setHint(editTextAddMedDose.getEditText().getHint().toString() + " (ml)");
                } else {
                    Objects.requireNonNull(editTextAddMedDose.getEditText()).setHint(editTextAddMedDose.getEditText().getHint().toString() + " (Number)");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        medicationViewModel.getAllMedications().observe(getViewLifecycleOwner(), new Observer<List<Medication>>() {
            @Override
            public void onChanged(List<Medication> medications) {
                Toast.makeText(view.getContext(), "DataUpdated: ", Toast.LENGTH_SHORT).show();
            }
        });

        LiveData<List<Medication>> allMedications = medicationViewModel.getAllMedications();
        Log.w(MY_TAG, "AllMeds: " + allMedications.isInitialized());

        tvAddMedReminder.setOnClickListener(this);
        tvAddMedDate.setOnClickListener(this);
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
        if (v.getId() == R.id.tvAddMedDateIssued) {
            Calendar now = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                    (view, year, month, dayOfMonth) -> {
                        String date = year + "-" + "-" + month + "-" + dayOfMonth;
                        tvAddMedDate.setText(date);
                    },
                    now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else if (v.getId() == R.id.tvAddMedReminderTime) {
            Calendar now = Calendar.getInstance();

            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), (view, hourOfDay, minute) -> {
                String time = hourOfDay + ":" + minute;
                tvAddMedReminder.setText(time);
            },
                    now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        } else if (v.getId() == R.id.btnAddMedSave) {
            Toast.makeText(v.getContext(), "Button Save Clicked", Toast.LENGTH_SHORT).show();
            // verify the fields
            if (!verifyInputs(/*send the focus to the view that has not been filled*/)) {
                Toast.makeText(v.getContext(), "Error in some fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // apply the necessary pill images
            // save data to local database (SQLITE)
            String medicationName = Objects.requireNonNull(tiEditTextAddMedName.getEditText()).getText().toString();
            String medicationForm = spinnerMedicationForm.getSelectedItem().toString();
            String medicationDose = Objects.requireNonNull(editTextAddMedDose.getEditText()).getText().toString();
            String medicationDate = tvAddMedDate.getText().toString();
            String medicationReminderTime = tvAddMedReminder.getText().toString();
            String medicationFor = Objects.requireNonNull(editTextAddMedFor.getEditText()).getText().toString();
            String medicationNote = Objects.requireNonNull(editTextAddMedNote.getEditText()).getText().toString();

            Medication medication = new Medication(medicationName, medicationForm, medicationDose,
                    medicationDate, medicationReminderTime, medicationFor, medicationNote);

            medicationViewModel.insert(medication);
            LiveData<List<Medication>> allMedications = medicationViewModel.getAllMedications();
            Log.w(MY_TAG, "All: " + allMedications.getValue());
            Toast.makeText(v.getContext(), "Data Saved", Toast.LENGTH_SHORT).show();
            // if connected to internet, upload to the server (FIREBASE)

        } else if (v.getId() == R.id.btnAddMedDiscard) {
            clearViews();
            // reset spinner to point at element 0
        }else if(v.getId() == R.id.btnCameraAddPrescription){
            Toast.makeText(v.getContext(), "Opening Camera...", Toast.LENGTH_SHORT).show();

        }
    }

    private void clearViews() {
        Objects.requireNonNull(tiEditTextAddMedName.getEditText()).setText("");
        Objects.requireNonNull(editTextAddMedDose.getEditText()).setText("");
        Objects.requireNonNull(editTextAddMedNote.getEditText()).setText("");
        tvAddMedDate.setText("");
        tvAddMedReminder.setText("");
        Objects.requireNonNull(editTextAddMedFor.getEditText()).setText("");
        spinnerMedicationForm.setSelection(0);
        spinnerMedPeriod.setSelection(0);

    }

    private boolean verifyInputs() {
        if (Objects.requireNonNull(Objects.requireNonNull(tiEditTextAddMedName.getEditText()).getText()).toString().equals("")) {
            // change drawable color and add it to the errors
            tiEditTextAddMedName.setError("Provide the name of the medication");
            tiEditTextAddMedName.requestFocus();
            return false;
        }
        if (Objects.requireNonNull(Objects.requireNonNull(editTextAddMedDose.getEditText()).getText()).toString().equals("")) {
            // change drawable color and add it to the errors
            editTextAddMedDose.setError("Provide medication dosage as prescribed by the physician");
            editTextAddMedDose.requestFocus();
            return false;
        }
        if (Objects.requireNonNull(tvAddMedDate.getText()).toString().equals("")) {
            // change drawable color and add it to the errors
            tvAddMedDate.setError("Provide medication dosage as prescribed by the physician");
            tvAddMedDate.requestFocus();
            return false;
        }
        if (tvAddMedReminder.getText().equals("")) {
            // change drawable color and add it to the errors
            tvAddMedReminder.setError("Please select reminder time");
            tvAddMedReminder.requestFocus();
            return false;
        }
        // cc
        if (Objects.requireNonNull(Objects.requireNonNull(editTextAddMedFor.getEditText()).getText()).toString().equals("")) {
            // change drawable color and add it to the errors
            editTextAddMedFor.setError("Provide medication dosage as prescribed by the physician");
            editTextAddMedFor.requestFocus();
            return false;
        }
        if (Objects.requireNonNull(Objects.requireNonNull(editTextAddMedNote.getEditText()).getText()).toString().equals("")) {
            // change drawable color and add it to the errors
            editTextAddMedNote.setError("Enter a note to self or doctor's comment");
            editTextAddMedNote.requestFocus();
            return false;
        }
        return true;
    }
}
