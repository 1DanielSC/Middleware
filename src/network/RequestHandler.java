package network;

import java.net.Socket;

import network.interfaces.HTTPHandler;
import server.Invoker;

public class RequestHandler implements HTTPHandler, Runnable{

    public Socket socket;
    
    public Invoker invoker;

    public RequestHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
    	
    	
    	//receber dados
    	//desserializar
    	//enviar dados
    }

    public void send(){
    	
    }
    public void receive(){

    }
}
