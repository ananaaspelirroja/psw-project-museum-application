package it.project.utils.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException {

    public CategoryAlreadyExistsException() {
        super("Category already exists.");
    }
}
