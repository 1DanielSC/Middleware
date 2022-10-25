import application.Buy;
import broker.Invoker;
import broker.Marshaller;
import broker.ServerRequestHandler;
import broker.UDPServerRequestHandler;
import broker.UdpMarshaller;
import broker.interfaces.IServerRequestHandler;
import extension.ExtensionService;
import extension.LoggingService;

public class WinterMiddleware {

	public IServerRequestHandler requestHandler;
	
	public ExtensionService extensionService;
	
	public Invoker invoker;
	
	public void addMethod(Object obj) {
		invoker.registerService(obj);
	}
	
	public void addExtensionService(Class<?> clazz) {
		extensionService.addService(clazz);
	}
	
	public WinterMiddleware() {
		this.invoker = new Invoker();
		this.extensionService = new ExtensionService();
	}
	
	public void start(int port, String networkProtocol) {

		switch (networkProtocol) {
			case "udp":
				this.requestHandler = new UDPServerRequestHandler(port, invoker, new UdpMarshaller(), extensionService);
				break;

			case "tcp":
				this.requestHandler = new ServerRequestHandler(port, invoker, new Marshaller(), extensionService);
				break;
				
			case "http":
				this.requestHandler = new ServerRequestHandler(port, invoker, new Marshaller(), extensionService);
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
		
		//LoggingService a = new LoggingService();
		
		//inter.addExtensionService(a.getClass());

		inter.start(9000, "tcp");
	}

}
