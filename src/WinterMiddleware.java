import application.Buy;
import broker.Invoker;
import broker.Marshaller;
import broker.ServerRequestHandler;
import broker.UDPServerRequestHandler;
import broker.UdpMarshaller;
import broker.interfaces.IServerRequestHandler;

public class WinterMiddleware {

	public IServerRequestHandler requestHandler;
	
	public Invoker invoker;
	
	public void addMethod(Object obj) {
		invoker.registerService(obj);
	}
	
	public WinterMiddleware() {
		this.invoker = new Invoker();
	}
	
	public void start(int port, String networkProtocol) {

		switch (networkProtocol) {
			case "udp":
				this.requestHandler = new UDPServerRequestHandler(port, invoker, new UdpMarshaller());
				break;

			case "tcp":
				this.requestHandler = new ServerRequestHandler(port, invoker, new Marshaller());
				break;
				
			case "http":
				this.requestHandler = new ServerRequestHandler(port, invoker, new Marshaller());
				break;

			default:
				System.out.println("Incorrect network layer protocol");
				System.exit(1);
				break;
		}
		
	}
	
	public static void main(String[] args) {
		WinterMiddleware inter = new WinterMiddleware();

		Buy buyClass = new Buy();
		
		inter.addMethod(buyClass);

		inter.start(9000, args[0]);
	}

}
