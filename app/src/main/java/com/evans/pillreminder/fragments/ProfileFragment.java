package com.evans.pillreminder.fragments;

import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.evans.pillreminder.LoginActivity;
import com.evans.pillreminder.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    ImageView profileImage;
    TextInputLayout firstName, lastName, email, username, phoneNumber;
    Button btnLogout, btnUpdate;
    ImageButton profileImageSelect;

    FirebaseAuth mAuth;
    private FragmentActivity context;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        mAuth = FirebaseAuth.getInstance();

        context = this.getActivity();

        profileImage = view.findViewById(R.id.profileImage);
        firstName = view.findViewById(R.id.editLayoutProfileFirstName);
        lastName = view.findViewById(R.id.editLayoutProfileLastName);
        email = view.findViewById(R.id.editLayoutProfileEmail);
        username = view.findViewById(R.id.editLayoutProfileUsername);
        phoneNumber = view.findViewById(R.id.editLayoutProfilePhoneNumber);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnUpdate = view.findViewById(R.id.btnProfileUpdate);
        profileImageSelect = view.findViewById(R.id.profileSelectImage);

        btnLogout.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        profileImageSelect.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogout) {
            mAuth.signOut();
            // TODO: logout user that joined using google
//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestIdToken(getString(R.string.default_web_client_id))
//                    .requestEmail()
//                    .build();
//
//            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(v.getContext())
//                    .enableAutoManage(context, this)
//                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                    .build();
//            Auth.GoogleSignInApi.signOut(googleApiClient).addStatusListener((status) -> {
//                //
//                Log.i(MY_TAG, "LogOut Success: " + status + "=> " + status.getStatusMessage());
//            });

            startActivity(new Intent(v.getContext(), LoginActivity.class));
            context.finish();
        } else if (v.getId() == R.id.btnProfileUpdate) {
        } else if (v.getId() == R.id.profileSelectImage) {
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(MY_TAG, "onConnectionFailed: " + connectionResult);
    }
}