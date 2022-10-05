package message;

import com.google.gson.JsonObject;

public class HTTPMessage {
	public String method;
	public String resource;
	public JsonObject body;
	
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public JsonObject getBody() {
		return body;
	}
	public void setBody(JsonObject body) {
		this.body = body;
	}
	
}
