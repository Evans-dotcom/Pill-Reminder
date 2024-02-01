package com.evans.pillreminder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.R;
import com.evans.pillreminder.helpers.MedicineDosage;

import java.util.ArrayList;
import java.util.Objects;

public class DosageRecyclerAdapter extends RecyclerView.Adapter<DosageRecyclerAdapter.DosageViewHolder> {
    ArrayList<MedicineDosage> dosage = new ArrayList<>();

    {
        String[] pillNames = {"Piliton", "Amoxil", "Panadol", "ARV"};
        int[] pillDosages = {1, 5, 3, 1};

        for (int i = 0; i < pillNames.length; i++) {
            dosage.add(new MedicineDosage(pillNames[i], pillDosages[i]));
        }
    }

    @NonNull
    @Override
    public DosageRecyclerAdapter.DosageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_medication, parent, false);
        return new DosageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DosageRecyclerAdapter.DosageViewHolder holder, int position) {
        holder.tvPillName.setText(dosage.get(position).getPillName());
        holder.tvPillDosage.setText(String.valueOf(dosage.get(position).getPillDosage()));
    }

    @Override
    public int getItemCount() {
        return dosage.size();
    }

    protected class DosageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvPillName, tvPillDosage;
        Button btnNotTaken, btnTaken;

        public DosageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPillName = itemView.findViewById(R.id.medCardPillName);
            tvPillDosage = itemView.findViewById(R.id.medCardPillDosage);
            btnNotTaken = itemView.findViewById(R.id.btnCardMedicationMissed);
            btnTaken = itemView.findViewById(R.id.btnCardMedicationTaken);

            btnNotTaken.setOnClickListener(this);
            btnTaken.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
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
