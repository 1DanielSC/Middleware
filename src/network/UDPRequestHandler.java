package network;

import java.net.DatagramPacket;

import broker.Invoker;
import broker.interfaces.IMarshaller;
import exceptions.RemotingError;
import extension.ExtensionService;
import message.HTTPMessage;
import network.interfaces.HTTPHandler;

public class UDPRequestHandler implements HTTPHandler, Runnable{
	
	public DatagramPacket packetReceived;
	
	public Invoker invoker;

    public IMarshaller marshaller;

    public ExtensionService extension;
    
    public UDPRequestHandler(DatagramPacket packetReceived, Invoker invoker, IMarshaller marshaller, ExtensionService extensionService){
        this.packetReceived = packetReceived;
        this.invoker = invoker;
        this.marshaller = marshaller;
        this.extension = extensionService;
    }
    
    @Override
    public void run() {
    	HTTPMessage messageReceived = null;
    	
    	try {
    		messageReceived = this.receiveRequest();
    		
    		extension.verifyBefore(messageReceived);
    		
    		HTTPMessage serverReply = invoker.invoke(messageReceived);
    		
    		extension.verifyAfter(messageReceived);
    		
    		this.sendResponse(serverReply);
    		
    	}catch(RemotingError e) {
    		e.printStackTrace();
    	}
    }
    
	public HTTPMessage receiveRequest() throws RemotingError{
		return null;
	}
	
	public void sendResponse(HTTPMessage replyMessage) {
		
	}
	
}
