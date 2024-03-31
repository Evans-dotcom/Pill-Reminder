package com.evans.pillreminder.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.R;
import com.evans.pillreminder.SelectRecipientActivity;
import com.evans.pillreminder.adapters.MessageViewAdapter;
import com.evans.pillreminder.db.Message;
import com.evans.pillreminder.db.MessageViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MessagesFragment extends Fragment implements View.OnClickListener {
    RecyclerView messagesViewRecyclerView;
    FloatingActionButton fabCreateNewMessage;
    MessageViewModel messageViewModel;
    private MessageViewAdapter adapter;
    private List<Message> groupedMessagesList;

    public MessagesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageViewModel = new MessageViewModel(requireActivity().getApplication());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messagesViewRecyclerView = view.findViewById(R.id.messagesViewRecyclerView);
        fabCreateNewMessage = view.findViewById(R.id.messagesStartNew);

        messagesViewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesViewRecyclerView.setHasFixedSize(true);

        adapter = new MessageViewAdapter(this.getActivity());

        new Thread(new Runnable() {
            @Override
            public void run() {
                groupedMessagesList = messageViewModel.getGroupedMessagesList();
                adapter.setMessages(Message.getMessageViewFormat(groupedMessagesList));
                requireActivity().runOnUiThread(() -> {//
                    adapter.notifyDataSetChanged();
                });
            }
        }).start();

        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        messagesViewRecyclerView.setLayoutManager(manager);
        messagesViewRecyclerView.setAdapter(adapter);

        fabCreateNewMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.messagesStartNew) {
            //
            Intent intent = new Intent(getContext(), SelectRecipientActivity.class);
            startActivity(intent);
        }
    }
}
