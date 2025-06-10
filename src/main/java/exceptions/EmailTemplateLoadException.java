package exceptions;

public class EmailTemplateLoadException extends RuntimeException{
    public EmailTemplateLoadException(String message, Throwable cause) {
        super(message, cause);
    }
    public EmailTemplateLoadException(String message) {
        super(message);
    }
}
