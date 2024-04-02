package com.evans.pillreminder.adapters;

import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.R;
import com.evans.pillreminder.helpers.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    List<ChatMessage> chatMessages;
    String userID;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        userID = FirebaseAuth.getInstance().getUid();
        this.chatMessages = new ArrayList<>();
        this.chatMessages = chatMessages;
    }

    public ChatAdapter() {
        userID = FirebaseAuth.getInstance().getUid();
        this.chatMessages = new ArrayList<>();
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        Log.w(MY_TAG, "setChatMessages: " + chatMessages.get(0).getRecipientID() + " <> " +
                chatMessages.get(0).getMessage());

        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_recycler_row, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        Log.d(MY_TAG, userID + " =>EachMessage: " + chatMessages.get(position).getRecipientID());

        if (!chatMessages.get(position).getRecipientID().equals(this.userID)) {
            holder.sentChatLayout.setVisibility(View.VISIBLE);
            holder.sentChat.setText(chatMessages.get(position).getMessage());
            holder.receivedChatLayout.setVisibility(View.GONE);
        } else {
            holder.receivedChatLayout.setVisibility(View.VISIBLE);
            holder.receiveChat.setText(chatMessages.get(position).getMessage());
            holder.sentChatLayout.setVisibility(View.GONE);
        }

//        holder.chatTime.setText(String.valueOf(chatMessages.get(position).getMessageTime()));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView receiveChat, sentChat, chatTime;
        LinearLayout receivedChatLayout, sentChatLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            receiveChat = itemView.findViewById(R.id.tvReceivedText);
            sentChat = itemView.findViewById(R.id.tvSentText);
//            chatTime = itemView.findViewById(R.id.chatTime);

            receivedChatLayout = itemView.findViewById(R.id.receivedChatLayout);
            sentChatLayout = itemView.findViewById(R.id.sentChatLayout);
        }
    }
}
