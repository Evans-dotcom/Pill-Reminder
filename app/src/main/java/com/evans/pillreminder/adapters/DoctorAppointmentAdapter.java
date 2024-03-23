package com.evans.pillreminder.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.R;
import com.evans.pillreminder.helpers.Appointment;

import java.util.ArrayList;
import java.util.List;

public class DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentAdapter.AppointmentViewHolder> {
    List<Appointment> appointments = new ArrayList<>();

    {
        appointments.add(new Appointment("Evans", "Langat", "Some mall description",
                "2023-12-20", "13:45:20"));

        appointments.add(new Appointment("Brian", "Langat", "Some mall description",
                "2023-12-20", "13:45:20"));

        appointments.add(new Appointment("Jane", "Langat", "Some mall description",
                "2023-12-20", "13:45:20"));

        appointments.add(new Appointment("Joe", "Langat", "Some mall description",
                "2023-12-20", "13:45:20"));

        appointments.add(new Appointment("Chris", "Langat", "Some mall description",
                "2023-12-20", "13:45:20"));
    }

    @NonNull
    @Override
    public DoctorAppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_schedule_home, parent, false);
        return new AppointmentViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DoctorAppointmentAdapter.AppointmentViewHolder holder, int position) {
        holder.tvName.setText(appointments.get(position).getFirstName() + " " + appointments.get(position).getLastName());
        holder.tvDate.setText(appointments.get(position).getAppointmentDate());
        holder.tvTime.setText(appointments.get(position).getAppointmentTime());
        holder.tvDescription.setText(appointments.get(position).getDescription());
        holder.appointmentCard.setOnClickListener(v -> Toast.makeText(v.getContext(), "Clicked: " + position, Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvTime, tvDescription;
        CardView appointmentCard;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.scheduleAppointeeName);
            tvDate = itemView.findViewById(R.id.scheduleAppointmentDate);
            tvTime = itemView.findViewById(R.id.scheduleAppointmentTime);
            tvDescription = itemView.findViewById(R.id.scheduleAppointmentDescription);

            appointmentCard = itemView.findViewById(R.id.appointmentCard);
        }
    }
}
