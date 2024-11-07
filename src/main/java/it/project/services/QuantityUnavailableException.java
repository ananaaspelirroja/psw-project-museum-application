package it.project.utils.exceptions;

public class QuantityUnavailableException extends RuntimeException {

    public QuantityUnavailableException() {
        super("Requested quantity is unavailable.");
    }

    public QuantityUnavailableException(String message) {
        super(message);
    }

    public QuantityUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuantityUnavailableException(Throwable cause) {
        super(cause);
    }
}
