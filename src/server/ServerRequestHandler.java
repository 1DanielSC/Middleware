package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import network.RequestHandler;

public class ServerRequestHandler {
    
    public ServerSocket socket;

    public Invoker invoker;

    public ServerRequestHandler(int port){
        this.connect(port);

        try {
            while (true) {
                Socket clientSocket = this.socket.accept();
                new Thread(new RequestHandler(clientSocket, invoker)).start();
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

}
