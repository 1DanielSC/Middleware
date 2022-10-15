package exceptions;

public class BadRequestError extends RemotingError {
    
    public BadRequestError(){
        this.code = 400;
    }

    public BadRequestError(String errorMessage){
        this.code = 400;
        this.error = errorMessage;
    }
}
