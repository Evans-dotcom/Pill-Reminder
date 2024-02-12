package com.evans.pillreminder;

import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

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

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FrameLayout mainFrameLayout;
    public MainActivity activity;
    AppCompatImageButton imgBtnHome, imgBtnNotification, imgBtnAddMed, imgBtnHistory, imgBtnContactUs;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        getWindow().setStatusBarColor(R.color.colorPurple);

        mainFrameLayout = findViewById(R.id.mainFrameLayout);

        imgBtnHome = findViewById(R.id.mainBNavHome);
        imgBtnNotification = findViewById(R.id.mainBNavNotification);
        imgBtnAddMed = findViewById(R.id.mainBNavAddMedication);
        imgBtnHistory = findViewById(R.id.mainBNavHistory);
        imgBtnContactUs = findViewById(R.id.mainBNavContactUs);

        imgBtnHome.setOnClickListener(this);
        imgBtnNotification.setOnClickListener(this);
        imgBtnAddMed.setOnClickListener(this);
        imgBtnHistory.setOnClickListener(this);
        imgBtnContactUs.setOnClickListener(this);
        // TODO: Add fragment to mainFrameLayout
        loadFragment(new HomeFragment());
    }

    /**
     * Load the fragments to the frame layout
     *
     * @param fragment The fragment that you want to load to the view
     */
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mainBNavHome) {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            loadFragment(new HomeFragment());
        } else if (v.getId() == R.id.mainBNavNotification) {
            Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
            loadFragment(new NotificationsFragment());
        } else if (v.getId() == R.id.mainBNavAddMedication) {
            Toast.makeText(this, "Add Med", Toast.LENGTH_SHORT).show();
            loadFragment(new AddMedicationFragment());
        } else if (v.getId() == R.id.mainBNavHistory) {
            MedicationViewModel medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);

            medicationViewModel.getAllMedications().observe(this, new Observer<List<Medication>>() {
                @Override
                public void onChanged(List<Medication> medications) {
                    Toast.makeText(v.getContext(), "DataUpdated: " + medications.size(), Toast.LENGTH_SHORT).show();
                }
            });

            LiveData<List<Medication>> allMedications = medicationViewModel.getAllMedications();
            if (allMedications.getValue() != null) {
                Log.w(MY_TAG, "Saved Data: " + allMedications.getValue().size());
            } else {
                Log.e(MY_TAG, "ERR: NULL VALUE REF");
            }
            Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
            loadFragment(new HistoryFragment());
        } else if (v.getId() == R.id.mainBNavContactUs) {
            Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
            loadFragment(new ContactUsFragment());
        }
    }
}
