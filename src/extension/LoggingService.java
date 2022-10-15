package extension;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonObject;

import message.HTTPMessage;

public class LoggingService {
    
    public void verifyAfter(HTTPMessage msg, Object informationContext) {
        JsonObject result = (JsonObject) informationContext;
        try {
            saveOnFile(msg, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveOnFile(HTTPMessage msg, JsonObject result) throws IOException{
        FileWriter writer = new FileWriter("MiddlewareLogging.txt", true);

        writer.write("Requested route: (" + msg.getMethod() + ") " + msg.getResource()+"\n");
        writer.write("Operation result: " + result.toString()+"\n");
        writer.write("Ended on: " + getDateAndTime()+"\n");

        writer.write("\n");
        writer.close();
    }
    
    public String getDateAndTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}
