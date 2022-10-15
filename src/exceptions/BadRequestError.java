package exceptions;

public class BadRequestError extends RemotingError {
    
    public BadRequestError(){
        this.code = 400;
        this.error = "Bad Request";
    }
}
