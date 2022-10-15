package broker;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.google.gson.JsonObject;

import annotations.DeleteMapping;
import annotations.GetMapping;
import annotations.PostMapping;
import annotations.PutMapping;
import annotations.RequestMapping;
import exceptions.NotFoundError;
import message.HTTPMessage;

public class Invoker {

	public HashMap<String, Class<?>> map;

	public Invoker() {
		this.map = new HashMap<>();
	}

	public void registerService(Object obj){
		Class<?> objClass = obj.getClass();

		if(objClass.isAnnotationPresent(RequestMapping.class)){
			RequestMapping annotation = objClass.getAnnotation(RequestMapping.class);
			String route = annotation.name();
			map.put(route, objClass);
		}
	}

	public HTTPMessage invoke(HTTPMessage msg) throws NotFoundError{
		String httpMethod = msg.getMethod();
		String resource = msg.getResource();

		String[] routes = resource.split("/");
		String classRoute = "/" + routes[1];
		String methodRoute = resource.replace(classRoute, "");

		Class<?> clazz = map.get(classRoute);
		
		JsonObject result = null;
		try {
			for (Method method : clazz.getDeclaredMethods()) {

				if(method.isAnnotationPresent(GetMapping.class) && httpMethod.equals("GET")){
					GetMapping getAnnotation = method.getAnnotation(GetMapping.class);
					if(getAnnotation.route().equals(methodRoute)){
						result = (JsonObject) method.invoke(clazz.getConstructor().newInstance(), msg.getBody());
					}
				}
				else if(method.isAnnotationPresent(PostMapping.class) && httpMethod.equals("POST")){
					PostMapping getAnnotation = method.getAnnotation(PostMapping.class);
					if(getAnnotation.route().equals(methodRoute)){
						result = (JsonObject) method.invoke(clazz.getConstructor().newInstance(), msg.getBody());
					}
				}
				else if(method.isAnnotationPresent(PutMapping.class) && httpMethod.equals("PUT")){
					PutMapping getAnnotation = method.getAnnotation(PutMapping.class);
					if(getAnnotation.route().equals(methodRoute)){
						result = (JsonObject) method.invoke(clazz.getConstructor().newInstance(), msg.getBody());
					}
				}
				else if(method.isAnnotationPresent(DeleteMapping.class) && httpMethod.equals("DELETE")){
					DeleteMapping getAnnotation = method.getAnnotation(DeleteMapping.class);
					if(getAnnotation.route().equals(methodRoute)){
						result = (JsonObject) method.invoke(clazz.getConstructor().newInstance(), msg.getBody());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(result == null){
			throw new NotFoundError();
		}
		
		HTTPMessage reply = new HTTPMessage();
		reply.setMethod(msg.getMethod());
		reply.setResource(msg.getResource());
        reply.setBody(result);
        reply.setStatusCode(200);
        reply.setErrorMessage("OK");

		return reply;
	}
	

}
