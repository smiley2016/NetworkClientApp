package com.example.serverclient;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatList extends Fragment {

    private View rootView;
    private  ChatListAdapter adapter;
    private String currentUserName;

    @BindView(R.id.chat_list_recycler_view)
    RecyclerView chatListRecyclerView;

    @BindView(R.id.chat_room)
    Button chatRoom;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.chat_list_fragment, container, false);
            ButterKnife.bind(this, rootView);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        chatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MessengerFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentHolder, fragment);
                ft.commit();
            }
        });

        adapter = new ChatListAdapter(currentUserName,(MainActivity) getActivity());

        chatListRecyclerView.setHasFixedSize(true);
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        chatListRecyclerView.setAdapter(adapter);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader input = null;
                try {
                    OutputStream os = com.example.serverclient.Socket.getSocket().getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    BufferedWriter bw = new BufferedWriter(osw);

                    String number = "5,";

                    bw.write(number);
                    bw.flush();

                    input = new BufferedReader(new InputStreamReader(Socket.getSocket().getInputStream()));
                    String st = input.readLine();
                    addToListOnmainThread(st);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();
    }

    public void addToListOnmainThread(final String st){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.addToList(st);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(getArguments() != null){
            currentUserName = getArguments().getString("NAME");
        }
    }
}
