package com.evans.pillreminder;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.evans.pillreminder.fragments.AddMedicationFragment;
import com.evans.pillreminder.fragments.ContactUsFragment;
import com.evans.pillreminder.fragments.HistoryFragment;
import com.evans.pillreminder.fragments.HomeFragment;
import com.evans.pillreminder.fragments.NotificationsFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FrameLayout mainFrameLayout;
    AppCompatImageButton imgBtnHome, imgBtnNotification, imgBtnAddMed, imgBtnHistory, imgBtnContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
            loadFragment(new HistoryFragment());
        } else if (v.getId() == R.id.mainBNavContactUs) {
            Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
            loadFragment(new ContactUsFragment());
        }
    }
}
