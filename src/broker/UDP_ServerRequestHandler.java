package broker;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import broker.interfaces.IMarshaller;
import broker.interfaces.IServerRequestHandler;
import broker.invoker.Invoker;
import extension.ExtensionService;
import network.UDP_RequestHandler;


public class UDP_ServerRequestHandler implements IServerRequestHandler{
	
	public DatagramSocket socket;
	
	public ExecutorService threadExecutor;
	
	public IMarshaller marshaller;
	
	public Invoker invoker;
	
	public UDP_ServerRequestHandler(int port, Invoker invoker, IMarshaller marshaller, ExtensionService extensionService) {
		
		this.connect(port);
		
		this.threadExecutor = Executors.newFixedThreadPool(30);
		this.invoker = invoker;
		this.marshaller = marshaller;
		
		try {
			
			while(true) {
				byte[] packet = new byte[1024];
				DatagramPacket packetReceived = new DatagramPacket(packet,packet.length);
				this.socket.receive(packetReceived);
				threadExecutor.execute(new UDP_RequestHandler(socket, packetReceived, invoker, marshaller, extensionService));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void connect(int port) {
		try {
			System.out.println("UDP - Starting Middleware on port " + port);
			this.socket = new DatagramSocket(port);
			System.out.println("UDP - Succesfully started Middleware on port " + port);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
