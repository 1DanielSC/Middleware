package broker;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import broker.interfaces.IMarshaller;

public class Marshaller implements IMarshaller{
    public byte[] serialize(JsonObject message){
        return message.getAsString().getBytes();
    }

    public JsonObject deserialize(String body){
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject;
    }
}
