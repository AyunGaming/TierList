package fr.esgi.tier_list.api.controller.rest;

import fr.esgi.tier_list.api.dto.UserResponse;
import fr.esgi.tier_list.application.user.UserService;
import fr.esgi.tier_list.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegistrationRequest request) {
        User user = userService.register(
                request.name(),
                request.email(),
                request.username(),
                request.password());
        return new ResponseEntity<>(UserResponse.fromDomain(user), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(UserResponse.fromDomain(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.email(), request.password());

        return ResponseEntity.ok(UserResponse.fromDomain(user));
    }

    public record LoginRequest(String email, String password) {
    }

    public record UserRegistrationRequest(
            String name,
            String email,
            String username,
            String password) {
    }
}