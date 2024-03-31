package com.evans.pillreminder;

import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_COLLECTIONS_USERS;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.adapters.RecipientSearchAdapter;
import com.evans.pillreminder.helpers.DoctorResultView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectRecipientActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    FirebaseFirestore firestore;
    SearchView searchChatRecipient;
    RecyclerView recipientsRecyclerView;
    private RecipientSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_recipient);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        firestore = FirebaseFirestore.getInstance();

        adapter = new RecipientSearchAdapter(this);
        recipientsRecyclerView = findViewById(R.id.recipientsRecyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(this);

        recipientsRecyclerView.setLayoutManager(manager);
        recipientsRecyclerView.setAdapter(adapter);

        // TODO: do this in background
        firestore.collection(DB_FIRESTORE_COLLECTIONS_USERS).get().addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> searchDocuments = task.getResult().getDocuments();
                Log.d(MY_TAG, "SuccessfulQuery: " + searchDocuments);

                // using an adapter display the users
                List<DoctorResultView> doctorResultViews = new ArrayList<>();
                if (searchDocuments != null) {
                    searchDocuments.forEach((snapshot) -> {
                        Log.d(MY_TAG, "Snap*****: " + snapshot + "*****");
                        doctorResultViews.add(
                                new DoctorResultView(Objects.requireNonNull(snapshot.get("firstName")).toString(),
                                        Objects.requireNonNull(snapshot.get("lastName")).toString(),
                                        Objects.requireNonNull(snapshot.get("uid")).toString(),
                                        Objects.requireNonNull(snapshot.get("fcmToken")).toString()));
                    });
                    adapter.setDoctors(doctorResultViews);
                    adapter.notifyDataSetChanged();
                }
            } else {
                Log.e(MY_TAG, "QueryError: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });

        searchChatRecipient = findViewById(R.id.searchChatRecipient);

        searchChatRecipient.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(MY_TAG, "SearchText: " + newText);
        return false;
    }
}