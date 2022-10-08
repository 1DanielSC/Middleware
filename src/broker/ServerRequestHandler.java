package broker;

import java.io.IOException;
import java.net.ServerSocket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import broker.interfaces.IMarshaller;
import network.RequestHandler;

public class ServerRequestHandler {
    
    public ServerSocket socket;

    public ExecutorService threadExecutor;

    public ServerRequestHandler(int port, Invoker invoker, IMarshaller marshaller){
        this.connect(port);

        this.threadExecutor = Executors.newFixedThreadPool(30);

        try {
            while (true) {
                threadExecutor.execute(new RequestHandler(this.socket.accept(), invoker, marshaller));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(int port){
        try {
            System.out.println("Starting Middleware on port " + port);
            this.socket = new ServerSocket(port);
            System.out.println("Succesfully started Middleware on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
