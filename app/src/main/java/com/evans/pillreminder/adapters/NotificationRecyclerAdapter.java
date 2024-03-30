package com.evans.pillreminder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.R;
import com.evans.pillreminder.helpers.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.NotificationViewViewHolder> {
    List<Notification> notifications = new ArrayList<>();

    @NonNull
    @Override
    public NotificationRecyclerAdapter.NotificationViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view, parent, false);
        return new NotificationViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationRecyclerAdapter.NotificationViewViewHolder holder, int position) {
        holder.notificationTitle.setText(notifications.get(position).getTitle());
        holder.notificationMessage.setText(notifications.get(position).getMessage());
        holder.notificationTitle.setText(notifications.get(position).getTitle());
        holder.notificationSentTime.setText(notifications.get(position).getSentTimestamp());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout notificationView;
        TextView notificationTitle, notificationMessage, notificationSentTime;

        public NotificationViewViewHolder(@NonNull View itemView) {
            super(itemView);

            notificationView = itemView.findViewById(R.id.notificationView);
            notificationTitle = itemView.findViewById(R.id.notificationTitle);
            notificationMessage = itemView.findViewById(R.id.notificationMessage);
            notificationSentTime = itemView.findViewById(R.id.notificationSentTime);
        }
    }
}
