package cz.sohlich.natsproxy.exception;

/**
 * Created by Radomir Sohlich on 7/16/16.
 */
public class ClientException extends RuntimeException {

    public ClientException(String message) {
        super(message);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
