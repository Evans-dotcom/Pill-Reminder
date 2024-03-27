package com.evans.pillreminder;

import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_COLLECTIONS_USERS;
import static com.evans.pillreminder.helpers.Constants.FILENAME_USER_DETAILS_JSON;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;
import static com.evans.pillreminder.helpers.UtilityFunctions.saveDictionary;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.evans.pillreminder.db.User;
import com.evans.pillreminder.db.UserViewModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    TextView registerLink, forgotPasswdLink;
    Button btnLogin;
    SignInButton loginWithGoogle;
    TextInputLayout emailLayout, passwordLayout;
    private BeginSignInRequest signInRequest;
    private boolean showOneTapUI = true;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // TODO: check if the userUID is in the database user table
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

        loginWithGoogle = findViewById(R.id.btnGoogleSignin);

        btnLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);
        forgotPasswdLink.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
//                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
//                    String idToken = credential.getGoogleIdToken();
//                    if (idToken != null) {
//                        // Got an ID token from Google. Use it to authenticate
//                        // with Firebase.
//                        Log.d(MY_TAG, "Got ID token.");
//                    }
                break;
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            // TODO: store user email, email as password
            auth.createUserWithEmailAndPassword(Objects.requireNonNull(Objects.requireNonNull(acct).getEmail()), acct.getEmail()).addOnSuccessListener((task) -> {
                DocumentReference document = firestore.collection(DB_FIRESTORE_COLLECTIONS_USERS)
                        .document(Objects.requireNonNull(task.getUser()).getUid());

                Map<String, Object> user = Map.of(
                        "firstName", Objects.requireNonNull(acct.getGivenName()),
                        "lastName", Objects.requireNonNull(acct.getFamilyName()),
                        "email", Objects.requireNonNull(acct.getEmail()),
                        "dp", Objects.requireNonNull(acct.getPhotoUrl())
                );

                document.set(user).addOnSuccessListener(aTask -> {
                    //
                    saveDictionary(this, user, FILENAME_USER_DETAILS_JSON);
                });

                Log.d(MY_TAG, "handleSignInResult: " + result.getStatus() + " " + Objects.requireNonNull(acct).getDisplayName());
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
            });
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

                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            String uid = Objects.requireNonNull(user).getUid();

                            DocumentReference mDocument = firestore.collection(DB_FIRESTORE_COLLECTIONS_USERS).document(uid);
                            // store the user in the database
                            mDocument.get().addOnSuccessListener(snapshot -> {
                                boolean gender = !Objects.requireNonNull(snapshot.get("genderID")).equals(0);
                                String firstName = (Objects.requireNonNull(snapshot.get("firstName")).toString());
                                String lastName = (Objects.requireNonNull(snapshot.get("lastName")).toString());
                                String phone = (Objects.requireNonNull(snapshot.get("mobileNumber")).toString());
                                String uEmail = (Objects.requireNonNull(snapshot.get("email")).toString());
                                String username = (Objects.requireNonNull(snapshot.get("username")).toString());
                                String imageURL = "";

                                Log.i(MY_TAG, "Data: " + snapshot.getData().toString());

                                User dbUser = new User(firstName, lastName, phone, uEmail, gender, username, imageURL, uid);
                                UserViewModel userViewModel = new UserViewModel((Application) getApplicationContext());
                                userViewModel.deleteAny();
                                userViewModel.insertFirst(dbUser);

                                saveDictionary(this, snapshot.getData(), FILENAME_USER_DETAILS_JSON);

                                Intent intent = new Intent(v.getContext(), MainActivity.class);
                                startActivity(intent);
                                this.finish();
                            });
                            // TODO: redirect to MainActivity
                        } else {
                            Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            Log.w(MY_TAG, "signInWithEmail: ", task.getException());
                            // TODO: display logins or network error
                        }
                    });
        } else if (v.getId() == R.id.btnGoogleSignin) {
            // TODO: do sign-in using google
            signIn();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, REQ_ONE_TAP);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}