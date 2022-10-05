import broker.Invoker;
import broker.Marshaller;
import broker.ServerRequestHandler;
import broker.interfaces.IMarshaller;

public class WinterMiddleware {

	public ServerRequestHandler requestHandler;
	
	public Invoker invoker;

	public IMarshaller marshaller;
	
	public void addMethod(Object obj) {
		invoker.registerService(obj);
	}
	
	public WinterMiddleware() {
		this.invoker = new Invoker();
		this.marshaller = new Marshaller();
	}
	
	public void start(int port) {
		this.requestHandler = new ServerRequestHandler(port, invoker, marshaller);
	}
	
	public static void main(String[] args) {
		WinterMiddleware inter = new WinterMiddleware();

		inter.addMethod(inter);

		inter.start(Integer.parseInt(args[0]));
	}

}
