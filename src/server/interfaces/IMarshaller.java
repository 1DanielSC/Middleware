package server.interfaces;

import com.google.gson.JsonObject;

public interface IMarshaller {
    public byte[] serialize(JsonObject message);
    public JsonObject deserialize(String body);
}
