package com.example.serverclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NameFragment extends Fragment {

    public View rootView;

    @BindView(R.id.name_edit_text)
    EditText nameEditText;

    @BindView(R.id.button)
    Button sendName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView== null){
            rootView = inflater.inflate(R.layout.name_fragment, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addName(nameEditText.getText().toString());

//                Fragment fragment = new MessengerFragment();
//                fragment.setArguments(bundle);
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.fragmentHolder, fragment);
//                ft.commit();

                Bundle bundle = new Bundle();
                bundle.putString("NAME", nameEditText.getText().toString());
                Fragment fragment = new ChatList();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentHolder, fragment);
                ft.commit();
            }
        });
    }

    private void addName(final String name){
                Thread nameThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BufferedReader input;
                        try {
                            OutputStream os = com.example.serverclient.Socket.getSocket().getOutputStream();
                            OutputStreamWriter osw = new OutputStreamWriter(os);
                            BufferedWriter bw = new BufferedWriter(osw);

                            String number = "0," + name;

                            bw.write(number);
                            bw.flush();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                nameThread.start();
    }

}
