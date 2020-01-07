package com.example.serverclient;

public class Socket {

    private static java.net.Socket s = null;

    static void setSocket(java.net.Socket socket){
        s = socket;
    }

    public static java.net.Socket getSocket(){
        return s;
    }
}
