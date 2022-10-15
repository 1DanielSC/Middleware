package exceptions;

public class NotFoundError extends RemotingError {
    
    public NotFoundError(){
        this.code = 404;
        this.error = "Not Found";
    }

}
