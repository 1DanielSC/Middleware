package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import com.google.gson.JsonObject;

import broker.Invoker;
import broker.interfaces.IMarshaller;
import exceptions.RemotingError;
import extension.ExtensionService;
import message.HTTPMessage;
import network.interfaces.HTTPHandler;

public class RequestHandler implements HTTPHandler, Runnable{

    public Socket socket;
    
    public Invoker invoker;

    public IMarshaller marshaller;

    public ExtensionService extension;

    public RequestHandler(Socket socket, Invoker invoker, IMarshaller marshaller, ExtensionService extensionService){
        this.socket = socket;
        this.invoker = invoker;
        this.marshaller = marshaller;
        this.extension = extensionService;
    }

    @Override
    public void run(){
    	try {
            
            HTTPMessage messageReceived = receiveRequest(); //receber dados
            
            extension.verifyBefore(messageReceived);
    
            HTTPMessage serverReply = invoker.invoke(messageReceived); //invocar objeto remoto
    
            extension.verifyAfter(serverReply);
    
            sendResponse(serverReply); //enviar dados

        } catch (RemotingError e) {

            HTTPMessage httpErrorMessage = new HTTPMessage();
            httpErrorMessage.setStatusCode(e.getCode());
            httpErrorMessage.setErrorMessage(e.getError());

            sendResponse(httpErrorMessage);
        }
    }


    public HTTPMessage receiveRequest() throws RemotingError{
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
		} catch(RemotingError e){
            throw e;
        }
        return null;
    }


    public void sendResponse(HTTPMessage replyMessage){
        DataOutputStream out = null;
        try {
            System.out.println("Enviando para a porta: " + socket.getPort());

            out = new DataOutputStream(this.socket.getOutputStream());
            
            String headerLine = "HTTP/1.1 " 
                + replyMessage.getStatusCode() + " " 
                + replyMessage.getErrorMessage() + "\r\n";

            String httpHeaders = "\r\n";
            String emptyLine="\r\n";

            byte[] replyMessageSerialized = null;

            if(replyMessage.getBody() != null)
                replyMessageSerialized = marshaller.serialize(replyMessage.getBody());


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
