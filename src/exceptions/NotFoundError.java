package exceptions;

public class NotFoundError extends RemotingError {
    
    public NotFoundError(){
        this.code = 404;
    }

    public NotFoundError(String errorMessage){
        this.code = 404;
        this.error = errorMessage;
    }
}
