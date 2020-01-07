package com.example.serverclient;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MessengerFragment extends Fragment {

    private View rootView;
    private MessageAdapter messageAdapter;
    private String cname;
    private String dname;

    @BindView(R.id.message_recycler_view)
    RecyclerView messageRecyclerView;

    @BindView(R.id.message_edit_text)
    EditText messageEditText;

    @BindView(R.id.message_send_image_view)
    ImageView sendImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.messenger_fragment, container, false);
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
        messageAdapter = new MessageAdapter();
        messageRecyclerView.setHasFixedSize(true);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        messageRecyclerView.setAdapter(messageAdapter);

        Thread listenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader input = null;
                try {
                    input = new BufferedReader(new InputStreamReader(Socket.getSocket().getInputStream()));
                    String st = null;
                    while ((st = input.readLine()) != null) {
                        Log.e("Message", "run: " + st);
                        if(st.equals("The Message was sent!")){
                            continue;
                        }
                        addToListOnMainThread(st);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        listenThread.start();

        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageEditText.length() > 0) {
                    messageAdapter.addToList(new Message(messageEditText.getText().toString(), true));
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            BufferedReader input = null;
                            try {
                                OutputStream os = com.example.serverclient.Socket.getSocket().getOutputStream();
                                OutputStreamWriter osw = new OutputStreamWriter(os);
                                BufferedWriter bw = new BufferedWriter(osw);

                                String number = "1,Moni," + messageEditText.getText().toString();

                                bw.write(number);
                                bw.flush();
//                                input = new BufferedReader(new InputStreamReader(Socket.getSocket().getInputStream()));
//                                String st = input.readLine();
//                                Log.d("Message Feedback", "Message Sent " + st);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
            }
        });


    }

    public void addToListOnMainThread(final String st) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter.addToList(new Message(st, false));
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(getArguments() != null){
            cname = getArguments().getString("CNAME");
            dname = getArguments().getString("DNAME");
        }
    }
}
