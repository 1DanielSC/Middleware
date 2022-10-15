package exceptions;

public class NotFoundError extends RemotingError {
    
    public NotFoundError(){
        this.code = 404;
    }
}
