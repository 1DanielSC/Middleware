package network.interfaces;

import com.google.gson.JsonObject;

public interface HTTPHandler {
    public void sendResponse(JsonObject replyMessage);
    public JsonObject receiveRequest();
}
