package fr.esgi.tier_list.domain;

import lombok.Getter;

@Getter
public class User {
    private final String id;
    private final String name;
    private final String email;
    private final String username;

    public User(String id, String name, String email, String username) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("User email cannot be empty");
        }

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("User username cannot be empty");
        }

        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
    }
}
