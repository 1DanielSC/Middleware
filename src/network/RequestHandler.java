package network;

import java.net.Socket;

import network.interfaces.HTTPHandler;

public class RequestHandler implements HTTPHandler, Runnable{

    public Socket socket;

    public RequestHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){

    }

    public void send(){

    }
    public void receive(){

    }
}
