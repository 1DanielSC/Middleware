package broker;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import broker.interfaces.IMarshaller;
import exceptions.BadRequestError;
import exceptions.RemotingError;

public class Marshaller implements IMarshaller{
    public byte[] serialize(JsonObject message){
        return message.toString().getBytes();
    }

    public JsonObject deserialize(String body) throws RemotingError{
        try {

            JsonElement jsonElement = JsonParser.parseString(body);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return jsonObject;

        } catch (Exception e) {
            throw new BadRequestError();
        }
    }
}
