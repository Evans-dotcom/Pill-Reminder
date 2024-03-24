package com.evans.pillreminder.adapters;

import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.evans.pillreminder.fragments.AppointmentFragment;
import com.evans.pillreminder.fragments.MessagesFragment;

import java.util.ArrayList;

public class ContactUsPagerAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();


    public ContactUsPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ContactUsPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.i(MY_TAG, "FragPosition: "+position);
//        switch (position) {
//            case 0:
//                return new AppointmentFragment();
//            case 1:
//                return new MessagesFragment();
//            default:
//                return null; // unreachable
//        }
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }
}
