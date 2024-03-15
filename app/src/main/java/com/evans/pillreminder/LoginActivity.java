package com.evans.pillreminder;

import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    TextView registerLink, forgotPasswdLink;
    Button btnLogin;
    ImageView loginWithGoogle;
    TextInputLayout emailLayout, passwordLayout;
    private BeginSignInRequest signInRequest;
    private boolean showOneTapUI = true;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // TODO: reload()
        }
//        updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btnLogin = findViewById(R.id.btnLogin);
        registerLink = findViewById(R.id.tvRegisterLink);
        forgotPasswdLink = findViewById(R.id.tvForgotPasswordLink);
        emailLayout = findViewById(R.id.editLayoutLoginEmail);
        passwordLayout = findViewById(R.id.editLayoutLoginPassword);

        loginWithGoogle = findViewById(R.id.socialLoginGoogle);

        btnLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);
        forgotPasswdLink.setOnClickListener(this);
        loginWithGoogle.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
//                try {
//                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
//                    String idToken = credential.getGoogleIdToken();
//                    if (idToken != null) {
//                        // Got an ID token from Google. Use it to authenticate
//                        // with Firebase.
//                        Log.d(MY_TAG, "Got ID token.");
//                    }
//                } catch (ApiException e) {
                // ...
//                }
//                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvRegisterLink) {
            Intent intent = new Intent(v.getContext(), RegisterActivity.class);
            startActivity(intent);
            // don't go back onBackPress()
            this.finish();
        } else if (v.getId() == R.id.tvForgotPasswordLink) {
            Toast.makeText(v.getContext(), "Forgot password!", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.btnLogin) {
            String email = Objects.requireNonNull(emailLayout.getEditText()).getText().toString();
            String password = Objects.requireNonNull(passwordLayout.getEditText()).getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(MY_TAG, "signInWithEmail: " + task.getResult().toString());
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                            startActivity(intent);
                            this.finish();
                            // TODO: redirect to MainActivity
                        } else {
                            Log.w(MY_TAG, "signInWithEmail: ", task.getException());
                            // TODO: display logins or network error
                        }
                    });
        } else if (v.getId() == R.id.socialLoginGoogle) {
            // TODO: do sign-in using google
        }
    }
}