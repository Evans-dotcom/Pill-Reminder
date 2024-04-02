package com.evans.pillreminder.adapters;

import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.R;
import com.evans.pillreminder.db.Medication;
import com.evans.pillreminder.helpers.MedicationPill;

import java.util.ArrayList;
import java.util.List;

public class MedicationRecyclerAdapter extends RecyclerView.Adapter<MedicationRecyclerAdapter.MedicationViewHolder> {
    List<Medication> medications = new ArrayList<>();
    String firstMedicationTime, lastMedicationTime, firstMedicationHour, lastMedicationHour, previousHour;
    private boolean updateTime = false;

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_medication, parent, false);
        return new MedicationViewHolder(view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || !medications.get(position).getMedicationReminderTime().split(":")[0].equals(previousHour)) {
            Log.w(MY_TAG, position + "Diff: " + medications.get(position).getMedicationReminderTime() + " " + previousHour);
//            this.previousHour = medications.get(position).getMedicationReminderTime().split(":")[0];
//            firstMedicationHour = medications.get(position).getMedicationReminderTime().split(":")[0].concat(":00");
            updateTime = true;
            return 0;
        }
//        return super.getItemViewType(position);
        return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        Log.w(MY_TAG, position + " onBindViewHolder: " + medications.get(position).getMedicationReminderTime() + " " + previousHour);
        if (position == 0 || !medications.get(position).getMedicationReminderTime().split(":")[0].equals(previousHour)) {
            this.previousHour = medications.get(position).getMedicationReminderTime().split(":")[0];
            holder.tvTimeCardScheduledTime.setText(previousHour.concat(":00"));
        }

        holder.tvMedicationName.setText(medications.get(position).getMedicationName());
        holder.tvMedicationDosage.setText(medications.get(position).getMedicationDosage());

        String pillForm = medications.get(position).getMedicationForm();
        int image = 0;

        if (pillForm.equalsIgnoreCase("capsules")) {
            image = R.drawable.capsule_right_svg;
        } else if (pillForm.equalsIgnoreCase("tablets")) {
            image = R.drawable.tablets_svg;
        } else if (pillForm.equalsIgnoreCase("drops")) {
            image = R.drawable.drops_of_medicine_svg;
        } else if (pillForm.equalsIgnoreCase("inhalers")) {
            image = R.drawable.inhaler_asthma_svg;
        } else if (pillForm.equalsIgnoreCase("suppositories")) {
            image = R.drawable.suppository_capsule_svg;
        }
        holder.pillImage.setImageResource(image);
        Log.i(MY_TAG, "Database Data: " + medications);
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }

    public void setMedications(List<Medication> medications) {
        // sort the medication with hh:mm not yyyy-mm-dd hh:mm
        if (!medications.isEmpty()) {

            medications.forEach(medication -> {
                String[] time1 = medication.getMedicationReminderTime().split(":");
                Log.i(MY_TAG, "MedTime: " + time1[0].length() + " " + time1[1].length());
                if (time1[0].length() < 2) {
                    time1[0] = "0".concat(time1[0]);
                }

                if (time1[1].length() < 2) {
                    time1[1] = "0".concat(time1[0]);
                }
                String time = time1[0].concat(":").concat(time1[1]);
                medication.setMedicationReminderTime(time);
            });


            medications.sort((o1, o2) -> {
                String[] time1 = o1.getMedicationReminderTime().split(":");
                String[] time2 = o2.getMedicationReminderTime().split(":");

                int s0 = time1[0].compareTo(time2[0]);
                if (s0 == 0) {
                    String[] time3 = o1.getMedicationReminderTime().split(":");
                    String[] time4 = o2.getMedicationReminderTime().split(":");
                    return time3[1].compareTo(time4[1]);
                }
                return s0;
            });

            this.medications = medications;


            medications.forEach((medication) -> {
                Log.i(MY_TAG, "Medication Time: " + medication.getMedicationReminderTime());
            });

            firstMedicationTime = medications.get(0).getMedicationReminderTime();
            lastMedicationTime = medications.get(medications.size() - 1).getMedicationReminderTime();
            firstMedicationHour = firstMedicationTime.split(":")[0].concat(":00");  // get the first two characters
            lastMedicationHour = lastMedicationTime.split(":")[0].concat(":00");  // get the first two characters
        }

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
        TextView tvMedicationName, tvMedicationDosage, tvTimeCardScheduledTime;
        AppCompatImageView pillImage;
        Button btnNotTaken, btnTaken;

        //
        CardView scheduleTimeSwitch, cardMedication;
        RelativeLayout scheduleTimeViewLayout;

        public MedicationViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
//            cardMedication = itemView.findViewById(R.id.cardMedication);
            scheduleTimeViewLayout = itemView.findViewById(R.id.scheduleTimeViewLayout);
            tvTimeCardScheduledTime = itemView.findViewById(R.id.tvTimeCardScheduledTime);

            tvMedicationName = itemView.findViewById(R.id.medCardPillName);
            tvMedicationDosage = itemView.findViewById(R.id.medCardPillDosage);
            btnNotTaken = itemView.findViewById(R.id.btnCardMedicationMissed);
            btnTaken = itemView.findViewById(R.id.btnCardMedicationTaken);
            pillImage = itemView.findViewById(R.id.medCardPillImg);

            btnNotTaken.setOnClickListener(this);
            btnTaken.setOnClickListener(this);

            if (viewType == 0) {
//                View view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.card_scheduled_time, (ViewGroup) itemView, false);
//                scheduleTimeSwitch = view.findViewById(R.id.cardScheduledTime);
//                ((ViewGroup) itemView).addView(scheduleTimeSwitch);
                scheduleTimeViewLayout.setVisibility(View.VISIBLE);
                Log.w(MY_TAG, "MedicationViewHolder: View Type 0");
            } else if (viewType == 1) {
//                View view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.card_scheduled_time, (ViewGroup) itemView, false);
//                scheduleTimeSwitch = view.findViewById(R.id.cardScheduledTime);
//                ((ViewGroup) itemView).addView(scheduleTimeSwitch);
                scheduleTimeViewLayout.setVisibility(View.GONE);
                Log.w(MY_TAG, "MedicationViewHolder: View Type 1");
            }
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
