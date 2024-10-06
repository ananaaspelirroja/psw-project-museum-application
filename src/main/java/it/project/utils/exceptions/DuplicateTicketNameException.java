package it.project.utils.exceptions;

public class DuplicateTicketNameException extends RuntimeException {

    public DuplicateTicketNameException() {
        super("A ticket with this name already exists.");
    }
}
