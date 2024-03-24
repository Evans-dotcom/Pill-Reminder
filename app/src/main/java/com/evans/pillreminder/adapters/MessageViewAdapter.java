package com.evans.pillreminder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.R;
import com.evans.pillreminder.fragments.ChatFragment;
import com.evans.pillreminder.helpers.MessageView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewAdapter.MessageViewViewHolder> {
    List<MessageView> messages = new ArrayList<>();
    FragmentActivity context;

    public MessageViewAdapter(FragmentActivity context) {
        this.context = context;
    }

    {
        messages.add(new MessageView("Brandon Mwangi", "13:54", "Description 1"));
        messages.add(new MessageView("Brandon Maina", "13:54", "Description 2"));
        messages.add(new MessageView("Jane Mwangi", "13:54", "Description 3"));
        messages.add(new MessageView("Joe Mwangi", "13:54", "Description 4"));
        messages.add(new MessageView("Jack Mwangi", "13:54", "Description"));
        messages.add(new MessageView("Sammy Mwangi", "13:54", "Description"));
        messages.add(new MessageView("Jake Mwangi", "13:54", "Description"));
        messages.add(new MessageView("Brenda Mwangi", "13:54", "Description"));
        messages.add(new MessageView("Mary Mwangi", "13:54", "Description"));
        messages.add(new MessageView("Leah Mwangi", "13:54", "Description"));
        messages.add(new MessageView("James Mwangi", "13:54", "Description"));
        messages.add(new MessageView("Ruth Mwangi", "13:54", "Description"));
        messages.add(new MessageView("Lyn Mwangi", "13:54", "Description"));
        messages.add(new MessageView("Jade Mwangi", "13:54", "Description"));
    }

    @NonNull
    @Override
    public MessageViewAdapter.MessageViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view, parent, false);
        return new MessageViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewAdapter.MessageViewViewHolder holder, int position) {
        holder.senderName.setText(messages.get(position).getSenderName());
        holder.lastMessageView.setText(messages.get(position).getLastMessage());
        holder.lastMessageTime.setText(messages.get(position).getLastMessageTime());
        holder.messageViewView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new fragment with chats displayed
                FragmentManager fragmentManager = context.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.messages_fragment, new ChatFragment());
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView messageViewView;
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
