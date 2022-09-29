import server.ServerRequestHandler;
import server.interfaces.Broker;

public class WinterMiddleware {

	public ServerRequestHandler requestHandler;
	
	
	
	public void addMethod(Object obj) {
		Class<?> clazz = obj.getClass();
	}
	
	public WinterMiddleware(int port) {
		
		
		this.start(port);
	}
	
	public void start(int port) {
		this.requestHandler = new ServerRequestHandler(port);
	}
	
	public static void main(String[] args) {
		new WinterMiddleware(Integer.parseInt(args[0]));

	}

}
