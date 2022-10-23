package broker;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import broker.interfaces.IMarshaller;
import exceptions.BadRequestError;
import exceptions.RemotingError;

public class UdpMarshaller implements IMarshaller{
    public byte[] serialize(JsonObject message){
        return message.toString().getBytes();
    }

    public JsonObject deserialize(String body) throws RemotingError{
        if(body == null || body.equals("") ){
            return null;
        }

        try {
            JsonObject jsonObject = JsonParser.parseString(body.trim()).getAsJsonObject();
            return jsonObject;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestError();
        }

    }
}
