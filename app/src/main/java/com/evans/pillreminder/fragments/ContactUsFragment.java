package com.evans.pillreminder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.evans.pillreminder.R;
import com.evans.pillreminder.adapters.ContactUsPagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ContactUsFragment extends Fragment {
    String[] tabTitles = {"Appointments", "Messages"};
    ViewPager2 viewPager;

    TabLayout tabLayoutHeader;
    private ContactUsPagerAdapter pagerAdapter;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
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
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayoutHeader = view.findViewById(R.id.tabLayoutHeader);

        viewPager = view.findViewById(R.id.contactViewPager);
        pagerAdapter = new ContactUsPagerAdapter(this.requireActivity().getSupportFragmentManager(), this.requireActivity().getLifecycle());
        pagerAdapter.addFragment(new AppointmentFragment());
        pagerAdapter.addFragment(new MessagesFragment());

        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayoutHeader, viewPager, (tab, i) -> tab.setText(tabTitles[i])).attach();
    }

}
