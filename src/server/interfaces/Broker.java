package server.interfaces;

public interface Broker {
    public void forwardRequest();
    public void forwardResponse();
    public void registerService();
}
