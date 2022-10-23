package message;

import com.google.gson.JsonObject;

public class HTTPMessage {
	public String method;
	public String resource;
	public JsonObject body;

	public int statusCode;
	public String errorMessage;
	
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
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	@Override
	public String toString() {
		return "HTTPMessage [method=" + method + ", resource=" + resource + ", body=" + body + ", statusCode="
				+ statusCode + ", errorMessage=" + errorMessage + "]";
	}
	
}
