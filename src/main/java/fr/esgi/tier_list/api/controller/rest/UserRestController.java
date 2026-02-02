package fr.esgi.tier_list.api.controller.rest;

import fr.esgi.tier_list.api.dto.UserResponse;
import fr.esgi.tier_list.application.user.UserService;
import fr.esgi.tier_list.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegistrationRequest request) {
        User user = userService.register(
            request.name(), 
            request.email(), 
            request.username()
        );
        return new ResponseEntity<>(UserResponse.fromDomain(user), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(UserResponse.fromDomain(user));
    }

    public record UserRegistrationRequest(
        String name, 
        String email, 
        String username
    ) {}
}