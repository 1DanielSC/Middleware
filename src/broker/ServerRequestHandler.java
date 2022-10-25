package broker;

import java.io.IOException;
import java.net.ServerSocket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import broker.interfaces.IMarshaller;
import broker.interfaces.IServerRequestHandler;
import extension.ExtensionService;
import network.RequestHandler;

public class ServerRequestHandler implements IServerRequestHandler{
    
    public ServerSocket socket;

    public ExecutorService threadExecutor;

    public ServerRequestHandler(int port, Invoker invoker, IMarshaller marshaller){
        this.connect(port);

        this.threadExecutor = Executors.newFixedThreadPool(30);
        
        ExtensionService extensionService = new ExtensionService();

        try {
            while (true) {
                threadExecutor.execute(new RequestHandler(this.socket.accept(), invoker, marshaller, extensionService));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(int port){
        try {
            System.out.println("TCP - Starting Middleware on port " + port);
            this.socket = new ServerSocket(port);
            System.out.println("TCP - Succesfully started Middleware on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
