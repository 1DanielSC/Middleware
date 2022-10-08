package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import com.google.gson.JsonObject;

import broker.Invoker;
import broker.interfaces.IMarshaller;
import message.HTTPMessage;
import network.interfaces.HTTPHandler;

public class RequestHandler implements HTTPHandler, Runnable{

    public Socket socket;
    
    public Invoker invoker;

    public IMarshaller marshaller;

    public RequestHandler(Socket socket, Invoker invoker, IMarshaller marshaller){
        this.socket = socket;
        this.invoker = invoker;
        this.marshaller = marshaller;
    }

    @Override
    public void run(){
    	
    	HTTPMessage messageReceived = receiveRequest(); //receber dados
    	
        JsonObject serverReply = invoker.invoke(messageReceived); //invocar objeto remoto

        sendResponse(serverReply); //enviar dados
    }


    public HTTPMessage receiveRequest(){
        try  {
            BufferedReader in = new BufferedReader( new InputStreamReader( this.socket.getInputStream() ) );

			String headerLine = in.readLine();
			StringTokenizer tokenizer = new StringTokenizer(headerLine);

			String httpMethod = tokenizer.nextToken();
            String httpResource = tokenizer.nextToken();

            for(int i = 0; i < 6; i++){
                in.readLine();
            }
            
            String httpBody = in.readLine();

            HTTPMessage httpRequest = new HTTPMessage();
            httpRequest.setMethod(httpMethod.trim());
            httpRequest.setResource(httpResource.trim());

            JsonObject jsonObj = marshaller.deserialize(httpBody);
            httpRequest.setBody(jsonObj);

            return httpRequest; 

		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }


    public void sendResponse(JsonObject replyMessage){
        DataOutputStream out = null;
        try {
            System.out.println("Enviando para a porta: " + socket.getPort());

            out = new DataOutputStream(this.socket.getOutputStream());
            
            String headerLine = "HTTP/1.1 200 OK" + "\r\n";
            String httpHeaders = "\r\n";
            String emptyLine="\r\n";

            byte[] replyMessageSerialized = marshaller.serialize(replyMessage);

            if(socket.isOutputShutdown()){
                System.out.println("out was Shutdown");
            }

            if(!socket.isOutputShutdown()){
                System.out.println("O out nao esta fechado");
            }

            if(socket.isClosed()){
                System.out.println("socket closed");
            }

            
            out.writeBytes(headerLine);
            out.writeBytes(httpHeaders);
            out.writeBytes(emptyLine);
            out.writeBytes(new String(replyMessageSerialized));

            out.flush();
            out.close();

            System.out.println("oi");

        } catch (Exception e) {

            if(socket.isClosed()){
                System.out.println("socket closed2");
            }

            if(!socket.isOutputShutdown()){
                System.out.println("O out nao esta fechado2");
            }

            if(socket.isOutputShutdown()){
                System.out.println("out was Shutdown2");
            }

            if(socket.isConnected() && socket.isBound()){
                System.out.println("socket conectado");
            }


            e.printStackTrace();
        }finally{
			try {
				if(out != null) out.close();
                if(this.socket != null) this.socket.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
    }
}
