package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.google.gson.JsonObject;

import broker.Invoker;
import broker.interfaces.IMarshaller;
import exceptions.RemotingError;
import extension.ExtensionService;
import message.HTTPMessage;
import network.interfaces.HTTPHandler;

public class UDPRequestHandler implements HTTPHandler, Runnable{
	
	public DatagramSocket socket;

	public DatagramPacket packetReceived;
	
	public Invoker invoker;

    public IMarshaller marshaller;

    public ExtensionService extension;
    
    public UDPRequestHandler(DatagramSocket socket, DatagramPacket packetReceived, Invoker invoker, IMarshaller marshaller, ExtensionService extensionService){
        this.socket = socket;
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
    		
    		extension.verifyAfter(serverReply);
    		
    		this.sendResponse(serverReply);
    		
    	}catch(RemotingError e) {
    		HTTPMessage httpErrorMessage = new HTTPMessage();
            httpErrorMessage.setStatusCode(e.getCode());
            httpErrorMessage.setErrorMessage(e.getError());

            if(messageReceived != null){
                httpErrorMessage.setMethod(messageReceived.getMethod());
                httpErrorMessage.setResource(messageReceived.getResource());
            }

            extension.verifyAfter(httpErrorMessage);

            sendResponse(httpErrorMessage);
    	}
    }
    
	public HTTPMessage receiveRequest() throws RemotingError{

		try {
			byte[] packetData = this.packetReceived.getData();
			JsonObject packet = marshaller.deserialize(new String(packetData));

			HTTPMessage msg = new HTTPMessage();	
			msg.setMethod(packet.get("method").getAsString());
			msg.setResource(packet.get("resource").getAsString());
						
			packet.remove("method");
			packet.remove("resource");

			JsonObject bodyy = marshaller.deserialize(packet.toString().trim());
			JsonObject insideBodyy = marshaller.deserialize(bodyy.get("body").toString().trim());

			msg.setBody(insideBodyy.getAsJsonObject());

			return msg;

		} catch (Exception e) {
			e.printStackTrace();
		} catch(RemotingError e){
            throw e;
        }

		return null;
	}
	
	public void sendResponse(HTTPMessage replyMessage) {

		try {
			String headerLine = "HTTP/1.1 " 
					+ replyMessage.getStatusCode() + " " 
					+ replyMessage.getErrorMessage() + "\r\n";
			
			String headers = "\r\n";
			String emptyLine="\r\n";
	
			byte[] replyMessageSerialized = null;
	
			if(replyMessage.getBody() != null)
				replyMessageSerialized = marshaller.serialize(replyMessage.getBody());
	
			String httpMessage = headerLine + headers + emptyLine + new String(replyMessageSerialized);
	
	
			byte[] serverReplyBytes = httpMessage.getBytes();
			DatagramPacket packet = new DatagramPacket(serverReplyBytes, serverReplyBytes.length, packetReceived.getAddress(), packetReceived.getPort());
			socket.send(packet);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
