package co.com.crediya.restconsumer;

import org.springframework.http.HttpStatus;

public class UsuarioAuthException extends RuntimeException {

    private final HttpStatus status;

    public UsuarioAuthException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
