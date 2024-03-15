package com.evans.pillreminder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private SplashActivity context;
    private final long SPLASH_SCREEN_DELAY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent;

                if (user != null) {
                    // User is signed in
                    intent = new Intent(context, MainActivity.class);
                    context.finish();
                } else {
                    // No user is signed in
                    intent = new Intent(context, LoginActivity.class);
                    context.finish();
                }
                startActivity(intent);
            }
        }, SPLASH_SCREEN_DELAY);
    }
}
