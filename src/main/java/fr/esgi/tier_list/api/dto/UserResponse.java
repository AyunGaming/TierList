package fr.esgi.tier_list.api.dto;

import fr.esgi.tier_list.domain.User;

public record UserResponse(
    String id,
    String name,
    String email,
    String username
) {
    public static UserResponse fromDomain(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getUsername()
        );
    }
}