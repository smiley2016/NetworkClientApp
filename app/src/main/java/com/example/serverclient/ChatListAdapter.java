package com.example.serverclient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private ArrayList<String> chatList;
    private Context context;
    private String currentUsername;
    private MainActivity activity;

    public ChatListAdapter(String userName, MainActivity activity){
        chatList = new ArrayList<>();
        currentUsername = userName;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ChatListAdapter.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.chat_list_element, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListAdapter.ChatListViewHolder holder, int position) {
        final String name = chatList.get(position);
        holder.userName.setText(name);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("CNAME", currentUsername);
                bundle.putString("DNAME", String.valueOf(holder.userName));
                Fragment fragment = new MessengerFragment();
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentHolder, fragment);
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void addToList(String st) {
        chatList.add(st);
        notifyDataSetChanged();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.chat_list_element)
        ConstraintLayout layout;

        @BindView(R.id.profilePicture)
        ImageView profilePicture;

        @BindView(R.id.userName)
        TextView userName;

        @BindView(R.id.user_checkBox)
        CheckBox checkBox;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
