package com.evans.pillreminder;

import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.evans.pillreminder.db.User;
import com.evans.pillreminder.db.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private final long SPLASH_SCREEN_DELAY = 500;
    private SplashActivity context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        context = this;

        //
        Intent mIntent = new Intent();
        Intent fromIntent = getIntent();
        if (fromIntent.getExtras() != null) {
            try {
                startActivity(new Intent(this, NotificationsActivity.class));
                finish();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        new Handler().postDelayed(() -> {
            Intent intent;

            UserViewModel userViewModel = new UserViewModel(context.getApplication());
            User localUser = userViewModel.getUser().getValue();
            Log.i(MY_TAG, "Splash: " + localUser + " F: ");

            if (user != null) {
                // User is signed in
                Log.i(MY_TAG, "SplashOnCreate: 1");
                intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                context.finish();
            } else {
                // No user is signed in
                Log.i(MY_TAG, "SplashOnCreate: 2");
                // if the user used google sign in api
//                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                            .requestIdToken(getString(R.string.default_web_client_id))
//                            .requestEmail()
//                            .build();
//                    gso.getAccount();
//                    Log.i(MY_TAG, "GSO: " + gso + " " + Objects.requireNonNull(gso.getAccount()));
                intent = new Intent(context, LoginActivity.class);
                context.finish();
            }
            startActivity(intent);
        }, SPLASH_SCREEN_DELAY);
    }
}
