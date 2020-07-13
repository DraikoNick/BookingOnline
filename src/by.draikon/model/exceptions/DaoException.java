package by.draikon.model.exceptions;

public class DaoException extends Throwable {

    private final String message;
    public DaoException(String message){
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}