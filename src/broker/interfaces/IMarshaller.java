package broker.interfaces;

import com.google.gson.JsonObject;

import exceptions.RemotingError;

public interface IMarshaller {
    public byte[] serialize(JsonObject message);
    public JsonObject deserialize(String body) throws RemotingError;
}
