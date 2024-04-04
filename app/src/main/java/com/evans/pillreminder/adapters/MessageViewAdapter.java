package com.evans.pillreminder.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.ChatActivity;
import com.evans.pillreminder.R;
import com.evans.pillreminder.helpers.MessageView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewAdapter.MessageViewViewHolder> {
    private final String userID;
    List<MessageView> messages = new ArrayList<>();
    FragmentActivity context;

    public MessageViewAdapter(FragmentActivity context) {
        this.context = context;
        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    public void setMessages(List<MessageView> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewAdapter.MessageViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view, parent, false);
        return new MessageViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewAdapter.MessageViewViewHolder holder, int position) {
        String userFullName = messages.get(position).getSenderName() +
                ((messages.get(position).getRecipientID().equals(userID)) ? "(You)" : "");

        if (messages.get(position).getRecipientID().equals(userID)) {
            holder.messageViewView.setVisibility(View.GONE);
        }

        holder.senderName.setText(userFullName);
        holder.lastMessageView.setText(messages.get(position).getLastMessage());
        // create time from epoch time HH:mm (DD/MM/YYYY)
        Date date = new Date(messages.get(position).getLastMessageTime());
        String dateTime = date.toString();
        String time = dateTime.substring(11, 16);
        String dateStr = dateTime.substring(0, 11);

        holder.lastMessageTime.setText(time + " " + dateStr);

        holder.messageViewView.setOnClickListener(v -> {
            // new fragment with chats displayed
//                FragmentManager fragmentManager = context.getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.messages_fragment, new ChatFragment()).addToBackStack(null);
//                fragmentTransaction.commit();
            // TODO: pass the user details to the next activity
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("recipientID", messages.get(position).getRecipientID());
            intent.putExtra("recipientToken", messages.get(position).getRecipientToken());
            intent.putExtra("recipientName", messages.get(position).getSenderName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewViewHolder extends RecyclerView.ViewHolder {
        CardView messageViewView;
        CircleImageView senderImage;
        TextView senderName, lastMessageView, lastMessageTime;

        public MessageViewViewHolder(@NonNull View itemView) {
            super(itemView);

            messageViewView = itemView.findViewById(R.id.messageViewView);
            senderImage = itemView.findViewById(R.id.senderImage);
            senderName = itemView.findViewById(R.id.senderName);
            lastMessageView = itemView.findViewById(R.id.lastMessageView);
            lastMessageTime = itemView.findViewById(R.id.lastMessageTime);
        }
    }
}
