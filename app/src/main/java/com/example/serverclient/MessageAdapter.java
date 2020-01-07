package com.example.serverclient;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private ArrayList<Message> message;

    MessageAdapter() {
        this.message = new ArrayList<>();
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
       return new MessageViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.message_element, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        Message mess = message.get(position);
        holder.message_text_view.setText(mess.getMessage());
        if(mess.isFromMe()){
            holder.message_text_view.setBackgroundColor(Color.GREEN);
        }else{
            holder.message_text_view.setBackgroundColor(Color.BLUE);
        }
    }

    @Override
    public int getItemCount() {
        return message.size();
    }

    void addToList(Message mess){
        message.add(mess);
        notifyDataSetChanged();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.message_text_view)
        TextView message_text_view;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
