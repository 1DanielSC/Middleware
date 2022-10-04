package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import com.google.gson.JsonObject;

import message.HTTPMessage;
import network.interfaces.HTTPHandler;
import server.Invoker;
import server.Marshaller;
import server.interfaces.IMarshaller;

public class RequestHandler implements HTTPHandler, Runnable{

    public Socket socket;
    
    public Invoker invoker;

    public IMarshaller marshaller;

    private BufferedReader in;
    private DataOutputStream out;

    public RequestHandler(Socket socket, Invoker invoker){
        this.socket = socket;
        this.invoker = invoker;
        this.marshaller = new Marshaller();
    }

    @Override
    public void run(){
    	
    	HTTPMessage messageReceived = receiveRequest(); //receber dados
    	
        JsonObject serverReply = invoke(messageReceived); //call the invoker --

        sendResponse(serverReply); //enviar dados
    }

    public JsonObject invoke(HTTPMessage message){ //call the invoker to invoke the appropriate remote object

        //get parameters from the marshaller
        //invoke appropriate class

        return null;
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
            httpRequest.setVerb(httpMethod);
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
