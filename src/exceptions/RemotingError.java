package exceptions;

public abstract class RemotingError extends Throwable {

    public String error;
    public int code;

    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    
}
