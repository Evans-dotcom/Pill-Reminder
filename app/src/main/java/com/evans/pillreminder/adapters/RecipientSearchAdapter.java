package com.evans.pillreminder.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.ChatActivity;
import com.evans.pillreminder.R;
import com.evans.pillreminder.helpers.DoctorResultView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipientSearchAdapter extends RecyclerView.Adapter<RecipientSearchAdapter.SearchViewHolder> {
    private final String userID;
    List<DoctorResultView> doctors = new ArrayList<>();
    Context context;

    public RecipientSearchAdapter(Context context) {
        this.context = context;
        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    public void setDoctors(List<DoctorResultView> doctors) {
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public RecipientSearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view, parent, false);
        return new RecipientSearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipientSearchAdapter.SearchViewHolder holder, int position) {
        String doctorName = doctors.get(position).getFirstName() + " " + doctors.get(position).getLastName();

        if (userID.equals(doctors.get(position).getDoctorUID())) {
            // FIXME: this is a hack to show the user that they are the sender
            doctorName += "(You)";
            holder.messageViewView.setVisibility(View.GONE);
        }

        holder.doctorImage.setImageResource(doctors.get(position).getDoctorImage());
        holder.doctorName.setText(doctorName);

        holder.messageViewView.setOnClickListener(v -> {
            String recipientName = doctors.get(position).getFirstName() + " " + doctors.get(position).getLastName();
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("recipientID", doctors.get(position).getDoctorUID());
            intent.putExtra("recipientToken", doctors.get(position).getDoctorToken());
            intent.putExtra("recipientName", recipientName);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName, lastMessage, lastMessageTime;
        ImageView doctorImage;
        CardView messageViewView;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            doctorName = itemView.findViewById(R.id.senderName);
            doctorImage = itemView.findViewById(R.id.senderImage);
            messageViewView = itemView.findViewById(R.id.messageViewView);

            lastMessage = itemView.findViewById(R.id.lastMessageView);
            lastMessageTime = itemView.findViewById(R.id.lastMessageTime);

            lastMessage.setVisibility(View.GONE);
            lastMessageTime.setVisibility(View.GONE);
        }
    }
}
