package com.epam.esm.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    GUEST("guest"),
    USER("user"),
    ADMIN("admin");

    private final String role;

    Role(final String role) {
        this.role = role;
    }

    Role() {
        this.role = this.name();
    }

    @JsonValue
    public String getRole() {
        return this.role;
    }
}
