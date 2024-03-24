package com.evans.pillreminder;

import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_COLLECTIONS_USERS;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

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

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public MainActivity activity;
    FrameLayout mainFrameLayout;
    AppCompatImageButton imgBtnHome, imgBtnNotification, imgBtnAddMed, imgBtnHistory, imgBtnContactUs, imgBtnProfile;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        getWindow().setStatusBarColor(R.color.colorPurple);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

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
            } else {
                setContentView(R.layout.activity_main);
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
            }
        }));
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment);
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
