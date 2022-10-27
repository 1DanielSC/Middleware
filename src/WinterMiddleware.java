import application.Buy;
import broker.TCP_ServerRequestHandler;
import broker.UDP_ServerRequestHandler;
import broker.interfaces.IServerRequestHandler;
import broker.invoker.Invoker;
import broker.marshallers.TCP_Marshaller;
import broker.marshallers.UDP_Marshaller;
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

	public void activateLogging(){
		this.extensionService.addService(LoggingService.class);
	}

	public WinterMiddleware() {
		this.invoker = new Invoker();
		this.extensionService = new ExtensionService();
	}
	
	public void start(int port, String networkProtocol) {

		switch (networkProtocol) {
			case "udp":
				this.requestHandler = new UDP_ServerRequestHandler(port, invoker, new UDP_Marshaller(), extensionService);
				break;

			case "tcp":
				this.requestHandler = new TCP_ServerRequestHandler(port, invoker, new TCP_Marshaller(), extensionService);
				break;
				
			case "http":
				this.requestHandler = new TCP_ServerRequestHandler(port, invoker, new TCP_Marshaller(), extensionService);
				break;

			default:
				System.out.println("Incorrect network layer protocol");
				System.exit(1);
				break;
		}
		
	}
	
	/*
	public static void main(String[] args) {
		WinterMiddleware inter = new WinterMiddleware();

		Buy buyClass = new Buy();
		
		inter.addMethod(buyClass);
		
		inter.activateLogging();

		inter.start(9000, "tcp");
	}
	*/

}
