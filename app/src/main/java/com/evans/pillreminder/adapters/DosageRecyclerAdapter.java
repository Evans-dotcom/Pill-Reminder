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
import com.evans.pillreminder.helpers.Medication;
import com.evans.pillreminder.helpers.MedicineDosage;

import java.util.ArrayList;

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

    static class MedicationDiff extends DiffUtil.ItemCallback<Medication> {
        /**
         * Called to check whether two objects represent the same item.
         * <p>
         * For example, if your items have unique ids, this method should check their id equality.
         * <p>
         * Note: {@code null} items in the list are assumed to be the same as another {@code null}
         * item and are assumed to not be the same as a non-{@code null} item. This callback will
         * not be invoked for either of those cases.
         *
         * @param oldItem The item in the old list.
         * @param newItem The item in the new list.
         * @return True if the two items represent the same object or false if they are different.
         * @see Callback#areItemsTheSame(int, int)
         */
        @Override
        public boolean areItemsTheSame(@NonNull Medication oldItem, @NonNull Medication newItem) {
            return oldItem == newItem;
        }

        /**
         * Called to check whether two items have the same data.
         * <p>
         * This information is used to detect if the contents of an item have changed.
         * <p>
         * This method to check equality instead of {@link Object#equals(Object)} so that you can
         * change its behavior depending on your UI.
         * <p>
         * For example, if you are using DiffUtil with a
         * {@link RecyclerView.Adapter RecyclerView.Adapter}, you should
         * return whether the items' visual representations are the same.
         * <p>
         * This method is called only if {@link #areItemsTheSame(T, T)} returns {@code true} for
         * these items.
         * <p>
         * Note: Two {@code null} items are assumed to represent the same contents. This callback
         * will not be invoked for this case.
         *
         * @param oldItem The item in the old list.
         * @param newItem The item in the new list.
         * @return True if the contents of the items are the same or false if they are different.
         * @see Callback#areContentsTheSame(int, int)
         */
        @Override
        public boolean areContentsTheSame(@NonNull Medication oldItem, @NonNull Medication newItem) {
            // put an id that will be used to check the difference
            return false;
        }
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
