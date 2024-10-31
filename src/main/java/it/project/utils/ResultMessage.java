package it.project.utils;

public class ResultMessage { //result  message for an operation
    private String message;
    private Integer id;

    public ResultMessage(String message) {
        this.message = message;
    }

    public ResultMessage(String message, Integer id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
