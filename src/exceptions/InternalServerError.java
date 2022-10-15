package exceptions;

public class InternalServerError extends RemotingError{

    public InternalServerError(){
        this.code = 500;
    }

    public InternalServerError(String errorMessage){
        this.code = 500;
        this.error = errorMessage;
    }
}
