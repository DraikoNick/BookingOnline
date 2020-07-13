package by.draikon.model.exceptions;

public class InitException extends Throwable{

    private final String message;
    public InitException(String message){
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
