package exceptions;

public class PrivateKeyLoadException extends RuntimeException{
    public PrivateKeyLoadException(String message, Throwable cause) {
        super(message, cause);
    }
    public PrivateKeyLoadException(String message) {
        super(message);
    }
}
