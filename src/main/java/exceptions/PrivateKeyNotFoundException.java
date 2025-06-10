package exceptions;

public class PrivateKeyNotFoundException extends RuntimeException{
    public PrivateKeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public PrivateKeyNotFoundException(String message) {
        super(message);
    }
}
