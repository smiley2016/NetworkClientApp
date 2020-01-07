package com.example.serverclient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    MyClient client;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                client = new MyClient(MainActivity.this);
                client.doInBackground();
            }
        });

        thread.start();
        Fragment fragment = new NameFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentHolder, fragment);
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            com.example.serverclient.Socket.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class MyClient extends AsyncTask<Void, Void, Void> {

        private Context context;
        private String st;
        private Thread thread;

        MyClient(Context context) {
            this.context = context;
        }

        public Thread getThread() {
            return thread;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedReader input = null;
                    try {
                        //Replace below IP with the IP of that device in which server socket open.
                        //If you change port then change the port number in the server side code also.
                        com.example.serverclient.Socket.setSocket(new Socket("192.168.137.1", 165));

                        Log.e(TAG, "run: Client connected");


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, st, Toast.LENGTH_SHORT).show();
        }
    }
}


