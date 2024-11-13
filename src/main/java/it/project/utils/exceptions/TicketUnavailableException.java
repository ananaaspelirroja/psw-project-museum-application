package it.project.utils.exceptions;

public class TicketUnavailableException extends RuntimeException {

    public TicketUnavailableException() {
        super("Il biglietto richiesto non è più disponibile.");
    }

    public TicketUnavailableException(String message) {
        super(message);
    }
}
