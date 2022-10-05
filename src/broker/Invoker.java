package broker;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.google.gson.JsonObject;

import annotations.DeleteMapping;
import annotations.GetMapping;
import annotations.PostMapping;
import annotations.PutMapping;
import annotations.RequestMapping;
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

	public JsonObject invoke(HTTPMessage msg){
		String httpMethod = msg.getMethod();
		String resource = msg.getResource(); //TODO: get class route from resource

		Class<?> clazz = map.get(resource); //TODO: change resource to the route from RequestMapping of class
		JsonObject result = null;
		try {
			for (Method method : clazz.getDeclaredMethods()) {

				if(method.isAnnotationPresent(GetMapping.class)){
					GetMapping getAnnotation = method.getAnnotation(GetMapping.class);
					if(getAnnotation.route().equals(resource)){
						result = (JsonObject) method.invoke(clazz);
					}
				}

				else if(method.isAnnotationPresent(PostMapping.class)){
					PostMapping getAnnotation = method.getAnnotation(PostMapping.class);
					if(getAnnotation.route().equals(resource)){
						result = (JsonObject) method.invoke(clazz);
					}
				}

				else if(method.isAnnotationPresent(PutMapping.class)){
					PutMapping getAnnotation = method.getAnnotation(PutMapping.class);
					if(getAnnotation.route().equals(resource)){
						result = (JsonObject) method.invoke(clazz);
					}
				}

				else if(method.isAnnotationPresent(DeleteMapping.class)){
					DeleteMapping getAnnotation = method.getAnnotation(DeleteMapping.class);
					if(getAnnotation.route().equals(resource)){
						result = (JsonObject) method.invoke(clazz);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return result;
	}
	
}
