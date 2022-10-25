package broker;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import broker.interfaces.IMarshaller;
import broker.interfaces.IServerRequestHandler;
import extension.ExtensionService;
import network.UDPRequestHandler;


public class UDPServerRequestHandler implements IServerRequestHandler{
	
	public DatagramSocket socket;
	
	public ExecutorService threadExecutor;
	
	public IMarshaller marshaller;
	
	public Invoker invoker;
	
	public UDPServerRequestHandler(int port, Invoker invoker, IMarshaller marshaller, ExtensionService extensionService) {
		
		this.connect(port);
		
		this.threadExecutor = Executors.newFixedThreadPool(30);
		this.invoker = invoker;
		this.marshaller = marshaller;
		//ExtensionService extensionService = new ExtensionService();
		
		try {
			
			while(true) {
				byte[] packet = new byte[1024];
				DatagramPacket packetReceived = new DatagramPacket(packet,packet.length);
				this.socket.receive(packetReceived);
				threadExecutor.execute(new UDPRequestHandler(socket, packetReceived, invoker, new UdpMarshaller(), extensionService));
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
