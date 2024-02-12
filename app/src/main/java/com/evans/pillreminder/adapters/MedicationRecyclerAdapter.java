package com.evans.pillreminder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.R;
import com.evans.pillreminder.db.Medication;
import com.evans.pillreminder.helpers.MedicationPill;

import java.util.ArrayList;
import java.util.List;

public class MedicationRecyclerAdapter extends RecyclerView.Adapter<MedicationRecyclerAdapter.MedicationViewHolder> {
    List<Medication> medications = new ArrayList<>();

//    {
//        String[] pillNames = {"Piliton", "Amoxil", "Panadol", "ARV"};
//        int[] pillDosages = {1, 5, 3, 1};
//
//        for (int i = 0; i < pillNames.length; i++) {
//            dosage.add(new MedicineDosage(pillNames[i], pillDosages[i]));
//        }
//    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_medication, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        holder.tvMedicationName.setText(medications.get(position).getMedicationName());
        holder.tvMedicationDosage.setText(medications.get(position).getMedicationDosage());
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
        notifyDataSetChanged();
    }

    static class MedicationDiff extends DiffUtil.ItemCallback<MedicationPill> {

        @Override
        public boolean areItemsTheSame(@NonNull MedicationPill oldItem, @NonNull MedicationPill newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MedicationPill oldItem, @NonNull MedicationPill newItem) {
            // put an id that will be used to check the difference
            return false;
        }
    }

    protected static class MedicationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMedicationName, tvMedicationDosage;
        Button btnNotTaken, btnTaken;

        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedicationName = itemView.findViewById(R.id.medCardPillName);
            tvMedicationDosage = itemView.findViewById(R.id.medCardPillDosage);
            btnNotTaken = itemView.findViewById(R.id.btnCardMedicationMissed);
            btnTaken = itemView.findViewById(R.id.btnCardMedicationTaken);

            btnNotTaken.setOnClickListener(this);
            btnTaken.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnCardMedicationMissed) {
                Toast.makeText(v.getContext(), "Not Taken", Toast.LENGTH_SHORT).show();
            } else if (v.getId() == R.id.btnCardMedicationTaken) {
                Toast.makeText(v.getContext(), "Taken", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
