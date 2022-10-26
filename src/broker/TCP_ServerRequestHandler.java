package broker;

import java.io.IOException;
import java.net.ServerSocket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import broker.interfaces.IMarshaller;
import broker.interfaces.IServerRequestHandler;
import broker.invoker.Invoker;
import extension.ExtensionService;
import network.TCP_RequestHandler;

public class TCP_ServerRequestHandler implements IServerRequestHandler{
    
    public ServerSocket socket;

    public ExecutorService threadExecutor;

    public TCP_ServerRequestHandler(int port, Invoker invoker, IMarshaller marshaller, ExtensionService extensionService){
        this.connect(port);

        this.threadExecutor = Executors.newFixedThreadPool(30);

        try {
            while (true) {
                threadExecutor.execute(new TCP_RequestHandler(this.socket.accept(), invoker, marshaller, extensionService));
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
