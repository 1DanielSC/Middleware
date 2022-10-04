package network.interfaces;

import com.google.gson.JsonObject;

import message.HTTPMessage;

public interface HTTPHandler {
    public void sendResponse(JsonObject replyMessage);
    public HTTPMessage receiveRequest();
}
