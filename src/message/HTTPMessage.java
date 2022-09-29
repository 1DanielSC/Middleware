package message;

import com.google.gson.JsonObject;

public class HTTPMessage {
	public String verb;
	public String resource;
	public JsonObject body;
	
	
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
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
