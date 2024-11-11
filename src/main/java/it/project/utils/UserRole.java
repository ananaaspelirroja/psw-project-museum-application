package it.project.utils;

public enum UserRole {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}