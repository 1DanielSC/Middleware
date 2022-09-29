package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import network.RequestHandler;
import server.interfaces.Broker;

public class ServerRequestHandler implements Broker{
    public ServerSocket socket;

    public ServerRequestHandler(int port){
        this.connect(port);

        try {
            while (true) {
                Socket clientSocket = this.socket.accept();
                new Thread(new RequestHandler(clientSocket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(int port){
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forwardRequest(){

    }
    public void forwardResponse(){

    }
    public void registerService(){

    }

    public static void main(String[] args) {
        new ServerRequestHandler(Integer.parseInt(args[0]));
    }
}