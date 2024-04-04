package com.evans.pillreminder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.R;
import com.evans.pillreminder.db.Medication;

import java.util.ArrayList;
import java.util.List;

public class MedicationHistoryAdapter extends RecyclerView.Adapter<MedicationHistoryAdapter.MedicationHistoryViewHolder> {
    List<Medication> medications = new ArrayList<>();

    @NonNull
    @Override
    public MedicationHistoryAdapter.MedicationHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_view_item, parent, false);
        return new MedicationHistoryAdapter.MedicationHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationHistoryAdapter.MedicationHistoryViewHolder holder, int position) {
        holder.issuedDate.setText(medications.get(position).getMedicationDate());
        holder.resolvedDate.setText(medications.get(position).getUpdatedAt());
        holder.medicationName.setText(medications.get(position).getMedicationName());
        holder.resolvedStatus.setText(medications.get(position).isTodayTaken() ? "Taken" : "Not Taken");
        holder.historyCard.setOnClickListener(v -> Toast.makeText(v.getContext(), "Medication: " +
                medications.get(position).getMedicationName() + "\nWill sometimes later open a new page", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }

    public class MedicationHistoryViewHolder extends RecyclerView.ViewHolder {
        CardView historyCard;
        TextView medicationName, issuedDate, resolvedDate, resolvedStatus;

        public MedicationHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyCard = itemView.findViewById(R.id.historyCard);
            medicationName = itemView.findViewById(R.id.tvHistoryMedicationName);
            resolvedDate = itemView.findViewById(R.id.tvHistoryDateChecked);
            resolvedStatus = itemView.findViewById(R.id.medicationTakenStatus);
            issuedDate = itemView.findViewById(R.id.tvHistoryIssueDate);
        }
    }
}
