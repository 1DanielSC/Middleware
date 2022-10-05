package broker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import broker.interfaces.IMarshaller;
import network.RequestHandler;

public class ServerRequestHandler {
    
    public ServerSocket socket;


    public ServerRequestHandler(int port, Invoker invoker, IMarshaller marshaller){
        this.connect(port);

        try {
            while (true) {
                Socket clientSocket = this.socket.accept();
                new Thread(new RequestHandler(clientSocket, invoker, marshaller)).start();
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