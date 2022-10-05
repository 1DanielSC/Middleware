package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import com.google.gson.JsonObject;

import broker.Invoker;
import broker.Marshaller;
import broker.interfaces.IMarshaller;
import message.HTTPMessage;
import network.interfaces.HTTPHandler;

public class RequestHandler implements HTTPHandler, Runnable{

    public Socket socket;
    
    public Invoker invoker;

    public IMarshaller marshaller;

    private BufferedReader in;
    private DataOutputStream out;

    public RequestHandler(Socket socket, Invoker invoker, IMarshaller marshaller){
        this.socket = socket;
        this.invoker = invoker;
        this.marshaller = marshaller;
    }

    @Override
    public void run(){
    	
    	HTTPMessage messageReceived = receiveRequest(); //receber dados
    	
        JsonObject serverReply = invoker.invoke(messageReceived);
        
        sendResponse(serverReply); //enviar dados
    }


    public HTTPMessage receiveRequest(){
        try  {
            in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );

			String headerLine = in.readLine();
			StringTokenizer tokenizer = new StringTokenizer(headerLine);

			String httpMethod = tokenizer.nextToken();
            String httpResource = tokenizer.nextToken();

			while(in.readLine() != "\n"){ } //ignore http headers

            String httpBody = in.readLine();

            HTTPMessage httpRequest = new HTTPMessage();
            httpRequest.setMethod(httpMethod);
            httpRequest.setResource(httpResource);

            JsonObject jsonObj = marshaller.deserialize(httpBody);
            httpRequest.setBody(jsonObj);

            return httpRequest; 

		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }


    public void sendResponse(JsonObject replyMessage){
        Socket client = null;
        try {
            client = new Socket("localhost", socket.getPort());
            out = new DataOutputStream(client.getOutputStream());

            String headerLine = "HTTP/1.0 200 OK \r\n";
            String httpHeaders = "\r\n";
            String emptyLine="\r\n";
            
            byte[] replyMessageSerialized = marshaller.serialize(replyMessage);

            out.writeBytes(headerLine);
            out.writeBytes(httpHeaders);
            out.writeBytes(emptyLine);
            out.writeBytes(new String(replyMessageSerialized));

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
			try {
				if(out != null) out.close();
				if(client != null) client.close();
                if(socket != null) socket.close(); //TODO: check this
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
    }
}
