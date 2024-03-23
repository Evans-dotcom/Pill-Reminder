package com.evans.pillreminder;

import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_COLLECTIONS_USERS;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvLoginLink;
    TextInputLayout firstName, lastName, emailAddress, username, mobileNumber, password, confirmPassword;
    AppCompatSpinner genderSpinner;
    Button btnRegister;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        tvLoginLink = findViewById(R.id.tvLoginLink);
        firstName = findViewById(R.id.editLayoutFirstName);
        lastName = findViewById(R.id.editLayoutLastName);
        emailAddress = findViewById(R.id.editLayoutEmail);
        username = findViewById(R.id.editLayoutUsername);
        mobileNumber = findViewById(R.id.editLayoutPhone);
        password = findViewById(R.id.editLayoutPassword);
        confirmPassword = findViewById(R.id.editLayoutConfirmPassword);
        genderSpinner = findViewById(R.id.registerGenderSpinner);
        btnRegister = findViewById(R.id.btnRegister);


        tvLoginLink.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvLoginLink) {
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else if (v.getId() == R.id.btnRegister) {
            if (validInputs()) {
                // register to the firebase and log in
                Log.i(MY_TAG, "All fields valid:");
                String email = Objects.requireNonNull(emailAddress.getEditText()).getText().toString();
                String strPassword = Objects.requireNonNull(password.getEditText()).getText().toString();

                mAuth.createUserWithEmailAndPassword(email, strPassword)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.i(MY_TAG, "User registered successfully");
                                // save user data to the firebase FireStore
                                mAuth.signInWithEmailAndPassword(email, strPassword)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Log.i(MY_TAG, "User logged in successfully");
                                                DocumentReference userDocument = firestore.collection(
                                                        DB_FIRESTORE_COLLECTIONS_USERS)
                                                        .document(Objects.requireNonNull(task1.getResult().getUser()).getUid());

                                                Log.i(MY_TAG, "UID: " + Objects.requireNonNull(task1.getResult().getUser()).getUid());

                                                Map<String, Object> user = Map.of(
                                                        "firstName", Objects.requireNonNull(firstName.getEditText()).getText().toString(),
                                                        "lastName", Objects.requireNonNull(lastName.getEditText()).getText().toString(),
                                                        "email", email,
                                                        "username", Objects.requireNonNull(username.getEditText()).getText().toString(),
                                                        "mobileNumber", Objects.requireNonNull(mobileNumber.getEditText()).getText().toString(),
                                                        "genderID", genderSpinner.getSelectedItemPosition());

                                                userDocument.set(user).addOnSuccessListener(aVoid -> {
                                                    Log.i(MY_TAG, "User data saved successfully");
                                                    Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification();

                                                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                                                    startActivity(intent);
                                                    this.finish();
                                                }).addOnFailureListener(e -> Log.e(MY_TAG, "User data save failed: " + e.getMessage()));

                                            } else {
                                                Log.e(MY_TAG, "User login failed: " + Objects.requireNonNull(task1.getException()).getMessage());
                                            }
                                        });
                            } else {
                                Log.e(MY_TAG, "User registration failed: " + Objects.requireNonNull(task.getException()).getMessage());
                            }
                        });
            }
        }
    }

    private boolean validInputs() {
        if (Objects.requireNonNull(firstName.getEditText()).getText().toString().isEmpty() || firstName.getEditText().getText().toString().length() < 3) {
            firstName.setError("First name is required");
            firstName.requestFocus();
            return false;
        } else {
            firstName.setError(null);
        }

        if (Objects.requireNonNull(lastName.getEditText()).getText().toString().isEmpty() || lastName.getEditText().getText().toString().length() < 3) {
            lastName.setError("Last name is required");
            lastName.requestFocus();
            return false;
        } else {
            lastName.setError(null);
        }

        if (Objects.requireNonNull(emailAddress.getEditText()).getText().toString().isEmpty() || emailAddress.getEditText().getText().toString().length() < 3) {
            emailAddress.setError("Email address is required");
            emailAddress.requestFocus();
            return false;
        } else {
            emailAddress.setError(null);
        }

        if (Objects.requireNonNull(username.getEditText()).getText().toString().isEmpty() || username.getEditText().getText().toString().length() < 3) {
            username.setError("Username is required");
            username.requestFocus();
            return false;
        } else {
            username.setError(null);
        }

        if (Objects.requireNonNull(mobileNumber.getEditText()).getText().toString().isEmpty() || mobileNumber.getEditText().getText().toString().length() < 10) {
            // number format 07xxxxxxxx/01xxxxxxxx
            mobileNumber.setError("Mobile phone number is required");
            mobileNumber.requestFocus();
            return false;
        } else {
            mobileNumber.setError(null);
        }

        // TODO: use regex to validate password
        if (Objects.requireNonNull(password.getEditText()).getText().toString().isEmpty() || password.getEditText().getText().toString().length() < 5) {
            password.setError("Password is required");
            password.requestFocus();
            return false;
        } else {
            password.setError(null);
        }

        if (Objects.requireNonNull(confirmPassword.getEditText()).getText().toString().isEmpty() || confirmPassword.getEditText().getText().toString().length() < 5) {
            confirmPassword.setError("Password confirmation is required");
            confirmPassword.requestFocus();
            return false;
        } else {
            confirmPassword.setError(null);
        }

        if (password.getEditText().getText().toString().equals(confirmPassword.getEditText().getText().toString())) {
            confirmPassword.setError(null);
        } else {
            password.getEditText().setText("");
            confirmPassword.getEditText().setText("");
            password.requestFocus();
            confirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }
}