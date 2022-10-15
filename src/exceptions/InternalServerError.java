package exceptions;

public class InternalServerError extends RemotingError{

    public InternalServerError(){
        this.code = 500;
        this.error = "Internal Server Error";
    }

}
