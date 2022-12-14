package extension;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import message.HTTPMessage;

public class LoggingService {
    
    public void verifyAfter(HTTPMessage msg) {
        try {
            saveOnFile(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveOnFile(HTTPMessage msg) throws IOException{
        FileWriter writer = new FileWriter("MiddlewareLogging.txt", true);

        writer.write("Requested route: (" + msg.getMethod() + ") " + msg.getResource()+"\n");
        writer.write("Status: " + msg.getStatusCode() + " " + msg.getErrorMessage() + "\n");
        if(msg.getBody() != null){
            writer.write("Result: " + msg.getBody().toString()+"\n");
        }
        writer.write("Finished at: " + getDateAndTime()+"\n");
        
        writer.write("\n");
        writer.close();
    }
    
    public String getDateAndTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}
