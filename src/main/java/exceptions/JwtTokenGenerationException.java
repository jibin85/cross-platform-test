package exceptions;

public class JwtTokenGenerationException extends RuntimeException{
    public JwtTokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
    public JwtTokenGenerationException(String message) {
        super(message);
    }
}
