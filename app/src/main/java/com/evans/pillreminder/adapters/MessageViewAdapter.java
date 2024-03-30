package com.evans.pillreminder.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.ChatActivity;
import com.evans.pillreminder.R;
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

//    {
//        messages.add(new MessageView("George N", "13:54", "Description 1", "1FWHdkeDLPezEzhaFQSMiuLDzGW2", "ejnhwXolTBaiFpy-pujwr7:APA91bHabyDTduhl8T_wJA2Jy6m5PHaRB3f7-jWK-6t2vn6e5VGlDiy0ol3sHxJSnfT8fmb4zYL5ldxGVq1xBhxwSZHrDeSzV4nVGd1GZhAxeEnPlWY0WCk_rVUYPw3nDtK0z6cSUNmo"));
//        messages.add(new MessageView("Evans L", "13:56", "Description", "PW6t2plxsKXzJGio5BgR4JDO0wi1", "dkaWLKyiRwaiD4bc1RB11d:APA91bHGUXOWDBLjcxGRjTy348zUhfMx7xXAfffBHq24e3SBC__7kVT1QssbZFGh9cJlAc7MUxkDWi4v32KE1oEqjVA3tQfkkQ3kIe4QCbM-D8gkfMnuk14c6gkOgc4voUVzEbXPr6KR"));
//    }

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
        holder.lastMessageTime.setText(String.valueOf(messages.get(position).getLastMessageTime()));
        holder.messageViewView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new fragment with chats displayed
//                FragmentManager fragmentManager = context.getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.messages_fragment, new ChatFragment()).addToBackStack(null);
//                fragmentTransaction.commit();
                // TODO: pass the user details to the next activity
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("recipientID", messages.get(position).getRecipientID());
                intent.putExtra("recipientToken", messages.get(position).getRecipientToken());
                context.startActivity(intent);
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
