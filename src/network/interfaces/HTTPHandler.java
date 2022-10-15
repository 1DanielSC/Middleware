package network.interfaces;

import exceptions.RemotingError;
import message.HTTPMessage;

public interface HTTPHandler {
    public void sendResponse(HTTPMessage replyMessage);
    public HTTPMessage receiveRequest() throws RemotingError;
}
